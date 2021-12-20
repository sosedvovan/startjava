package com.urise.webapp;



import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.ArrayStorage;
import com.urise.webapp.storage.Storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

//ИЗ ЭТОГО КЛАССА БУДЕМ ПОЛЬЗОВАТЬСЯ НАШИМИ СОБСТВЕННОРУЧНО НАПИСАННЫМИ ХРАНИЛИЩАМИ-КОЛЛЕКЦИЯМИ.
//РЕАЛИЗОВАННЫМИ НА ОСНОВАХ ОБЫЧНОГО МАССИВА, АРРАЙЛИСТА, МАПЫ...
public class MainArray {

    //Выберем реализацию интерфейса Storage:

    //получаем ссылку типа интерфейс-Storage ARRAY_STORAGE и присваеваем ей реализацию : new ArrayStorage() в данном случае
    //теперь по ссылке ARRAY_STORAGE можно обращаться к абстрактным методам интерфейса и они будут "брать" присвоенную им реализацию
    private final static Storage ARRAY_STORAGE = new ArrayStorage();
    //тип ссылки - Storage тк это интерфейс кот имплементирует ArrayStorage- так принято- обращаться к классам через интерфейс

    // private final static Storage ARRAY_STORAGE = new SortedArrayStorage(); чтобы протестировать класс SortedArrayStorage.

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Resume r;//создали указатель
        while (true) {
            System.out.print("Введите одну из команд - (list | save fullName | delete uuid | get uuid | update uuid fullName | clear | exit): ");
            //проверка - что ввел пользователь: объект reader.считывает всю строку.убирает пробелы в начале и конце.
            // приводит к строчным.разделяет по пробелу: результат помещается в массив params:
            String[] params = reader.readLine().trim().toLowerCase().split(" ");
            //далее проверяем- чтобы в массиве находились один или два элемента- иначе ошибка:
            if (params.length < 1 || params.length > 3) {
                System.out.println("Неверная команда.");
                continue;
            }

            //если в массиве params находится команда состоящая из 2-х слов, то второе слово присвоится переменной String uuid:
            //где uuid это значение поля объекта Резюме, который мы хотим сохранить, изменить, удалить...
            String param = null;
            if (params.length > 1) {
                param = params[1].intern();// Метод intern просто перед созданием объекта String смотрит
                // есть ли этот объект в пуле стрингов и возвращает его. Иначе создается новый объект в пуле.
            }
            switch (params[0]) {
                case "list":
                    printAll();
                    break;
                case "size":
                    System.out.println(ARRAY_STORAGE.size());
                    break;
                case "save"://сохранение в массиве:
                    r = new Resume(param);//создали новый объект
                    //r.setUuid(uuid);//в его сеттер послали второе слово команды//сеттер удалили тк стали использовать конструктор
                    ARRAY_STORAGE.save(r);//этот новый объект отправили в метод save() класса ArrayStorage(кот не сортирован)
                    printAll();
                    break;
                case "update":
                    r = new Resume(param, params[2] );//та же логика, что и при save
                    //r.setUuid(uuid);//сеттер удалили тк стали использовать конструктор
                    ARRAY_STORAGE.update(r);
                    printAll();
                    break;
                case "delete":
                    ARRAY_STORAGE.delete(param);
                    printAll();
                    break;
                case "get":
                    System.out.println(ARRAY_STORAGE.get(param));
                    break;
                case "clear":
                    ARRAY_STORAGE.clear();
                    printAll();
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("Неверная команда.");
                    break;
            }
        }
    }

    static void printAll() {
        List<Resume> all = ARRAY_STORAGE.getAllSorted();//массиву all присвоим то, что вернет метод getAll()
        System.out.println("----------------------------");
        if (all.size() == 0) {//если массив all окажется пустой выведем "Empty":
            System.out.println("Empty");
        } else {//иначе(если не пустой) пробежим по массиву и выведем его элементы)
            for (Resume r : all) {
                System.out.println(r);
            }
        }
        System.out.println("----------------------------");
    }
}
