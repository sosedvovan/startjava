package com.urise.webapp.sql;

import com.urise.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//класс сделан универсальным под любые sql запросы с пом стратегии чз интерфейс SqlExecutor<T>
//объект этого класса будет содержать в себе объект Connection (подключение к дб).
public class SqlHelper {
    //поле с ссылкой типа интерфейс. чтобы методы этого класса получил доступ
    // к ConnectionFactory- открытие подключения к дб
    private final ConnectionFactory connectionFactory;

    //констуктор, инициализируем ссылкой типа интерфейс реализацией его абстакта
    //в реализации создаем подключение к дб
    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * Далее два перегруженных метода
     * В СЛУЧАЕ, КОГДА ВЫЗЫВАЕМ execute() с одним аргументом
     * 1-й принимает строку с запросом и запускает 2-й, передавая ему в аргументы этот запрос
     * и  еще наш абстракт в котором preparedStatement.execute() и возвращает Boolean
     * далее 2-й метод откроет соединение с дб, на этом соединении откроет PreparedStatement ps,
     * передавая ему в конструктор стрингу с запросом и запустит абстракт, реализованный
     * в 1-ом методе передав ему этот PreparedStatement ps для preparedStatement.execute().
     * И вернется Boolean - как результат запроса(получилось-не получилось)
     *
     * В СЛУЧАЕ, КОГДА ВЫЗЫВАЕМ execute() сразу с двумя аргументами, те 2-й,
     * ему в аргументы передаем Стрингу с запросом и реализацию абстракта SqlExecutor<...>
     * параметризируя его типом, ожидаемым на возврате от этого метода
     * напр get() надо параметризировать <Resume> тк он возвращает объект Resume
     */
    //принимает строку с запросом и запускает следующий метод:
    public void execute(String sql) {
        //вызываем след. метод передавая ему эту строку и реализацию нашего интерфейса SqlExecutor<T>
        execute(sql, new SqlExecutor<Boolean>() {
            @Override
            public Boolean execute(PreparedStatement preparedStatement) throws SQLException {
                return preparedStatement.execute();//!!!!!!в этом месте запрос еще не будет выполнен(тк это вызов следующего метода),
                                                    // а этот кусок кода делегируется в следующий метод
            }
        });
    }

    //принимает стрингу с запросом и кусок кода для метода-абстракта
    public <T> T execute(String sql, SqlExecutor<T> executor) {
        //открываем соединение с дб - Connection conn
        //открываем PreparedStatement ps и передаем ему стрингу с запросом:
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {//sql пока не доделан- содержит=?, в абстракте доделывается
            //вместо команды : ps.execute(); - запускаем кусок кода из пришедшей реализации абстракта:
            //запускаем абстракт (кусок кода кот сюда делегировали) на пришедшей в аргументы ссылке типа наш интерфейс
            //передавая в данную реализацию абстракта PreparedStatement ps внутри которого стринга с запросом.
            //в данной реализации прописанно: preparedStatement.execute()   (execute()- возвращает Boolean)
            return executor.execute(ps);//сюда приходят разные куски кода-разные реализации SqlExecutor<T>
            //ТО, ЗАПУСКАЯ АБСТРАКТ В ТЕЛЕ МЕТОДА, МЫ ЗАПУСКАЕМ ОПРЕДЕЛЕННУЮ РЕАЛИЗАЦИЮ КУСКА КОДА ПРИШЕДШЕГО В АРГУМЕНТЫ
            //НА ССЫЛКУ ТИПА ИНТЕРФЕЙС (SqlExecutor<T> executor в данном случае)
        } catch (SQLException e) {
            //в случая возврата из постриджа его ошибки
            //конвертируем ее в свой ексепшен и выбрасываем:
            throw ExceptionUtil.convertException(e);
        }
    }

    //метод или коммитит трансакцию или роллбечит ее:
    //принимаем SqlTransaction<T> - кусок кода:
    public <T> T transactionalExecute(SqlTransaction<T> executor) {
        //из connectionFactory берем Connection:
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                //setAutoCommit(true) означает, что после каждого execute - коммитимся, а у нас тут false
                //те отключаем автокомичинье, тк хотим сделать несколько операций execute в одном Connection:
                conn.setAutoCommit(false);
                //далее выполняем пришедший кусок кода(стратегия)- те операции с Connection(там будет несколько execute)
                T res = executor.execute(conn);
                //и коммитим сами этот Connection:
                conn.commit();
                return res;
            } catch (SQLException e) {
                //если в дб случается SQLException при нескольких операций execute, то откатываем:
                conn.rollback();
                //и выбрасываем наше сконвертируемое исключение:
                throw ExceptionUtil.convertException(e);
            }
        } catch (SQLException e) {
            //этот эксепшен для rollback(), если в нем что-то не так случится:
            throw new StorageException(e);
        }
    }

}