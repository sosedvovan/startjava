package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class ArrayStorage {
    private Resume[] storage = new Resume[1000];
    private int size = 0;

    public void clear() {
        //моя реализация
        Arrays.fill(storage, 0);
        size = 0;
    }

    public void update(Resume r) {
        // ТОДО: проверка есть ли такое резюме в бд- чтобы оно было
    }

    public void save(Resume r) {
        //ТОДО: проверка есть ли такое резюме в бд- чтобы его небыло
    }

    //метод смотрит- есть ли в массиве storage Resume
    public Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid == storage[i].getUuid()) {//в массиве storage лежат объекты Resume -> они вызывают свой геттер
                return storage[i];
            }
        }
        return null;
    }

    //удалим элемент из массива и чтобы не сдвигать весь массив возьмем последний
    //элемент и переместим его на место удаленного, а на место последнего поместим null
    //и конечно уменьшаем вручную переменную size
    public void delete(String uuid) {
        // ТОДО: проверка есть ли такое резюме в бд- чтобы оно было
        for (int i = 0; i < size; i++) {
            if (uuid == storage[i].getUuid()) {
                storage[i] = storage[size - 1];
                storage[i] = null;
                size--;
            }
        }
    }

    //возвращает массив с размером с size надо реализовать
    public Resume[] getAll() {
        Resume[] result = new Resume[size];
        return result;
    }

    public int size() {
        return size;
    }
}


// Использование ArrayList для удаления элемента из обычного массива:
  /*   Если мы не можем удалить элемент в обычном массиве,
        мы можем преобразовать массив в структуру, позволяющую удалять элементы. А потом преобразовать эту структуру
        обратно в массив.
        Выполнить вышеописанную схему нам поможет java.util.List или ArrayList. Дело в том, что в ArrayList реализован
        специальный метод, позволяющий удалять элементы — remove. В общем виде всё выглядит так:
        String[] array = new String[]{"foo", "bar", "baz"};
        List<String> list = new ArrayList<>(Arrays.asList(array));
        list.remove("foo");
        array = list.toArray(new String[list.size()]);
        System.out.println(Arrays.toString(array));
        */
