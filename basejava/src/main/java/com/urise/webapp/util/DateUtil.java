package com.urise.webapp.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

//утильный класс, помогает создавать LocalDate в
//классе Organization
public class DateUtil {

    //задали в костанте NOW - 3000 год для каких-то сравнений в будующем
    public static final LocalDate NOW = LocalDate.of(3000, 1, 1);

    //переменная содержит паттерн форматирования даты в виде месяц-год
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");

    //метод возвращает дату, принимая год и месяц (день всегда здесь 1)
    public static LocalDate of(int year, Month month) {

        return LocalDate.of(year, month, 1);
    }


    public static String format(LocalDate date) {
        if (date == null) return "";
        return date.equals(NOW) ? "Сейчас" : date.format(DATE_FORMATTER);
    }

    public static LocalDate parse(String date) {
        //защита от спешел-кейсов- если date пустая или "Сейчас", то возвращаем NOW:
        if (HtmlUtil.isEmpty(date) || "Сейчас".equals(date)) return NOW;
        //YearMonth поможет убрать числа из DATE_FORMATTER тк мы их не используем
        YearMonth yearMonth = YearMonth.parse(date, DATE_FORMATTER);
        //возвращаем LocalDate, тк postges c YearMonth не умеет работать
        return LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
    }
}
