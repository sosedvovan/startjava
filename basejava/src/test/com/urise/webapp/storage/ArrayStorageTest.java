package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.AbstractArrayStorage;
import org.junit.Test;

import static org.junit.Assert.*;

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