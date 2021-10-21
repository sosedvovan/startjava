package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

//на основе сортированного массива
//implements Storage тк AbstractArrayStorage implements Storage
public class SortedArrayStorage extends AbstractArrayStorage{


    @Override
    public void clear() {

    }

    @Override
    public void update(Resume r) {

    }

    @Override
    public void save(Resume r) {

    }

    @Override
    public void delete(String uuid) {

    }

    @Override
    public Resume[] getAll() {
        return new Resume[0];
    }

    //реализовали  метод родительского абстрактного класса (тоже protected, как сигнатура в родительском. но
    // можно расширить до public, а сузить нельзя)
    // для избегания повторения кода. делает методом деления на 2
    //возвращает индекс элемента в массиве по значению этого элемента
    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size,searchKey);
        //где Arrays.binarySearch() уже имеющийся в java метод класса  Arrays для нахождения индекса по значению
        //методом деления на 2. на вход надо подать наш массив, интервал поиска(0-size) и объект содержащий
        //искомое значение-uuid
        //причем storage должен быть отсортирован и по uuid должен быть переопределен equals(в Resume)
        // чтобы сравнивать класс Resume должен implements Comparable<Resume>
    }
}
