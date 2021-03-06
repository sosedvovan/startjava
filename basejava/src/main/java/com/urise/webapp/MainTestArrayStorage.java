package com.urise.webapp;


import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.ArrayStorage;
import com.urise.webapp.storage.Storage;

public class MainTestArrayStorage {
    //static тк обращаемся из static void main к этому объекту
    //те чтобы в статическом методе  использовать какие то члены класса
    //то они должны быть тоже статическими
    //final значит что мы не можем присвоить этому объекту в дальнейшем null, например
    //но внутри объекта можно поменять все что угодно
    //private тк это поле класса
    //называем большими буквами тк это private final static по конвенции
    //тип ссылки - Storage тк это интерфейс кот имплементирует ArrayStorage- так принято- обращаться к классам через интерфейс

    //ПРОСТО СОЗДАЛИ ОБЪЕКТ КЛАССА ArrayStorage:(и тк в этом классе реализованна функциональность,  на основе обычного массива,
    //то далее будем работать с нашим  ARRAY_STORAGE так, как и с обычной коллекцией Ява.
    //получаем ссылку типа интерфейс-Storage ARRAY_STORAGE и присваеваем ей реализацию : new ArrayStorage() в данном случае
    //теперь по ссылке ARRAY_STORAGE можно обращаться к абстрактным методам интерфейса и они будут "брать" присвоенную им реализацию
    //те мы выбрали реализацию для хранилища интерфейса Storage(объект new ArrayStorage() принес реализацию интерфейсу Storage)
   private final static Storage ARRAY_STORAGE = new ArrayStorage();//тип Storage - это интерфейс(так принято обращаться к классам)

   // private final static Storage ARRAY_STORAGE = new SortedArrayStorage(); чтобы протестировать класс SortedArrayStorage.

    public static void main(String[] args) {
        //СОЗДАДИМ ТРИ ОБЪЕКТА НАШЕЙ МОДЕЛИ, ДЛЯ ДАЛЬНЕЙШЕГО ДОБАВЛЕНИЯ ИХ В НАШ ARRAY_STORAGE:
       final Resume r1 = new Resume("uuid1",  "Name1");
        //r1.setUuid("uuid1");// поле сделали final -> пользуем конструктор. сеттер теперь нельзя
       final Resume r2 = new Resume("uuid2", "Name2" );
        //r2.setUuid("uuid2");// поле сделали final -> пользуем конструктор. сеттер теперь нельзя
       final Resume r3 = new Resume("uuid3", "Name3");
        //r3.setUuid("uuid3");// поле сделали final -> пользуем конструктор. сеттер теперь нельзя

        //здесь ARRAY_STORAGE получили реализацию от ArrayStorage те ложить объекты будем в массив :->
        //-> protected Resume[] storage = new Resume[STORAGE_LIMIT]; который и описан в ArrayStorage
        ARRAY_STORAGE.save(r1);//save() это наш рукописный метод (наподобии add() и put() в Ява)
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r3);

        //get() и  size() это наши рукописныеметоды (наподобии add() и put()... в Ява)
        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size());

       // System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));//пробуем взять несуществующий

            //ниже в //строке error: тк отсюда не видно protected Resume[] storage из сlass AbstractArrayStorage
        // и в private final static Storage ARRAY_STORAGE -- интерфейс Storage надо исправить на класс ArrayStorage.
        //System.out.println("Index of r3: " + Arrays.binarySearch(ARRAY_STORAGE.storage, 0, ARRAY_STORAGE.size(), r3));

        printAll();
        ARRAY_STORAGE.delete(r1.getUuid());//delete() это наш рукописный метод (наподобии add() и put() в Ява)
        printAll();
        ARRAY_STORAGE.clear();//clear() это наш рукописный метод (наподобии add() и put() в Ява)
        printAll();

        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAllSorted()) {
            System.out.println(r);
        }
    }
}
