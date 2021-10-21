package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public class ArrayStorage extends AbstractArrayStorage {//implements Storage тк AbstractArrayStorage implements Storage


    public void clear() {
        //метод класса Arrays fill заполнит значениями часть массива (от 0 до size)
        //где size инклюдев = включается(это можно уточнить в реализации метода)
        //есть реализация без указания интервала (от 0 до size)
        Arrays.fill(storage, 0, size, null);
        size = 0;

        //for(int i = 0; i < size; i++){ //один из вариантов очистки массива
        //    storage[i] = null;
       // }
    }

    public void update(Resume r) {//String newUuid - новое значение для Resume r
        // проверка есть ли такое резюме в бд- чтобы оно было
        int index = getIndex(r.getUuid());//getIndex вернет индекс по значению
        if (index == -1){
            System.out.println("Resume: " + r.getUuid() + " not exist");
        }else {
            storage[index] = r;
        }

        //вариант моей реализации:
       // for (int i = 0; i < size; i++) {//чтобы обновить ячейку c Resume сначала найдем ее в цикле for
       //     if (r.getUuid().equals(storage[i].getUuid())) {//в массиве storage лежат объекты Resume -> они вызывают свой геттер
      //          r.setUuid(newUuid); //String newUuid в арг метода должно быть
      //      } //чтобы использовать equals надо его значала переопределить в Resume
      //  }

    }

    public void save(Resume r) {
        //проверка есть ли такое резюме в бд- надо чтобы его небыло:
        if (getIndex(r.getUuid()) != -1) {//если элемент с таким значением уже есть, то выведем сообщение:
            System.out.println("Resume: " + r.getUuid() + " alredy exist");
        }else if (size == STORAGE_LIMIT) {//проверка-что в массиве еще есть свободные ячейки
            System.out.println("Storage overflow");
        } else {// если элемента с таким знаечением в массиве нет и в массиве есть свободные ячейки:
            storage[size] = r;
            size++;
        }

        //вариант моей реализации:
       // for (int i = 0; i < size; i++) {//чтобы обновить ячейку c Resume сначала найдем ее в цикле for
       //     if (!r.getUuid().equals(storage[i].getUuid())) {//в массиве storage лежат объекты Resume -> они вызывают свой геттер
        //        storage[size] = r;
        //        size++;
        //    }
       // }
    }





    //удалим элемент из массива и чтобы не сдвигать весь массив возьмем последний
    //элемент и переместим его на место удаленного, а на место последнего поместим null
    //и конечно уменьшаем вручную переменную size
    public void delete(String uuid) {
        // сначала проверка есть ли такое резюме в бд- чтобы оно было:
        int index = getIndex(uuid);//если наш метод getIndex() вернет -1, то такого объекта в массиве нет
        if (index == -1) {
            System.out.println("Resume: " + uuid + " not exist");
        }else {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        }
        //вариант моей реализации:
        //for (int i = 0; i < size; i++) {
        //    if (uuid.equals(storage[i].getUuid())) {
        //        storage[i] = storage[size - 1];
         //       storage[size - 1] = null;
         //       size--;
        //    }
      //  }
    }

    //клонирует наш массив storage и возвращает новый массив result с размером size
    public Resume[] getAll() {

        //метод Arrays.copyOfRange копирует интервал от 0 индекса до size
        return Arrays.copyOfRange(storage, 0, size);

       // Resume[] result = new Resume[size]; //один из вариантов клонирования массива
       // for(int i = 0; i < size; i++){
       //     result[i] = storage[i];
      //  }
      //  return result;
    }



    //реализовали  метод родительского абстрактного класса (тоже protected, как сигнатура в родительском. но
    // можно расширить до public, а сузить нельзя)
    // для избегания повторения кода. делает методом перебора(в сортед-варианте-методом деления на 2)
    //возвращает индекс элемента в массиве по значению этого элемента
    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++){
            if (uuid.equals(storage[i].getUuid()));
            return i;
        }
        return -1; // -1 возвращается если элемент с таким значением отсутствует
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
