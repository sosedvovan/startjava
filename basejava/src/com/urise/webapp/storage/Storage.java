package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public interface Storage { //создадим интерфейс тк принято обращаться к классам через интерфейс. из класса ArrayStorage
                        // перенесем сюда сигнатуры всех методов, кроме private
    // и при создании объекта класса(ArrayStorage), кот имплементирует этот интерфейс в качестве типа- будем указывать
    // интерфейс (private final static Storage ARRAY_STORAGE = new ArrayStorage();)
    void clear();
    void update(Resume r);
    void save(Resume r);
    Resume get(String uuid);
    void delete(String uuid);
    Resume[] getAll();
    int size();
}
