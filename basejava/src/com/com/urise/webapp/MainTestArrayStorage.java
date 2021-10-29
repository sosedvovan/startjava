package com.com.urise.webapp;


import com.com.urise.webapp.model.Resume;
import com.com.urise.webapp.storage.ArrayStorage;
import com.com.urise.webapp.storage.Storage;

public class MainTestArrayStorage {
    //static тк обращаемся из static void main к этому объекту
    //те чтобы в статическом методе  использовать какие то члены класса
    //то они должны быть тоже статическими
    //final значит что мы не можем присвоить этому объекту в дальнейшем null, например
    //но внутри объекта можно поменять все что угодно
    //private тк это поле класса
    //называем большими буквами тк это private final static по конфенции
    //тип ссылки - Storage тк это интерфейс кот имплементирует ArrayStorage- так принято- обращаться к классам через интерфейс
   private final static Storage ARRAY_STORAGE = new ArrayStorage();//тип Storage - это интерфейс(так принято обращаться к классам)

   // private final static Storage ARRAY_STORAGE = new SortedArrayStorage(); чтобы протестировать класс SortedArrayStorage.

    public static void main(String[] args) {
       final Resume r1 = new Resume("uuid1");
        //r1.setUuid("uuid1");// поле сделали final -> пользуем конструктор. сеттер теперь нельзя
       final Resume r2 = new Resume("uuid2");
        //r2.setUuid("uuid2");// поле сделали final -> пользуем конструктор. сеттер теперь нельзя
       final Resume r3 = new Resume("uuid3");
        //r3.setUuid("uuid3");// поле сделали final -> пользуем конструктор. сеттер теперь нельзя

        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r3);

        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size());

        System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));

            //error: тк отсюда не видно protected Resume[] storage из сlass AbstractArrayStorage
        // и в private final static Storage ARRAY_STORAGE -- интерфейс Storage надо исправить на класс ArrayStorage.
        //System.out.println("Index of r3: " + Arrays.binarySearch(ARRAY_STORAGE.storage, 0, ARRAY_STORAGE.size(), r3));

        printAll();
        ARRAY_STORAGE.delete(r1.getUuid());
        printAll();
        ARRAY_STORAGE.clear();
        printAll();

        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAll()) {
            System.out.println(r);
        }
    }
}
