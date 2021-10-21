package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage{//абстрактные классы могут не имплементировать методы интерфейса

    //все поля и методы этого абстрактного класса наследуются дочерними наследниками) этого класса

    protected static final int STORAGE_LIMIT = 100000;//protected = public + видны в наследниках
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int size() {
        return size;
    }

    //метод смотрит- есть ли в массиве storage объект Resume с пришедшим в аргументы значением
    //мы создали его в этом абстрактном классе, кот является родительским для SortedArrayStorage и ArrayStorage
    //мы в этот метод вынесли общий механизм для классов SortedArrayStorage и ArrayStorage
    //а в этом методе используется другой-внутренний метод: getIndex()  -> ниже мы его реализуем-сделаем его
    //абстрактным чтобы в классах SortedArrayStorage и ArrayStorage реализовать(переопределить) его по разному
    //это называется ШАБЛОННЫМ МЕТОДОМ - мы переопределим некоторые шаги алгоритма(те мы будем находить индекс
    //в переопределенных getIndex() по разному - перебором и делением на 2)
    public Resume get(String uuid) {

        int index = getIndex(uuid);//находит индекс по значению. если наш метод getIndex() вернет -1, то такого объекта в массиве нет
        if (index != -1) {
            System.out.println("Resume: " + uuid + " not exist");
            return null;
        }
        return storage[index];

        //вариант моей реализации обхода массива:
        // for (int i = 0; i < size; i++) {
        //    if (uuid.equals(storage[i].getUuid())) {//в массиве storage лежат объекты Resume -> они вызывают свой геттер
        //        return storage[i];
        //    }
        // }
        // return null;
    }

    protected abstract int getIndex(String uuid);
}



