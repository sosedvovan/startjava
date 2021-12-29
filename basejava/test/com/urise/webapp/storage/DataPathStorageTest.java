package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.DataStreamSerializer;


public class DataPathStorageTest extends AbstractStorageTest  {
        //надо создать (public!!! для JUnit) конструктор тк он есть в родителе:
    //в конструкторе создается новый объект проверяемого класса и отправляется в super конструктор
        // и там происходит инициализация унаследованного поля этого класса - storage,
    //кот мы хотим тестировать

    public DataPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new DataStreamSerializer()));
    }
}


