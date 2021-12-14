package com.urise.webapp.util;

import java.time.LocalDate;
import java.time.Month;

//утильный класс, помогает создавать LocalDate в
//классе Organization
public class DateUtil {
    //задали 3000 год для какихто сравнений в будующем
    public static final LocalDate NOW = LocalDate.of(3000, 1, 1);

    //метод возвращает дату
    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }
}
