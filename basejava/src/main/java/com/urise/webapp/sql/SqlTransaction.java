package com.urise.webapp.sql;

import java.sql.Connection;
import java.sql.SQLException;
//для трансакций
//из Connection берем какую-то операцию препейтментстетем и говорим : коммит или роллбек
public interface SqlTransaction<T> {
    T execute(Connection conn) throws SQLException;
}
