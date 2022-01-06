package com.urise.webapp.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * <p> implements Comparable<Resume> говорит о том что объекты этого класса можно сравнивать-
 * чтобы работал метод Arrays.binarySearch()-поиск индекса по значению методом бинарного деления на 2
 * в классе SortedArrayStorage.
 *
 * но мы убирали implements Comparable<Resume> кот нужен был для метода getIndex()с бинарным поиском
 * в классе SortedArrayStorage
 * и классе SortedArrayStorage вместо Comparable  реализуем Comparator с пом внутр. класса -> лямбды.</p>
 */

// Todo: во всех реализациях Storage замените метод Resume[] getAll() на List<Resume> getAllSorted()
// чтобы возвращался отсортированный массив- вернем implements Comparable<Resume>

    //Класс- Главная модель- зависит от объектов всех классов в своем пэкедже

@XmlRootElement//+для Джакарты-XML нужен конструктор по умолчанию, тк она через отражение

//+Джакарта работает только с сеттерами(а здесь сеттеры только на 2-а поля есть)
//и чтобы не делать сеттеры добавим Анотацию(тогда начнет работать с полями):
@XmlAccessorType(XmlAccessType.FIELD)
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
    //и принято, если мы чтото меняем в классе, в котором есть сериализация
    //то и именяем и эту переменную
    //так же скопируем ее в организайшен и все 3-и секции- в классы, от которых зависит этот класс
    //(в родителя секций-Секшен не надо: это по наследству не работает)

    //Unique identifier
    private  String uuid;
    /**
     * <p>
     * сделали поле final -> обязат. нужно инициализировать
     * можно ициализировать с пом конструктора, а сеттер надо удалить тк final
     * (далее потеряли final для получения возможности сериализовать в XML)
     * </p>
     */

    //Todo: в конструктор Resume добавьте второй параметр — fullName:
    private  String fullName;

    //EnumMap - мапа, где ключи- енумы. в его конструктор подается класс
    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);

    //---------------------------------------------------------------------------------------------------------
    //                                      КОНСТРУКТОРЫ


    //для Джакарты-XML нужен конструктор по умолчанию, тк она через отражение
    //и мы теряем final в полях
    public Resume() {
    }

    /**
     * <p>
     * конструктор без параметров с автогенерацией uuid
     * с пом. this он подает автосгенерированную строку
     * в высокоуровневый конструктор с параметрами(который ниже)
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFullName() {
        return fullName;
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public Map<SectionType, Section> getSections() {
        return sections;
    }

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



    /**<p>
     * сеттер удалили тк поле uuid сделали final и теперь можно только через конструктор с ним работать
     * в дальнейшем и final удалили из-за XML.
     * public void setUuid(String uuid) {
     * this.uuid = uuid;
     * }
     * </p>*/

    //-----------------------------------------------------------------------------------------------------------
    //переопределили equals():
    //переопределили hashCode()
    //после добавления поля fullName - перегенерили. В форме генерации Идеа-
    //- поставили галочки-выбрать не нулевые поля- тк у нас в конструкторе
    //есть проверка на null- зачем этим еще загромождать методы equals() и hashCode()
    //ЕЩЕ РАЗ ПЕРЕГЕНЕРРИМ ТК ДОБАВИЛИ ПОЛЯ_МАПЫ. БЕЗ НЕНУЛЕВЫХ ПОЛЕЙ.
    //И ТЕМПЛЕЙТ ВЫБРАЛИ (В ВЫПАД СПИСКЕ НА 1 СТР) - ЯВА 7+

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid) &&
                Objects.equals(fullName, resume.fullName) &&
                Objects.equals(contacts, resume.contacts) &&
                Objects.equals(sections, resume.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, sections);
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
