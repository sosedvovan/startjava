package com.urise.webapp.storage;


import com.urise.webapp.model.Resume;
import org.junit.Test;

public class ArrayStorageTest extends AbstractArrayStorageTest {

    //мы наследуемся от класса в кот нет дефолтного конструктора, след надо сделать конструктор
    public ArrayStorageTest() {
        super(new ArrayStorage());
    }


}