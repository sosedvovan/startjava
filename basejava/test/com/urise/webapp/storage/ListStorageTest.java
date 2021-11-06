package com.urise.webapp.storage;

//тк мы создали класс ListStorage- то создадим и тест:
public class ListStorageTest extends AbstractStorageTest {
        //надо создать (public!!! для JUnit) конструктор тк он есть в родителе:
    //в конструкторе создается новый объект класса,
    //кот мы хотим тестировать
    public ListStorageTest() {
        super(new ListStorage());
    }
}

//тест на SaveOverFlow не будет пройден тк этот тест только для Аррей
//надо сделать так, чтобы он был только для Аррей
