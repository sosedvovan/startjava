package com.urise.webapp.storage.serializer;
//применяем паттерн стратегия для сериализаций. этот класс- одна из реализаций(вариаций сериализации) StreamSerializer
//делаем сериализацию с пом Json


import com.urise.webapp.model.Resume;
import com.urise.webapp.util.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class JsonStreamSerializer implements StreamSerializer {

    //СТОРАДЖИ ОБРАЩАЮТСЯ К ЭТИМ МЕТОДАМ, КОГДА СЕРИАЛИЗУЮТСЯ С ПОМ Json
    //ЭТИ МЕТОДЫ НАПРАВЛЯЮТ ЛОГИКУ В ПАРСЕР Json

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        //символьный поток- создаем Writer
        try (Writer writer = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            //и отдаем парсеру
            JsonParser.write(r, writer);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return JsonParser.read(reader, Resume.class);
        }
    }
}
