package com.urise.webapp.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
//реализуем паттерн стратегия
//типизировали, чтобы мог возвращать в том числе и Резюме
public interface SqlExecutor<T> {//Sql-выполнитель запросов.
    //принимает PreparedStatement, возвращает то, чем типизирован
    T execute(PreparedStatement st) throws SQLException;
    //PreparedStatement st- это объект содержащий в себе стрингу с запросом.на нем вызываем execute
    //PreparedStatement st создается на объекте Connection
}
/**
 * где-то в SqlHelper мы создаем этот PreparedStatement st, передаем в этот абстракт(в нем
 * исполняется какое-то действие-стратегия) и потом делается команда execute()
 */