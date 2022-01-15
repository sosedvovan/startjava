package com.urise.webapp.sql;

import java.sql.Connection;
import java.sql.SQLException;

//интерфейс для подключения к дб. его абстракты возвращают Connection.

public interface ConnectionFactory {

    Connection getConnection() throws SQLException;

}