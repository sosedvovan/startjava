package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.model.Section;
import com.urise.webapp.model.SectionType;
import com.urise.webapp.sql.ConnectionFactory;
import com.urise.webapp.sql.SqlExecutor;
import com.urise.webapp.sql.SqlHelper;
import com.urise.webapp.sql.SqlTransaction;
import com.urise.webapp.util.JsonParser;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//часть методов AbstractStorage не потребуется(geSearchKey, )
//поэтому сразу implements от Storage, а не от AbstractStorage.

//в методах этого класса мы, по большому счету, будем доделывать запос(вопросики??? заменять пришедшими извне значениями)
//и отправлять готовый prepareStatement в методы класса SqlHelper на выполнение

public class SqlStorage implements Storage {

    //декларируем поле с объектом класса SqlHelper.
    //для его инициализации в конструкторе надо реализовать абстракт getConnection()
    //нашего функционального интерфейса ConnectionFactory
    public final SqlHelper sqlHelper;

    //------------------------------------------------------------------------------------------------------------------

    //конструктор- создаем объект этого класса SqlStorage, передавая в аргументы логины-пароли.
    //объект этого класса будет содержать в себе объект класса SqlHelper, который мы инициализируем нужным нам подключением.
    //SqlHelper в этом конструкторе инициализируется реализацией connectionFactory, абстракт которого возвращает Connection.
    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {

        //подгрузим драйвер, чтобы он точно нашелся(если не так(взяли с собой) то драйвер нужно будет подложить в Томкат
        // стобы класс-лоадер Томката его увидел)
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }

        sqlHelper = new SqlHelper(new ConnectionFactory() {
            @Override
            public Connection getConnection() throws SQLException {
                return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            }
        });
    }

    //------------------------------------------------------------------------------------------------------------------
    //                                        clear():
    //в методе clear() вызывается первый перегруженный метод sqlHelper.execute()
    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

    //------------------------------------------------------------------------------------------------------------------
    //                                        get():
    //в этом методе вызываем второй перегруженный метод sqlHelper.execute()
    //передавая ему в аргументы стрингу с запросом и реализацию SqlExecutor<T>(кусок уместного в данном случае кода)
    //в этом вызванном методе произийдет подключение к дб, создание PreparedStatement ps, передача в ps запроса с =?,
    //далее этот ps передается в запускаемый там абстракт(наш кусок кода), где окончательно сформируется запрос(uuid вместо =?)
    //далее вызов ps.executeQuery() и результат присваивается в ResultSet rs
    //далее итерируемся по ResultSet rs, и если там что-то лежит собираем и возвращаем new Resume()
    //
    //sql запрос сделаем с JOIN. в таблицу contact добавили строки PHONE и SKYPE для первого resume.
    //в запросе: JOIN contact - это обединяем с таблицей resume. ON... объединение по указанному предикату
    //JOIN - в результирующую таблицу войдут только пересекающиеся области
    //LEFT JOIN - в результирующую таблицу войдут пересекающиеся области и вся первая таблица
    //RIGHT JOIN - в результирующую таблицу войдут пересекающиеся области и вся вторая таблица
    //есть еще outer JOIN(полное объединение) - редко используют, как и RIGHT JOIN, как и Cross JOIN(перемножение)
    //этот запрос возвращает одну объединенную таблицу-один ко многим(резюме одно, а контактов много).
    //если здесь использовать просто JOIN (а не LEFT JOIN), то резюме без контактов не вернутся в результирующую таблицу
    //тк в таблице контактов они не представленны, а JOIN возвращает пересекающиеся области.
//    @Override
//    public Resume get(String uuid) {
//        return sqlHelper.execute("" +
//                        "    SELECT * FROM resume r " +
//                        " LEFT JOIN contact c " +
//                        "        ON r.uuid = c.resume_uuid " +
//                        "     WHERE r.uuid =? ",
//                ps -> {
//                    ps.setString(1, uuid);
//                    ResultSet rs = ps.executeQuery();
//                    if (!rs.next()) {
//                        throw new NotExistStorageException(uuid);
//                    }//если в ResultSet чтото есть - соберем объект Resume
//                    Resume r = new Resume(uuid, rs.getString("full_name"));
//
//                    do {
//                        addContact(rs, r);
//                    } while (rs.next());
//                    return r;
//                });
//    }

    //перепишем метод get не через LEFT JOIN, а через раздельные запросы, учитывая что уже добавили секции:

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(conn -> {
            Resume r;
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume WHERE uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                r = new Resume(uuid, rs.getString("full_name"));
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact WHERE resume_uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addContact(rs, r);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section WHERE resume_uuid =?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addSection(rs, r);
                }
            }

            return r;
        });
    }

    //------------------------------------------------------------------------------------------------------------------

    //сначала обновляем full_name по его uuid'у,
    //а при добавлении контактов можно реализовать сложную логику- проверки: а сущ. ли такой контакт,
    //а сущ. и изменен ли, а может вооюще такой не существовал
    //но можно пойти путем попроще- сначала удалить все контакты, а потом записать пришедшие
    //для этого создадим доп методы deleteContacts и insertContact
    //ДАЛЕЕ ПЕРЕДЕЛАЕМ : БУДЕМ ЧЗ ТРАНЗАКЦИИ тк ps у нас один и он будет обновлять full_name
    //те исп. метод transactionalExecute а не execute в sqlHelper
//    @Override
//    public void update(Resume r) {
//        sqlHelper.execute("UPDATE resume SET full_name = ? WHERE uuid = ?", new SqlExecutor<Object>() {
//            @Override//этот переопределенный метод- кусок кода, кот. мы передаем методу sqlHelper.execute
//            //вместе со стрингой с недоделанным запросом(= ? = ?)
//            //
//            public Object execute(PreparedStatement ps) throws SQLException {
//                //доделываем недоделанный запрос:
//                ps.setString(1, r.getFullName());
//                ps.setString(2, r.getUuid());
//                if (ps.executeUpdate() == 0) {//вызываем executeUpdate() (если неудачно, возвратится 0 и выбросим ексепшен)
//                    throw new NotExistStorageException(r.getUuid());
//                }
//                deleteContacts(conn, r);
//                insertContact(conn, r);
//                return null;//при update() ничего возвращать из метода не надо
//            }
//        });
//    }

    @Override
    public void update(Resume r) {
        sqlHelper.transactionalExecute(new SqlTransaction<Object>() {
            @Override
            public Object execute(Connection conn) throws SQLException {
                try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                    ps.setString(1, r.getFullName());
                    ps.setString(2, r.getUuid());
                    //executeUpdate возвращает кол-во записей, которое он обновил:
                    //у нас uuid это primaryKey, значит вернется или 1 или 0
                    if (ps.executeUpdate() != 1) {
                        throw new NotExistStorageException(r.getUuid());
                    }
                }
                deleteContacts(conn, r);
                deleteSections(conn, r);
                insertContacts(conn, r);
                insertSections(conn, r);
                return null;
            }
        });
    }

    //------------------------------------------------------------------------------------------------------------------
//                                          save():
//    @Override
//    public void save(Resume r) {
//        //если из типизированного метода мы ничего не хотим возратить, воспользуемся <Void>
//        //здесь в лямбде ps-это объект PreparedStatement со стрингой содержащей =???, и после оператора -> замещаем =? на значения и ps.execute()
//        sqlHelper.<Void>execute("INSERT INTO resume (uuid, full_name) VALUES (?,?)", ps -> {
//            ps.setString(1, r.getUuid());
//            ps.setString(2, r.getFullName());
//            ps.execute();
//            return null;
//        });
//    }

//    @Override //ДОБАВИЛИ СОХРАНЕНИЕ С КОНТАКТАМИ:
//    public void save(Resume r) {
//        //если из типизированного метода мы ничего не хотим возратить, воспользуемся <Void>
//        //здесь в лямбде ps-это объект PreparedStatement со стрингой содержащей =???,
//        //и после оператора -> замещаем =? на значения и ps.execute()(кусок кода отправляем в др метод)
//        sqlHelper.<Void>execute("INSERT INTO resume (uuid, full_name) VALUES (?,?)", ps -> {
//            ps.setString(1, r.getUuid());
//            ps.setString(2, r.getFullName());
//            ps.execute();
//            return null;
//        });
//        //для добавления контактов в Resume r создадим еще один PreparedStatement:
//        //так не эффективно, тк получается несколько запросов в дб (далее исправим)
//        for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {//на мапе вызываем entrySet()
//            sqlHelper.<Void>execute("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)", ps -> {
//                ps.setString(1, r.getUuid());//добавляем в запрос Uuid
//                ps.setString(2, e.getKey().name());//добавляем в запрос енум(контакт) типа PHONE или SKYPE(это ключ мапы контактов)
//                ps.setString(3, e.getValue());//добавляем в запрос енум(контакт) строковое значение от енума-ключа типа +79846363748
//                ps.execute();
//                return null;
//            });
//        }
//    }

    @Override //СДЕЛАЛИ СОХРАНЕНИЕ ЧЕРЕЗ ТРАНСАКЦИЮ:
    public void save(Resume r) {
        //на нашем conn - делаем запрос для записи в таблицу resume полей: uuid и full_name:
        sqlHelper.transactionalExecute(conn -> {
                    try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, r.getFullName());
                        ps.execute();//возможно ошибка- заменить надо на addBatch()
                    }
            //на том же conn - делаем запрос для записи в таблицу contact полей: resume_uuid, type, value:
            //перенесли эту часть кода в отдельный метод insertContact
                    insertContacts(conn, r);
                    insertSections(conn, r);
                    return null;
                }
        );
    }

    //------------------------------------------------------------------------------------------------------------------

    //тк в таблицах дб с вторичным ключем которые, сделали каскадное удаление(при удалении из таблицы резюме с первичным
    // ключем) то метод delete не меняем для контактов и секций.
    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume WHERE uuid=?", new SqlExecutor<Object>() {
            @Override
            public Object execute(PreparedStatement ps) throws SQLException {
                ps.setString(1, uuid);
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
                return null;
            }
        });
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM resume", st -> {
            ResultSet rs = st.executeQuery();
            //next() в классе ResultSet не только проверяет- есть ли еще след. запись (возвр true)
            //но и передвигает курсор, если такая запись есть:
            return rs.next() ? rs.getInt(1) : 0;//getInt из ResultSet достанет одно первое число, если оно есть
        });
    }
//----------------------------------------------------------------------------------------------------------------------
//                                         getAllSorted():
    //ДАЛЕЕ ПЕРЕПИШЕМ, ЧТОБЫ И КОНТАКТЫ ДОСТАВАТЬ. через LEFT JOIN будем это делать
    //можно еще через 2- запроса(получить full_name, потом контакты, потом склеить)
//    @Override
//    public List<Resume> getAllSorted() {
//        //sql запрос с сортировкой ORDER BY
//        return sqlHelper.execute("SELECT * FROM resume r ORDER BY full_name,uuid", ps -> {
//            ResultSet rs = ps.executeQuery();
//            List<Resume> resumes = new ArrayList<>();
//            //next() в классе ResultSet не только проверяет- есть ли еще след. запись (возвр true)
//            //но и передвигает курсор, если такая запись есть:
//            while (rs.next()) {
//                //rs.getString() берет значение ячейки в таблице
//                resumes.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
//            }
//            return resumes;
//        });
//    }


    //1 в sqlHelper.execute() отправляем JOIN запрос(склеим 2-е таблицы по uuid)
    //2 и также отправляем кусок кода в котором ResultSet получает результирующую таблицу,
    //3 создаем новую служебную мапу, она LinkedHashMap чтобы сохраняла порядок занесения
    //  тк заносится будет уже отсортированные данные с пом ORDER BY
    //4 далее в цикле while:
    //5 переменной String uuid присваиваем значение "uuid" из очередной ячейки из таблицы из ResultSet
    //6 создаем объект Resume resume пытаясь взять из его из служебной мапы  по String uuid
    //такого объекта в мапе не будет, соответственно Resume resume = null
    //7 и далее в предикате if видим, что нет такого резюме (resume == null)
    //8 и далее собираем объект Resume resume
    //9 и кладем его в служебную мапу
    //10 далее не выходя из цикла while вызываем addContact в котром к текущему Resume resume добавляем контакты
    //11 из куска кода возвращаем новый ArrayList с объектами Resume resume
//    @Override
//    public List<Resume> getAllSorted() {
//        return sqlHelper.execute("" +    //1
//                "   SELECT * FROM resume r\n" +
//                "LEFT JOIN contact c ON r.uuid = c.resume_uuid\n" +
//                "ORDER BY full_name, uuid", ps -> {   //2
//            ResultSet rs = ps.executeQuery();   //2
//            Map<String, Resume> map = new LinkedHashMap<>();  //3
//            while (rs.next()) {  //4
//                String uuid = rs.getString("uuid");   //5
//                Resume resume = map.get(uuid);  //6
//                if (resume == null) { //7
//                    resume = new Resume(uuid, rs.getString("full_name")); //8
//                    map.put(uuid, resume);  //9
//                }
//                addContact(rs, resume); //11
//            }
//            return new ArrayList<>(map.values());
//        });
//    }

    // Todo: Сделать реализацию SqlStorage.getAllSorted через 2 отдельных запроса: отдельно резюме и отдельно контакты.
    //  Добавить в реализацию SqlStorage и в базу секции (кроме OrganizationSection). Для ListSection склеиваем строки через \n.
    // будем делать на одном коннекте через транзакцию, чтобы оперрация была атомарной
    //1  в метод sqlHelper.transactionalExecute() отправляем кусок кода
    //2  создаем новую служебную мапу. LinkedHashMap<>будет сохранять порядок отсортированоого через order by занесения
    //3  из таблицы резюме берем отсортированный список, результирующую таблицу кладем в ResultSet. в цикле идем
    //   по ResultSet и кладем в служебную мапу ключ-uuid и значение-new Resume
    //   [в postgresql закрывать ResultSet в блоке try с ресурсами необязательно]
    //4  достаем все контакты, проходимся по всем записям, достаем Resume r по значению в ячейке "resume_uuid",
    //   и добавляем контакт в Resume r в методе addContact()
    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalExecute(conn -> {//1
            Map<String, Resume> resumes = new LinkedHashMap<>();//2

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {//3
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    resumes.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")) {//4
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume r = resumes.get(rs.getString("resume_uuid"));
                    addContact(rs, r);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume r = resumes.get(rs.getString("resume_uuid"));
                    addSection(rs, r);
                }
            }

            return new ArrayList<>(resumes.values());
        });
    }
    //------------------------------------------------------------------------------------------------------------------

    //в этот метод, код переехал из get тк этот код повторяется и в getAllSorted
    private void addContact(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            r.addContact(ContactType.valueOf(rs.getString("type")), value);
        }
    }

    //1  читаем из rs "content", который далее перегоним в секции
    //2  читаем из rs "type" - это енум
    //3  добавляем к объекту Resume мапу Section, передавая в сеттер-метод r.addSection  ключ-енум type и
    //   и объект класса Section, который распарсиваем в объект с пом. нашего JsonParser.read из строки в ячейке content в дб
    private void addSection(ResultSet rs, Resume r) throws SQLException {
        String content = rs.getString("content");//1
        if (content != null) {
            SectionType type = SectionType.valueOf(rs.getString("type"));//2
            r.addSection(type, JsonParser.read(content, Section.class));//3
        }
    }

    private void insertSections(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, content) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, Section> e : r.getSections().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                Section section = e.getValue();
                ps.setString(3, JsonParser.write(section, Section.class));//десериализовываем строку в объект Section
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteSections(Connection conn, Resume r) throws SQLException {
        deleteAttributes(conn, r, "DELETE  FROM section WHERE resume_uuid=?");
    }

    //удаляет секции из объкута Резюме
    //из одноко коннекта постоянно делаем разные препаредСтетемы
    private void deleteAttributes(Connection conn, Resume r, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getUuid());
            ps.execute();
        }
    }

    //всё содержимое метода перенесли из save.
    private void insertContacts(Connection conn, Resume r) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    //этот метод исп. при update- сначала удаляем контакты (потом записываем вновь пришедшие)
    private void deleteContacts(Connection conn, Resume r) throws SQLException {
        deleteAttributes(conn, r, "DELETE  FROM contact WHERE resume_uuid=?");
    }
}
    /** ЭТО НАЧАЛЬНАЯ РЕАЛИЗАЦИЯ SqlStorage. ДАЛЕЕ ВЫНЕСЛИ ПОВТОРЯЮЩИЙСЯ КОД В УТИЛЬНЫЕ МЕТОДЫ
     * ЗДЕСЬ ДАЛЕЕ ХОРОШО ДЕМОНСТРИРУЕТСЯ ПРИНЦИП ЗАПРОСОВ К ДБ
    //поле- ссылка типа наш интерфейс для подключения к дб
    //инициализируем её ссылкой на Connection(реализовав её абстракт)
    public final ConnectionFactory connectionFactory;
    */
    /**
    //конструктор- инициализирует ссылку типа интерфейс для подключения к дб
    //на вход принимает 3-и стринги: Url ДБ, User, Password:
    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        //ссылку типа интерфейс connectionFactory инициализируем реализацией абстракта с пом Анонимного класса
        //те присвоим ей реализацию служебного в Ява интерфейса Connection
        //[можно и сразу получить и использовать далее только  Connection ???]
        //[напр.:Connection myConnection = DriverManager.getConnection(dbUrl, dbUser, dbPassword) ???]
        connectionFactory = new ConnectionFactory() {//объект анонимного, имплементящего ConnectionFactory
            @Override//в Анонимном классе переопределяем абстракт
            public Connection getConnection() throws SQLException {
                //в этой реализации абстракта нам надо получить ссылку типа интерфейс Connection
                //с помощью статик метода getConnection() класса DriverManager:
                return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            }
        };
        //лямбдой:  connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword)
    }
     */
    /**
    //получим подключение к дб и отправим команду:
    @Override
    public void clear() {
        //в блоке try(), тк Connection и PreparedStatement надо закрывать
        try (Connection conn = connectionFactory.getConnection();//запускаем наш абстракт-получаем Connection
             //на Connection conn можем вызвать prepareStatement и передать запрос в дб
             PreparedStatement ps = conn.prepareStatement("DELETE FROM resume")) {
            //на PreparedStatement ps отошлем команду на выполнение запроса execute()
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
    @Override
    public Resume get(String uuid) {
        try (Connection conn = connectionFactory.getConnection();
             //в строке запроса r это альяс???
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r WHERE r.uuid =?")) {
            //вместо =? вставляем пришедший в аргумены uuid:  (это типа защита от sql инъекций)
            ps.setString(1, uuid);//1 значит 1-й вопрос =?

            //результат выполнения запроса присвоим объекту rs спец. класса ResultSet:
            //в postgres ResultSet закрывается сам вместе с PreparedStatement
            ResultSet rs = ps.executeQuery();

            //next() в классе ResultSet не только проверяет- есть ли еще след. запись (возвр true)
            //но и передвигает курсор, если такая запись есть:
            if (!rs.next()) {//если ResultSet rs будет пуст, след. такого резюме в дб нет и ексепшен соотвенный
                throw new NotExistStorageException(uuid);
            }
            //и возвращаем найденное Резюме(uuid которорого сейчас лежит в ResultSet rs):
            //rs.getString() прочтет поле "full_name" в табличке resume в нашей дб (по соотв. uuid)
            return new Resume(uuid, rs.getString("full_name"));
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
    @Override
    public void update(Resume r) {
    }
    @Override
    public void save(Resume r) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
    @Override
    public void delete(String uuid) {
    }
    @Override
    public List<Resume> getAllSorted() {
        return null;
    }
    @Override
    public int size() {
        return 0;
    }
}
     */
