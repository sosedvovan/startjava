package com.urise.webapp.model;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * <p> implements Comparable<Resume> говорит о том что объекты этого класса можно сравнивать-
 * чтобы работал метод Arrays.binarySearch()-поиск индекса по значению методом бинарного деления на 2
 * в классе SortedArrayStorage.
 * <p>
 * но мы убирали implements Comparable<Resume> кот нужен был для метода getIndex()с бинарным поиском
 * в классе SortedArrayStorage
 * и классе SortedArrayStorage вместо Comparable  реализуем Comparator с пом внутр. класса -> лямбды.</p>
 */

// Todo: во всех реализациях Storage замените метод Resume[] getAll() на List<Resume> getAllSorted()
//чтобы возвращался отсортированный массив- вернем implements Comparable<Resume>

    //Класс- Главная модель- зависит от объектов всех классов в своем пэкедже
public class Resume implements Comparable<Resume>, Serializable {

    //---------------------------------------------------------------------------------------------------------
    //                                          ПОЛЯ

    //делаем такую штуку для сериализации(чтобы тесты проходили) -это какаято версия класса
    //иначе Ява генерирует ее всегда заново сама при любых изменениях в классе
    //при сериализации инстанс этого класса превратится в байты и запишется в файл
    //а при восстановлении объекта из файла Ява будет искать такую же версию класса
    //как и при сериализации. и если мы что то изменим в классе(и его версия
    // соотв изменится) - десериализация провалится
    private static final long serialVersionUID = 1L;//версия класса- ввели вручную
    //и принято, если мы чтото меняем в этом классе в кот есть сериализация
    //то и именяем и эту переменную
    //так же скопируем ее в организайшен и все 3-и секции
    //(в родителя секций-Секшен не надо: это по наследству не работает)

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

    //EnumMap - мапа, где ключи- енумы. в его конструктор подается класс
    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);

    //---------------------------------------------------------------------------------------------------------
    //                                      КОНСТРУКТОРЫ

    /**
     * <p>
     * конструктор без параметров с автогенерацией uuid
     * с пом. this он подает автосгенерированную строку
     * в конструктор с параметрами(который ниже)
     * следовательно этот низкоуровневый конструктор не
     * может существовать без конструктора с инициализацией параметров
     * </p>
     */
    public Resume(String fullName) {

        this(UUID.randomUUID().toString(), fullName);
    }


    public Resume(String uuid, String fullName) {
        //Objects.requireNonNull() бросает nullPointException если в конструктор придет null
        //лучше выбросить это при создании объекта, нежели чем при использовании готового
        //узнать что там null. тк отследить будет сложно.
        Objects.requireNonNull(uuid, "uuid mast not be null");
        Objects.requireNonNull(fullName, "fullName mast not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    //-------------------------------------------------------------------------------------------------------
    //геттер:
    public String getUuid() {
        return uuid;
    }

    //геттер для поля- подаем ключ мапы берем значение
    public String getContact(ContactType type){
        return contacts.get(type);
    }

    //геттер для поля- подаем ключ мапы берем значение
    public Section getSection(SectionType type){
        return sections.get(type);
    }

    //-------------------------------------------------------------------------------------------------------
    //                   МЕТОДЫ- ТИПА СЕТТЕРЫ ДЛЯ ПОЛЕЙ МАП

    public void addContact(ContactType type, String value) {
        contacts.put(type, value);
    }


    public void addSection(SectionType type, Section section) {
        sections.put(type, section);
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

    //-----------------------------------------------------------------------------------------------------------
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

    //----------------------------------------------------------------------------------------------------------

    //Todo: Переделайте компаратор, учтя тот случай, что fullName разных людей может совпадать:
    // имплементируем метод интерфейса
    // сравнение по имени, если вдруг 2-а одинаковых имени
    // то сравниваем еще и по uuid
    @Override
    public int compareTo(Resume o) {
        int cmp = this.fullName.compareTo(o.fullName);//можно без this. сказать
        return cmp != 0 ? cmp : this.uuid.compareTo(o.uuid);
    }


    /** <p>
     * реализовали нижележащий закоментированный метод когда этот класс implements Comparable<>
     * но потом Comparable заменили на Comparator с лямбдой в классе SortedArrayStorage
     *ЭТОТ КОМПОРАТОР СРАВНИВАЛ ТОЛЬКО ПО uuid
     * @Override
     * public int compareTo(Resume o) {
     *  return uuid.compareTo(o.uuid);//это дописали руками наподобии equals
     * }
     * compareTo() сравнивает в лексикографическом порядке
    </p>*/
}
