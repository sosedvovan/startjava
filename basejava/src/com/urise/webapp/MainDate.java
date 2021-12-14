package com.urise.webapp;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainDate {

    public static void main(String[] args) {

        //работать с long эффективнее чем с Date в ДБ но это не точно
        long start = System.currentTimeMillis();
        Date date =new Date();//в текущей таймзоне
        System.out.println(date);
        //прием- измеряем кол-во милисекунд от первого start до след. sout:
        System.out.println(System.currentTimeMillis() - start);

        Calendar cal = Calendar.getInstance();//получили григорианский календарь
        //это если надо работать с датами до 1970

        //для примера получим календарь для американской таймзоны
        cal.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        //и если у такого календаря взять дату, то она всеравно будет в
        //нашей текущей таймзоне:
        Date dateCal =  cal.getTime();//возвратит дату
        System.out.println(dateCal);

        //класс SimpleDateFormat используют для преобразования таймзон
        //см в рекоменд. материалах к этой теме

        //недостаток вышеописанной Даты- она изменяема.
        //те полученную дату из систеы можно изменить с пом сеттера
        //поэтому сейчас исспользуют другие классы и библиотеки
        //напр доп библиотека joda

        //в java8 ввели DateAPI- там исп  LocalDate, LocalTime
        //отличие LocalDate от Date - в LocalDate нет таимзоны
        //те всегда получаем относительные значения, не зависящие от таймзоны(абсолютная)
        //и это проще и удобно + реализует Комарабле
        //В нашем Резюме будем использовать LocalDate без времени

        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();
        LocalDateTime ldt = LocalDateTime.of(ld, lt);//или сказать now() можно
        System.out.println(ldt);

        //так же можно форматировать вывод даты как нам удобно:
        SimpleDateFormat sdf = new SimpleDateFormat("YY/MM/dd");
        System.out.println(sdf.format(date));//для старого класса Date

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YY/MM/dd");
        System.out.println(dtf.format(ldt));//для нового DateIPI







    }
}
