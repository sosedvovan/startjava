package com.urise.webapp;

import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.ArrayStorage;
import com.urise.webapp.storage.SortedArrayStorage;
import com.urise.webapp.storage.Storage;

import java.util.Arrays;

public class MainArray {
    //static тк обращаемся из static void main к этому объекту
    //те чтобы в статическом методе  использовать какие то члены класса
    //то они должны быть тоже статическими
    //final значит что мы не можем присвоить этому объекту в дальнейшем null, например
    //но внутри объекта можно поменять все что угодно
    //private тк это поле класса
    //называем большими буквами тк это private final static по конфенции
   private final static Storage ARRAY_STORAGE = new ArrayStorage();//тип Storage - это интерфейс(так принято обращаться к классам)
   // private final static Storage ARRAY_STORAGE = new SortedArrayStorage(); чтобы протестировать класс SortedArrayStorage.

    public static void main(String[] args) {
       final Resume r1 = new Resume();
        r1.setUuid("uuid1");
       final Resume r2 = new Resume();
        r1.setUuid("uuid2");
       final Resume r3 = new Resume();
        r1.setUuid("uuid3");

        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r2);

        System.out.println("Get r: " + ARRAY_STORAGE.get(r1.getUuid()));
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