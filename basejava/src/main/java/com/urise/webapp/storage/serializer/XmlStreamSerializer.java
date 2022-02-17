package com.urise.webapp.storage.serializer;

//Todo Сделать реализации Storage сохранения в файл через File и Path
// с возможностью выбора стратегии сериализации (посмотрите на паттерн стратегия)
// https://refactoring.guru/ru/design-patterns/strategy

import com.urise.webapp.model.*;
import com.urise.webapp.util.XmlParser;


import java.io.*;
import java.nio.charset.StandardCharsets;

//применяем паттерн стратегия для сериализаций. этот класс- одна из реализаций(вариаций сериализации) StreamSerializer
//делаем сериализацию с пом Xml
public class XmlStreamSerializer implements StreamSerializer {

    //СТОРАДЖИ ОБРАЩАЮТСЯ К ЭТИМ МЕТОДАМ, КОГДА СЕРИАЛИЗУЮТСЯ С ПОМ Xml
    //ЭТИ МЕТОДЫ НАПРАВЛЯЮТ ЛОГИКУ В ПАРСЕР Xml

    //в поле наш парсер
    private XmlParser xmlParser;

    //в конструкторе перечисляем классы, которые будем сериализовать
    public XmlStreamSerializer() {
        xmlParser = new XmlParser(
                Resume.class, Organization.class, Link.class,
                OrganizationSection.class, TextSection.class, ListSection.class, Organization.Position.class);
    }

    //далее делаем сериализацию-десериализацию
    //принимаем поток OutputStream(там разные преобразователи есть),
    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        //делаем объект врайтера с кодировочкой UTF_8(с Ява7)
        try (Writer w = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            //и отдаем парсеру на маршаризацию
            xmlParser.marshall(r, w);
        }
    }

    //и обратный метод
    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return xmlParser.unmarshall(r);
        }
    }
}
