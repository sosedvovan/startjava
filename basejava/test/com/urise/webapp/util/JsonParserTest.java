package com.urise.webapp.util;


import com.urise.webapp.model.Resume;
import com.urise.webapp.model.Section;
import com.urise.webapp.model.TextSection;
import org.junit.Assert;
import org.junit.Test;

import static com.urise.webapp.TestData.R1;
//протестим JsonParser наши методы кот. исп. для sqlStorage, когда с пом. JsonParser преобразуем строковое значение
//из ячейки таблицы в объекты класса Section для мапы, которую добавляем к объекту Resume
public class JsonParserTest {
    @Test
    public void testResume() throws Exception {
        String json = JsonParser.write(R1);//из объекта R1 сделали строку Json
        System.out.println(json);
        Resume resume = JsonParser.read(json, Resume.class);//из строкт Json сделали объект Resume
        Assert.assertEquals(R1, resume);//сравнили первоначальный объект с распарсенным
    }

    //тест для TextSection
    @Test
    public void write() throws Exception {
        Section section1 = new TextSection("Objective1");//есть у нас какаято TextSection
        String json = JsonParser.write(section1, Section.class);//перегоним ее в стринг (будем серилизовать вместе с классом, тк Json не хочет учитывать наш класс- адаптер)
        System.out.println(json);
        Section section2 = JsonParser.read(json, Section.class);//востановим из стринги объект
        Assert.assertEquals(section1, section2);//сравним первоначальный объект с полученным
    }
}