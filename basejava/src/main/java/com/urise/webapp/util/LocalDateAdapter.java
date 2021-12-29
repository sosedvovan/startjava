package com.urise.webapp.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;


import java.time.LocalDate;

//Для того чтобы Джакарта могла сериализовать объекты, в полях которых
//присутствует LocalDate, сделаем этот адаптор(кастомизируемся)
//здесь будем переводить LocalDate в строку для Маршалинга и обратно для Анмаршалинга
//те над полями в Организайшен надо сделать анотацию:
//@XmlJavaTypeAdapter(LocalDateAdapter.class)

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    @Override
    public LocalDate unmarshal(String str) throws Exception {
        return LocalDate.parse(str);
    }

    @Override
    public String marshal(LocalDate ld) throws Exception {
        return ld.toString();
    }
}

//CTRL+E - быстрое перемещение по классам проекта
