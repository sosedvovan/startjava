package com.urise.webapp.storage;


public class SortedArrayStorageTest extends AbstractStorageTest {

    //мы наследуемся от класса в кот нет дефолтного конструктора,а есть наш -> след надо сделать конструктор
    //тк перед выполнением тестов создается объект этого класса
    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }
}