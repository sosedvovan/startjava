package com.urise.webapp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.urise.webapp.model.Section;


import java.io.Reader;
import java.io.Writer;

public class JsonParser {
    //в поле-билдер Gson-парсера инициализируем
    private static Gson GSON = new GsonBuilder()
            //в билдере скажем, что класс Section будет сериализоваться через этот адаптер(тк Section абстрактный
            // и Json не может сам воссоздать объекты его наследников при десериализации- написали класс-адаптер в этой же папке)
            .registerTypeAdapter(Section.class, new JsonSectionAdapter())
            .create();


    public static <T> T read(Reader reader, Class<T> clazz) {//принимаем символьный Reader из текстового файла и Resume.class, объекты которого десеризует
        return GSON.fromJson(reader, clazz);
    }

    //сериализуем с учетом адаптера, кот объявлен в переменной GSON:
    public static <T> void write(T object, Writer writer) {//принимает Resume r и символьный поток Writer в техстовый файл, в котором сохраняем объект Резюме
        GSON.toJson(object, writer);
    }

}
