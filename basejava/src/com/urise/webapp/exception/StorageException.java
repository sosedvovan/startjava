package com.urise.webapp.exception;

/**
* чтобы класс стал Exception ему надо унаследоваться от RuntimeException или от др Exception.
 * [эксепшен этого класса будем выбрасывать если массив переполнен (Storage overflow),
 * чтобы не создавать еще один спец. для этого].
 */
public class StorageException extends RuntimeException{
    /**
     создадим 2-х наследников этого класса: ExistStorageException - объект уже существует(для save())
     и NotExistStorageException - объект не найден(для update())
     а эксепшен этого класса будем выбрасывать если массив переполнен.

     Ексепшены это как бы обычные классы и мы в них хранить можем переменные
     которые покажут - что же случилось, т.е. private final String uuid будет указывать на проблемный uuid
    */

    /**
     * final можно только с конструктором - иначе ошибка. с сеттером нельзя
     * и в дочерних классах необходимо создать конструкторы принимающие String uuid, иначе ошибка компиляции
     */
    private final String uuid;

    /**
     * так выглядет конструктор (без сообщения):
     * public StorageException(String uuid) {
     * this.uuid = uuid;
     * }
     */


    /**
     * лучше сделаем конструктор с сообщением:
     * super указывает на конструктор RuntimeException от которого наследуем этот класс.
     */
    public StorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }

    /**
     *сделали геттер чтобы поле стало доступным(не бледным):
     */
    public String getUuid() {//
        return uuid;
    }
}

/**
 * *********************************************************************************************************************
 * Наши собственные исключения обрабатывают специфические ошибки нашей программы.
 * ДАЛЕЕ ПРИМЕР СОЗДАНИЯ СОБСТВЕННОГО ЕКСЕПШИНА:
 */

/**
 *public class Bank {
 *     public static void main(String [] args) {
 *         Checking c = new Checking(101);
 *         System.out.println("Депозит $300...");
 *         c.deposit(300.00);
 *
 *         try {
 *             System.out.println("\nСнятие $100...");
 *             c.withdraw(100.00);
 *             System.out.println("\nСнятие $400...");
 *             c.withdraw(400.00);        //1-попробывали выполнить- зашли в этот метод
 *         }catch(InsufficientFundsException e) {//5-этот блок отловил ексепшен кот. появился и выбросился из c.withdraw()
 *             System.out.println("Извините, но у Вас $" + e.getAmount());
 *             e.printStackTrace();
 *         }
 *     }
 * }
 * *******************************************************
 *public class Checking {
 *     private int number;
 *     private double balance;
 *
 *     public Checking(int number) {
 *         this.number = number;
 *     }
 *
 *     public void deposit(double amount) {
 *         balance += amount;
 *     }
 *
 *     public void withdraw(double amount) throws InsufficientFundsException { //4-этот метод сам не обрабатывает ->
 *        // -> пробросили ексепшен обратно в методкоторый вызвал этот метод-там его отловит блок catch.
 *        //throws- перечисляет типы исключений (кроме исключений Error и RuntimeException и их подклассов)
 *
 *         if(amount <= balance) {                 //2- в этом методе не выполнилась проверка -> перешли в блок else
 *             balance -= amount;
 *         }else {
 *             double needs = amount - balance;
 *             throw new InsufficientFundsException(needs);     //3 в этом блоке создали объект нашего ексепшена -
 *                                                         //причем в конструктор ексепшена отправили недостающую сумму
 *         }
 *     }
 *
 *     public double getBalance() {
 *         return balance;
 *     }
 *
 *     public int getNumber() {
 *         return number;
 *     }
 * }
 * *********************************************
 *public class InsufficientFundsException extends Exception {
 *     private double amount;//проблемная переменная
 *
 *     public InsufficientFundsException(double amount) {
 *         this.amount = amount;
 *     }
 *
 *     public double getAmount() {
 *         return amount;
 *     }
 * }
 */

/**
 * Исключения (Exceptions):
 * http://proglang.su/java/exceptions
 *
 * Встроенные исключения:
 * http://proglang.su/java/exceptions-built-in-exceptions
 *
 * Статья про исключения:
 * http://developer.alexanderklimov.ru/android/java/exception.php
 *
 * Руководство по конструкторам:
 * https://topjava.ru/blog/rukovodstvo-po-konstruktoram-v-java
 *
 *Ключевые слова: this, super:
 * https://javarush.ru/groups/posts/1187-raznica-mezhdu-kljuchevihmi-slovami-this-i-super-v-java
 */
