import exception.LogicException;
import storage.ListStorageCheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Введите через пробел: operand#1 operation operand#2");
        System.out.println("В качестве операндов можно использовать арабские целые числа от 1 до 10 включительно");
        System.out.println("В качестве операндов можно использовать римские числа от I до X включительно");
        System.out.println("В качестве операций над числами используйте только ( + | - | * | / )");

        //разобьем введенную строку на элементы и поместим эти элементы в обычный массив:
        String[] params = reader.readLine().trim().toUpperCase().split(" ");

        //проверка, что ввели только 3 элемента:
        if (params.length != 3) {
            //System.out.println("Неверная команда.");
            throw new LogicException("Неверная команда.");
            //continue;
        }

        //создаем объект класса с проверками и запускаем его "первый" public метод и далее автоматически выполнятся
        //цепочка проверочных и настроечных private методов и запускаются методы из класса Сalculator.
        //в конструктор передаем массив с задачей:
        ListStorageCheck listStorageCheck = new ListStorageCheck(params);
        listStorageCheck.massToStringList();

    }
}
