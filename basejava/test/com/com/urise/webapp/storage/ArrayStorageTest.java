package com.com.urise.webapp.storage;


import com.com.urise.webapp.model.Resume;
import org.junit.Test;

public class ArrayStorageTest extends AbstractArrayStorage {

    @Test
    protected int getIndex(String uuid) {
        return 0;
    }

    @Test
    protected void fillDeletedElement(int index) {

    }

    @Test
    protected void insertElement(Resume r, int index) {

    }
}