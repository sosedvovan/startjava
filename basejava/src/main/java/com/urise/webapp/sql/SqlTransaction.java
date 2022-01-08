package com.urise.webapp.sql;

import java.sql.Connection;
import java.sql.SQLException;
//для трансакций
//из Connection берем какую-то операцию препейтментстетем и говорим : коммит или роллбек
//исп. для стратегии- передачи куска кода в др. метод
public interface SqlTransaction<T> {
    T execute(Connection conn) throws SQLException;
}
