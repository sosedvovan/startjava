package com.urise.webapp.sql;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.StorageException;
import org.postgresql.util.PSQLException;


import java.sql.SQLException;

public class ExceptionUtil {
    //конструктор
    private ExceptionUtil() {
    }

    //метод принимает SQLException и конвертирует по коду постридж-ошибки в наши ексепшены
    public static StorageException convertException(SQLException e) {
        if (e instanceof PSQLException) {

//            http://www.postgresql.org/docs/9.3/static/errcodes-appendix.html
            //если попытаемся вставить в таблицу перичный ключ, который уже есть
            //то бросим ExistStorageException
            if (e.getSQLState().equals("23505")) {
                return new ExistStorageException(null);
            }
        }
        //если из постриджа придет другой код ошибки (не "23505"),
        // то бросаем просто StorageException
        return new StorageException(e);
    }
}