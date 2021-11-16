package com.urise.webapp.storage;

//при запуске теста Java первым делом САМА создаст объект этого класса используя конструктор(дефолтный или рукописный),
// и на этом объекте будут запускаться тесты
public class ArrayStorageTest extends AbstractArrayStorageTest {

    //мы наследуемся от класса в кот нет дефолтного конструктора, след надо сделать конструктор
    public ArrayStorageTest() {
        super(new ArrayStorage());
        //этот объект будет создаваться в конструкторе super класса-
        // там его поле -----  private Storage storage; ------, которое он унаследовал,
        //будет проинициализированно объектом проверяемого класса --- new ArrayStorage()---
    }
}

//как создается обект этого класса:
//помним, что этот клас наследует не только методы, но и поля супер класса (в тч и статические(у нас они final))
//следовательно при создании объекта этого класса надо проинициализировать поле
// -----  private Storage storage; ------
//и мы видим, что это поле инициализируется объектом класса, кот имплементит интерфейс Storage
//следовательно мы в конструкторе обратимся к super и передадим в него ---- new ArrayStorage() ----

//тк мы хотим чтобы тестовые методы вызывались на объекте этого класса- ArrayStorageTest
//который внутри себя(в поле) содержит объект класса ArrayStorage к полям и методам которого
//будут применяться (наследуемые) тестовые методы этого класса.

//те в телах тестовых методов прописываем логику-команду action-сценари (напр.: storage.clear();//почистили)
//и assertion - сопоставление ожидания и полученного результата (напр.: assertSize(0);//убедились, что size стал 0)
