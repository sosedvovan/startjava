package com.urise.webapp;

import com.urise.webapp.model.SectionType;

public class TestSingleton {
    private static TestSingleton ourInstace;

    public static TestSingleton getInstance(){
        //не ленивый синглетон- инициализируем в конструкторе:
        if (ourInstace == null)
            ourInstace = new TestSingleton();

        return ourInstace;

        //в многопоточной среде в этот блок просочатся много потоков
        // и насоздают синглетонов, поэтому там по другому реализуют
    }

    private TestSingleton(){

    }

    public static void main(String[] args) {
        //ленивый синглетон- инициализируется на старте в поле:
        //--- private static TestSingleton ourInstace = new TestSingleton();  ---
       TestSingleton.getInstance().toString();
       //из Стринга получим одноименный синглетон с пом метода из енума:
        Singletone instance = Singletone.valueOf("INSTANCE");

        for (SectionType type : SectionType.values()){
            System.out.println(type.getTitle());
        }
    }

    //самый простой синглетон в Ява:
    public enum Singletone {
        INSTANCE
    }
}
