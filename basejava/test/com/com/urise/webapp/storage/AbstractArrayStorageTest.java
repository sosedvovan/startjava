package com.com.urise.webapp.storage;

import com.com.urise.webapp.exception.NotExistStorageException;
import com.com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractArrayStorageTest {
    /**
     * action в каждом тестовом методе создаем сценарий действия (можно в @Before)
     * те создаем необходимые объекты, процесс создания которых надо проверить, или надо
     * этити объекты использовать для вызова проверяемых методов. Так же здесь инициализируем
     * эти объекты нужными нам значениями, инициализируем (заполняем) нужные нам массивы.
     * Так же создаем подключения к БД и Серверу для проверки и др.
     *
     * assertion - сопоставление ожидания и полученного результата (с пом assertEquals() и др.)
     */

    //для теста заполним storage тремя резюме:
    /**
     * чтобы класс заработал сампосебе допиши сюда new ArrayStorage и убери abstract
      */

    private Storage storage;//указатель на какой-то Storage (SortedArrayStorage или dArrayStorage)
    //здесь мы его не инициализируем тк этот абстракт класс исспользуем в дочках
    private static final String UUID_1 = "uuid1"; //созд переменные String
    private static final String UUID_2 = "uuid2"; //для @Before
    private static final String UUID_3 = "uuid3"; //static- тк они одинак в каждом тест-методе

    @Before//этот метод вызывается перед каждым тестовым методом
    public void detUp() throws Exception {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }
    //перед запуском каждого тестового метода будет запускаться метод @Before
    //и очищаться и инициализироваться storage (в нашем случае)
    //Вместо @Before можно @BeforeClass(тогда он и поля класса должны быть static)
    //при запуске каждого тестового метода будет создаваться объект этого класса
    //и на этом объекте будет запускаться тело тестового метода
    //в названиях тестовых методов принято исп слово Should - (ожидается).
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