package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

/** <p>
 * это КОНТРАКТ для классов реализующих этот интерфейс.
 *
 * создадим интерфейс тк принято обращаться к классам через интерфейс.
 *
 * [ в процессе мы из класса ArrayStorage перенесли сюда сигнатуры всех методов]
 * в интерфейс нельзя поместить - private, final
 * можно перенести abstract методы, но их надо реализовывать,
 * можно static но их нельзя оверридить, и обращаемся к таким методам по имени интерфейса
 * и можно default - как обычные - и обращаемся по имени интерфейса?
 *
 * и при создании объекта класса(ArrayStorage), кот имплементирует этот интерфейс в качестве типа-
 * в тестовом классе MainArray будем указывать этот интерфейс Storage как тип, например:
 * private final static Storage ARRAY_STORAGE = new ArrayStorage();
 * [ArrayStorage далее сделали абстрактным и его екстендит новый ArrayStorage]
 * Класс ArrayStorage имплеминтирует этот интерфейс через своего родителя- AbstractArrayStorage,
 *
 </p>*/


public interface Storage {

    // следующие методы - abstract по умолчанию, их надо реализовывать.
    //это все те методы, которые должны уметь делать) разные реализации хранилищей
    void clear();
    void update(Resume r);
    void save(Resume r);
    Resume get(String uuid);
    void delete(String uuid);
    Resume[] getAll();
    int size();
}
