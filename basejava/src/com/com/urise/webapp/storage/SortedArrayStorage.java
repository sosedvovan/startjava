package com.com.urise.webapp.storage;



import com.com.urise.webapp.model.Resume;

import java.util.Arrays;

//на основе сортированного массива, чтобы большое О стало не линейным(перебор всего массива)
//а большое О стало от логарифма с основанием 2(бинарный поиск в массиве методом деления на 2)
//этот класс implements Storage тк его родительский AbstractArrayStorage implements Storage

public class SortedArrayStorage extends AbstractArrayStorage{


    //ЭТОТ МЕТОД- ЧАСТЬ ЛОГИКИ РЕАЛИЗУЕМОЙ В РОДИТЕЛЬСКОМ КЛАССЕ (ШАБЛОННЫЙ ПАТТЕРН)
    //реализовали  метод родительского абстрактного класса (тоже protected, как сигнатура в родительском. но
    // можно расширить до public, а сузить нельзя)
    // для избегания повторения кода. осуществляет поиск элемента по значению методом деления на 2
    //возвращает индекс элемента в массиве по значению этого элемента
    //если элемент не найден- возвращает любое отрицательное число
    @Override
    protected int getIndex(String uuid) {//получаем значение поля
        Resume searchKey = new Resume(uuid);//создаем новый объект

        //сеттер удалим тк стали использовать конструктор
        //searchKey.setUuid(uuid);//новому объекту присваеваем искомое значение поля из аргументов метода

        return Arrays.binarySearch(storage, 0, size, searchKey);
        //где Arrays.binarySearch() уже имеющийся в java метод класса  Arrays для нахождения индекса по значению
        //методом деления на 2. на вход надо подать наш массив, интервал поиска(0-size) и объект содержащий
        //искомое значение-uuid
        //причем storage должен быть отсортирован и по uuid должен быть переопределен equals(в Resume)
        // чтобы сравнивать класс Resume должен implements Comparable<Resume>
    }

    //------------------------------------------------------------------------------------------------------------------

    //ЭТОТ МЕТОД- ЧАСТЬ ЛОГИКИ РЕАЛИЗУЕМОЙ В РОДИТЕЛЬСКОМ КЛАССЕ (ШАБЛОННЫЙ ПАТТЕРН)
    //удаляет элемент из отсортированного массива не ломая сортировку
    //те сдвигает хвост влево
    @Override
    protected void fillDeletedElement(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(storage, index +1, storage, index, numMoved);
            //где storage -массив из которого копируем
            //index +1  индекс с которого начнем копирование
            //storage -массив в который копируем
            //index  индекс в принимающем массиве с которого начнется вставка элементов
            //numMoved  колличество элементов, которое необходимо перенести
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    //ЭТОТ МЕТОД- ЧАСТЬ ЛОГИКИ РЕАЛИЗУЕМОЙ В РОДИТЕЛЬСКОМ КЛАССЕ (ШАБЛОННЫЙ ПАТТЕРН)
    //добавляет элемент в отсортированный массив не ломая сортировку
    //те сдвигает хвост вправо и вставляет в нужное место
    //https://codereview.stackexchange.com/questions/36221/binary-search-for-inserting-in-array
    @Override
    protected void insertElement(Resume r, int index) {
        int insertIdx = -index - 1;
        System.arraycopy(storage, insertIdx, storage, insertIdx + 1, size - insertIdx);
        storage[insertIdx] = r;
        //где storage -массив из которого копируем
        //insertIdx  индекс с которого начнем копирование
        //storage -массив в который копируем
        //insertIdx + 1  индекс в принимающем массиве с которого начнется вставка элементов
        //size - insertIdx колличество элементов, которое необходимо перенести
    }
}