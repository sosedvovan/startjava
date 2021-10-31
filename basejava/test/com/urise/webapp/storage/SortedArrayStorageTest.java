package com.urise.webapp.storage;


import org.junit.Test;

public class SortedArrayStorageTest extends AbstractArrayStorageTest {

    //мы наследуемся от класса в кот нет дефолтного конструктора, след надо сделать конструктор
    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }
}