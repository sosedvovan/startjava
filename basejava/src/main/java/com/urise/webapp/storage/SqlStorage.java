package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.sql.ConnectionFactory;
import com.urise.webapp.sql.SqlExecutor;
import com.urise.webapp.sql.SqlHelper;
import com.urise.webapp.sql.SqlTransaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//часть методов AbstractStorage не потребуется
//поэтому implements Storage
public class SqlStorage implements Storage{

    //поле с классом SqlHelper, в методе которого делаем preparedStatement.execute()
    //объект класса SqlHelper содержит в себе поле ConnectionFactory, абстракт которого возвращает Connection
    public final SqlHelper sqlHelper;

    //конструктор- создаем объект этого класса SqlStorage, передавая в аргументы логины-пароли.
    //объект этого класса будет содержать в себе объект класса SqlHelper, который мы инициализируем нужным нам подключением.
    //SqlHelper в этом конструкторе инициализируется реализацией connectionFactory, абстракт которого возвращает Connection.
    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(new ConnectionFactory() {
            @Override
            public Connection getConnection() throws SQLException {
                return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            }
        });
    }

    //в методе clear() вызывается первый перегруженный метод sqlHelper.execute()
    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume");
    }

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
    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("" +
                        "    SELECT * FROM resume r " +
                        " LEFT JOIN contact c " +
                        "        ON r.uuid = c.resume_uuid " +
                        "     WHERE r.uuid =? ",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }//если в ResultSet чтото есть - соберем объект Resume
                    Resume r = new Resume(uuid, rs.getString("full_name"));

                    do {
                        addContact(rs, r);
                    } while (rs.next());

//                    ПЕРЕНЕСЛИ ЭТОТ КОД В служебный addContact() - убрали повторяющийся код (цикл do-while оставили)
//                    do {//и добавим в этот объект Resume  (r.addContact) вытащив контакты из ResultSet
//                        String value = rs.getString("value");
//                        ContactType type = ContactType.valueOf(rs.getString("type"));
//                        r.addContact(type, value);
//                    } while (rs.next());//делаем до тех пор, пока есть записи в ResultSet

                    return r;
                });
    }

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
                insertContact(conn, r);
                return null;
            }
        });
    }

    //всё содержимое метода перенесли из save.
    private void insertContact(Connection conn, Resume r) throws SQLException {
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
    private void deleteContacts(Connection conn, Resume r) {
        sqlHelper.execute("DELETE  FROM contact WHERE resume_uuid=?", new SqlExecutor<Object>() {
            @Override
            public Object execute(PreparedStatement ps) throws SQLException {
                ps.setString(1, r.getUuid());
                ps.execute();
                return null;
            }
        });
    }

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
                   insertContact(conn, r);
                    return null;
                }
        );
    }

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

    @Override
    public List<Resume> getAllSorted() {
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
        return sqlHelper.execute("" +    //1
                "   SELECT * FROM resume r\n" +
                "LEFT JOIN contact c ON r.uuid = c.resume_uuid\n" +
                "ORDER BY full_name, uuid", ps -> {   //2
            ResultSet rs = ps.executeQuery();   //2
            Map<String, Resume> map = new LinkedHashMap<>();  //3
            while (rs.next()) {  //4
                String uuid = rs.getString("uuid");   //5
                Resume resume = map.get(uuid);  //6
                if (resume == null) { //7
                    resume = new Resume(uuid, rs.getString("full_name")); //8
                    map.put(uuid, resume);  //9
                }
                addContact(rs, resume); //11
            }
            return new ArrayList<>(map.values());
        });
    }

    //в этот метод, код переехал из get тк этот код повторяется и в getAllSorted
    private void addContact(ResultSet rs, Resume r) throws SQLException {
        String value = rs.getString("value");
        if (value != null) {
            r.addContact(ContactType.valueOf(rs.getString("type")), value);
        }
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT count(*) FROM resume", st -> {
            ResultSet rs = st.executeQuery();
            //next() в классе ResultSet не только проверяет- есть ли еще след. запись (возвр true)
            //но и передвигает курсор, если такая запись есть:
            return rs.next() ? rs.getInt(1) : 0;//getInt из ResultSet достанет одно первое число, если оно есть
        });
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
