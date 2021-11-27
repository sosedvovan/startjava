package com.urise.webapp.model;

import java.util.Objects;
import java.util.UUID;

/**
 * <p> implements Comparable<Resume> говорит о том что объекты этого класса можно сравнивать-
 * чтобы работал метод Arrays.binarySearch()-поиск индекса по значению методом бинарного деления на 2
 * в классе SortedArrayStorage.
 * <p>
 * но мы убрали implements Comparable<Resume> кот нужен был для метода getIndex()с бинарным поиском
 * в классе SortedArrayStorage
 * и классе SortedArrayStorage вместо Comparable  реализуем Comparator с пом внутр. класса -> лямбды.</p>
 */

// Todo: во всех реализациях Storage замените метод Resume[] getAll() на List<Resume> getAllSorted()
//чтобы возвращался отсортированный массив- вернем implements Comparable<Resume>

public class Resume implements Comparable<Resume> {

    //Unique identifier
    private final String uuid;
    /**
     * <p>
     * сделали поле final -> обязат. нужно инициализировать
     * можно ициализировать с пом конструктора, а сеттер надо удалить тк final
     * </p>
     */

    //Todo: в конструктор Resume добавьте второй параметр — fullName:
    private final String fullName;


    public Resume(String fullName) {//
        this(UUID.randomUUID().toString(), fullName);
    }

    /**
     * <p>
     * выше конструктор без параметров с автогенерацией uuid
     * с пом. this он подает автосгенерированную строку
     * в конструктор с параметрами(который ниже)
     * следовательно он не может существовать без конструктора с параметрами
     * </p>
     */
    public Resume(String uuid, String fullName) {
        //Objects.requireNonNull() бросает nullPointException если в конструктор придет null
        //лучше выбросить это при создании объекта, нежели чем при использовании готового
        //узнать что там null. тк отследить будет сложно.
        Objects.requireNonNull(uuid, "uuid mast not be null");
        Objects.requireNonNull(fullName, "fullName mast not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    //геттер:
    public String getUuid() {
        return uuid;
    }

    /**
     * <p>
     * <p>
     * сеттер удалили тк поле uuid сделали final и теперь можно только через конструктор с ним работать
     * public void setUuid(String uuid) {
     * this.uuid = uuid;
     * }
     *
     * </p>
     */

    //переопределили equals():
    //переопределили hashCode()
    //после добавления поля fullName - перегенерирли. В форме генерации Идеа-
    //- поставили галочки-выбрать не нулевые поля- тк у нас в конструкторе
    //есть проверка на null- зачем этим еще загромождать методы equals() и hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        if (!uuid.equals(resume.uuid)) return false;
        return fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + fullName.hashCode();
        return result;
    }


    //переопределили toString()

    @Override
    public String toString() {
        return uuid + '(' + fullName + ')';
    }

    //Todo: Переделайте компаратор, учтя тот случай, что fullName разных людей может совпадать:
    //имплементируем метод интерфейса
    //сравнение по имени, если вдруг 2-а одинаковых имени
    //то сравниваем еще и по uuid
    @Override
    public int compareTo(Resume o) {
        int cmp = fullName.compareTo(o.fullName);
        return cmp != 0 ? cmp : uuid.compareTo(o.uuid);
    }


    /** <p>
     * реализовали нижележащий закоментированный метод когда этот класс implements Comparable<>
     * но потом Comparable заменили на Comparator с лямбдой в классе SortedArrayStorage
     *
     * @Override
     * public int compareTo(Resume o) {
     *  return uuid.compareTo(o.uuid);//это дописали руками наподобии equals
     * }
     * compareTo() сравнивает в лексикографическом порядке
    </p>*/
}
