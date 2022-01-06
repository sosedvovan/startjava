package com.urise.webapp.sql;

import java.sql.Connection;
import java.sql.SQLException;
//интерфейс для подключения к дб
public interface ConnectionFactory {
    //абстракт, реализация которого подключает к дб(соотв., может быть много реализаций)
    //ничего не принимает, возвращает Connection
    Connection getConnection() throws SQLException;
}