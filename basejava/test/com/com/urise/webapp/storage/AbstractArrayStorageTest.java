package com.com.urise.webapp.storage;

import com.com.urise.webapp.exception.NotExistStorageException;
import com.com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractArrayStorageTest {

    //для теста заполним storage тремя резюме:
    private Storage storage;//чтобы класс заработал сампосебе работал допиши сюда new ArrayStorage
    private static final String UUID_1 = "uuid1";       //и убери abstract
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    @Before
    public void detUp() throws Exception {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void clear() throws Exception{
        // throw new IllegalAccessException();
    }

    @Test
    public void update() throws Exception{
    }

    @Test
    public void getAll() throws Exception{
    }

    @Test
    public void save() throws Exception{
    }

    @Test
    public void delete() throws Exception{
    }

    @Test
    public void size() throws Exception{
        //проверим этот метод. 3- это сколько ожидается.
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void get() throws Exception{
    }

    //дописали руками- тест на NotExist:
    @Test(expected = NotExistStorageException.class)//тесту сказали что ожитаем этот эксепшен
    public void getNotExist() throws Exception{
        storage.get("dummi");//и попробуем найти несуществующее значение.
    }
}