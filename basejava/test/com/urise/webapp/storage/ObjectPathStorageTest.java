package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.ObjectStreamSerializer;

//тк мы создали класс ListStorage- то создадим и тест:
public class ObjectPathStorageTest extends AbstractStorageTest  {
        //надо создать (public!!! для JUnit) конструктор тк он есть в родителе:
    //в конструкторе создается новый объект проверяемого класса и отправляется в super конструктор
        // и там происходит инициализация унаследованного поля этого класса - storage,
    //кот мы хотим тестировать

    public ObjectPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new ObjectStreamSerializer()));
    }
}

//тест на SaveOverFlow не будет пройден тк этот тест только для Аррей
//надо сделать так, чтобы он был только для Аррей
