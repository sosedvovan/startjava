package com.com.urise.webapp;



import com.com.urise.webapp.model.Resume;
import com.com.urise.webapp.storage.ArrayStorage;
import com.com.urise.webapp.storage.Storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainArray {
    private final static Storage ARRAY_STORAGE = new ArrayStorage();
    //тип ссылки - Storage тк это интерфейс кот имплементирует ArrayStorage- так принято- обращаться к классам через интерфейс

    // private final static Storage ARRAY_STORAGE = new SortedArrayStorage(); чтобы протестировать класс SortedArrayStorage.

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Resume r;//создали указатель
        while (true) {
            System.out.print("Введите одну из команд - (list | save uuid | delete uuid | get uuid | update uuid | clear | exit): ");
            //проверка - что ввел пользователь: объект reader.считывает всю строку.убирает пробелы в начале и конце.
            // приводит к строчным.разделяет по пробелу: результат помещается в массив params:
            String[] params = reader.readLine().trim().toLowerCase().split(" ");
            //далее проверяем- чтобы в массиве находились один или два элемента- иначе ошибка:
            if (params.length < 1 || params.length > 2) {
                System.out.println("Неверная команда.");
                continue;
            }

            //если в массиве params находится команда состоящая из 2-х слов, то второе слово присвоится переменной String uuid:
            String uuid = null;
            if (params.length == 2) {
                uuid = params[1].intern();// Метод intern просто перед созданием объекта String смотрит
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
                    r = new Resume(uuid);//создали новый объект
                    //r.setUuid(uuid);//в его сеттер послали второе слово команды//сеттер удалили тк стали использовать конструктор
                    ARRAY_STORAGE.save(r);//этот новый объект отправили в метод save() класса ArrayStorage(кот не сортирован)
                    printAll();
                    break;
                case "update":
                    r = new Resume(uuid);//та же логика, что и при save
                    //r.setUuid(uuid);//сеттер удалили тк стали использовать конструктор
                    ARRAY_STORAGE.update(r);
                    printAll();
                    break;
                case "delete":
                    ARRAY_STORAGE.delete(uuid);
                    printAll();
                    break;
                case "get":
                    System.out.println(ARRAY_STORAGE.get(uuid));
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
        Resume[] all = ARRAY_STORAGE.getAll();//массиву all присвоим то, что вернет метод getAll()
        System.out.println("----------------------------");
        if (all.length == 0) {//если массив all окажется пустой выведем "Empty":
            System.out.println("Empty");
        } else {//иначе(если не пустой) пробежим по массиву и выведем его элементы)
            for (Resume r : all) {
                System.out.println(r);
            }
        }
        System.out.println("----------------------------");
    }
}
