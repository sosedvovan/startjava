package com.com.urise.webapp.storage;

import com.com.urise.webapp.model.Resume;


//КОНТРАКТ:
public interface Storage { //создадим интерфейс тк принято обращаться к классам через интерфейс. из класса ArrayStorage
    // перенесем сюда сигнатуры всех методов, (нельзя- private, final)
    // (можно abstract-надо реализовывать, static-(нельзя оверридить, а обращаемся по имени интерфейса) и default-как обычные)

    // и при создании объекта класса(ArrayStorage), кот имплементирует этот интерфейс в качестве типа-  в тестовом классе MainArray
    // будем указывать интерфейс (private final static Storage ARRAY_STORAGE = new ArrayStorage();

    void clear();
    void update(Resume r);
    void save(Resume r);
    Resume get(String uuid);
    void delete(String uuid);
    Resume[] getAll();
    int size();
}
