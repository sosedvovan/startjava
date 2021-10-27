package com.urise.webapp.model;

import java.util.UUID;

//implements Comparable<Resume> говорит о том что объекты этого класса можно сравнивать-
//чтобы работал метод Arrays.binarySearch()-поиск индекса по значению методом бинарного деления на 2
//реализуем метод compareTo() этого интерфейса и подправим его вручную. см ниже.
public class Resume implements Comparable<Resume>{

    //Unique identifier
    private final String uuid;//сделали поле final -> обязат. нужен конструктор, а сеттер надо удалить

    public Resume(){//это конструктор без параметров с автогенерацией uuid
        this(UUID.randomUUID().toString());//с пом. this он подает автосгенерированную строку
                                            //в конструктор с параметрами(который ниже)
                                            //но мы его не исспользуем пока-он бледный
    }

    public Resume(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    //сеттер удалили тк поле uuid сделали final и теперь можно только через конструктор с ним работать
    //public void setUuid(String uuid) {
    //    this.uuid = uuid;
   // }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;//если в аргументах подали тот же обект
        if (o == null || getClass() != o.getClass()) return false;//если подали null вместо объекта
                                                                    // или объект другого класса

        Resume resume = (Resume) o;//приводим Object o к нашему классу

        return uuid != null ? uuid.equals(resume.uuid) : resume.uuid == null;//проверка поля объекта на null
                                                                              // и сравнение поля обекта
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public int compareTo(Resume o) { //реализовали тк этот класс implements Comparable<>
        return uuid.compareTo(o.uuid);//это дописали руками наподобии equals
    }
}
