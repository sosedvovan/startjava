package com.urise.webapp.model;

import java.util.Objects;
import java.util.UUID;

/** <p> implements Comparable<Resume> говорит о том что объекты этого класса можно сравнивать-
* чтобы работал метод Arrays.binarySearch()-поиск индекса по значению методом бинарного деления на 2
* в классе SortedArrayStorage.

* но мы убрали implements Comparable<Resume> кот нужен был для метода getIndex()с бинарным поиском
 * в классе SortedArrayStorage
* и классе SortedArrayStorage вместо Comparable  реализуем Comparator с пом внутр. класса -> лямбды.</p>
 */

public class Resume /*implements Comparable<Resume>*/{

    //Unique identifier
    private final String uuid;
    /** <p>
     *сделали поле final -> обязат. нужно инициализировать
     * можно ициализировать с пом конструктора, а сеттер надо удалить тк final
     </p>*/

    private  String fullName;


    public Resume(){//
        this(UUID.randomUUID().toString());
    }
    /** <p>
     * выше конструктор без параметров с автогенерацией uuid
     * с пом. this он подает автосгенерированную строку
     * в конструктор с параметрами(который ниже)
     * следовательно он не может существовать без конструктора с параметрами
     * </p>*/
    public Resume(String uuid) {
        this.uuid = uuid;
    }

    //геттер:
    public String getUuid() {
        return uuid;
    }

    /** <p>
     *
     * сеттер удалили тк поле uuid сделали final и теперь можно только через конструктор с ним работать
     * public void setUuid(String uuid) {
     * this.uuid = uuid;
     * }
     *
     </p>*/

    //переопределили equals():
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;//если в аргументах подали тот же обект

        if (o == null || getClass() != o.getClass()) return false;
        //если подали null вместо объекта, или объект другого класса

        Resume resume = (Resume) o;//приводим Object o к нашему классу

        return Objects.equals(uuid, resume.uuid);//проверка равенства объектов по полю uuid будет
        //перегруженный equals- возвращает true если первый не null и если равны ссылки и значения полей
    }

    //переопределили hashCode()
    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    //переопределили toString()
    @Override
    public String toString() {
        return uuid;
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
