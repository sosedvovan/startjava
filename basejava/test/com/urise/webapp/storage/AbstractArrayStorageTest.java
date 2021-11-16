package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

//Todo: метод saveOverflow() должен исполняться только для массивов:

// создали AbstractArrayStorageTest скопировав все из AbstractStorageTest и наследуемся от него
// SortedArrayStorageTest и ArrayStorageTest теперь будет наследоваться от него
//оставим здесь только метод saveOverflow() и конструктор на супер направим
// и из AbstractStorageTest удалим метод saveOverflow() тк он для Map и List не валиден

//получилось, что только реализации хранилища на основе обычного массива ArrayStorage и SortedArrayStorage
//будут отсюда наследоваться и следовательно только в них будет находится тест saveOverflow() на переполнение,
//тк в коллекциях нет переполнения- они динамически расширяются


//перед его методом

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    protected AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)//проверка переполнения нашего массива storage
    public void saveOverflow() throws Exception { //проверка нашего собственого ексепшена
        try {
            for (int i = 4; i <= AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume("Name" + i));//заполним весь массив
            }
        } catch (StorageException e) {
            Assert.fail();
        }
        storage.save(new Resume("Overflow"));//пробуем после заполнения массива записать туда еще один объект
        //на этой крайней строчке должен выбросится StorageException -тогда тест пройден
        //но если StorageException выбросится в цикле for, тогда тест завалится-> возьмем в try-catch
    }
}

