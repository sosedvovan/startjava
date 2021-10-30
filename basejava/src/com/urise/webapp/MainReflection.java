package com.urise.webapp;



import com.urise.webapp.model.Resume;

import java.lang.reflect.Field;

//в классе Resume есть private поле, которое никто снаружи не видит, но мы с пом рефлексии увидим его и поменяем
//иногда этим пользуются для сериализации и чтением из файла
public class MainReflection {

    //запустим дебагер -> выделем объект r -> alt+f8(or Run -> Evaluate Excpression) -> getClass() ... or other
    public static void main(String[] args) throws IllegalAccessException {
        Resume r = new Resume();//используем конструктор без параметров - там сгенерится  значение

        //достанем первый элемент[0] из массива полей класса Resume через его объект r:
        Field field = r.getClass().getDeclaredFields()[0];//Field -класс библиотеки рефлекшн

        field.setAccessible(true);//без этого будет ошибка:
        //java.lang.IllegalAccessException: class com.urise.webapp.MainReflection cannot access a member of class
        // com.urise.webapp.model.Resume with modifiers "private final"
        //те мы через рефлексию сделали поле класса Resume неприватным чтобы здесь получить к нему доступ)))

        //выведем имя первого(и пока единственного) поля класса Resume:
        System.out.println("наименование поля:  " + field.getName());

        //Выведем значение(сгенерированное) поля uuid объекта r класса Resume еще таким способом:
        System.out.println("сгенерированное в конструкторе значен поля:  " + field.get(r));//выбросим обязательный чекед ексепшн в сигнатуру класса

        //поменяем значение поля uuid объекта r класса Resume через рефлекшн сеттер:
        //своего сеттера у класса Resume НЕТ
        field.set(r, "new_uuid");

        System.out.println("заданное в рефлекшн сеттере значен поля:  " + r);//у r вызывается toString()
    }
}
//чекед эксепшены можно оборачивать в рантайм эксепшены или в другие

//так же через рефлекшн можно брать анотоции у объекта, ту-стринг и др методы класса

/**
 * Руководство по аннотациям в Java:
 * https://topjava.ru/blog/rukovodstvo-po-annotatsiyam-v-java-i-mekhanizmu-ikh-raboty
 *
 * Reflection для начинающих, видео от BoostBrain:
 *https://www.youtube.com/watch?v=XJQuBXWADZg
 *
 * Руководство по Java Reflection API:
 * https://javadevblog.com/polnoe-rukovodstvo-po-java-reflection-api-refleksiya-na-primerah.html
 *
 * Алишев- Продвинутая Java.
 */