package com.urise.webapp.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.Reader;
import java.io.Writer;

public class XmlParser {
    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    public XmlParser(Class... classesToBeBound) {
        try {
            //создаем контекст по классам, которые будем сериализовать
            JAXBContext ctx = JAXBContext.newInstance(classesToBeBound);

            //создаем marshaller и unmarshaller и задаем проперти - кодировку и что красиво форматируем,
            //не в одну строчку, а с отступами
            marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

            unmarshaller = ctx.createUnmarshaller();
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    //и 2-а метода типизированных(а сам класс не типизирован). и ридер и врайтеры на входах для символьного потока
    public <T> T unmarshall(Reader reader) {
        try {
            return (T) unmarshaller.unmarshal(reader);//reader-символьный поток- откуда будем считывать
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    public void marshall(Object instance, Writer writer) {
        try {
            marshaller.marshal(instance, writer);//instance- куда мы будем выводить наш объект
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }
}
