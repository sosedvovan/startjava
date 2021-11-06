package com.urise.webapp.storage;


public class ArrayStorageTest extends AbstractStorageTest {

    //мы наследуемся от класса в кот нет дефолтного конструктора, след надо сделать конструктор
    public ArrayStorageTest() {
        super(new ArrayStorage());
    }


}