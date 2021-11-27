package storage;

import calculator.Сalculator;
import exception.LogicException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

//класс с проверками и настройка List'ов
public class ListStorageCheck {

    //поля класса
   private List<String> paramsStringArray;//создадим и инициализируем в методе класса
   private List<Integer> paramsIntegerArray = new ArrayList<>();//инициализируем в методе класса
   private String[] params;//инициализируем в конструкторе

    //конструктор
    public ListStorageCheck(String[] params) {
        this.params = params;
    }

    public void massToStringList(){

        //преобразуем обычный массив в ArrayList, чтобы использовать метод removeIf() из интерфейса Collection.
        //и удалим из листа средний элемент - операцию.

        paramsStringArray = new ArrayList<>(List.of(params));

        Predicate<String> pr1 = Predicate.isEqual("+");
        Predicate<String> pr2 = Predicate.isEqual("-");
        Predicate<String> pr3 = Predicate.isEqual("*");
        Predicate<String> pr4 = Predicate.isEqual("/");

        paramsStringArray.removeIf(pr1);
        paramsStringArray.removeIf(pr2);
        paramsStringArray.removeIf(pr3);
        paramsStringArray.removeIf(pr4);

        //проверка, что осталось только 2 элемента - те, что была введена правильная операция( + | - | * | / ):
        if (paramsStringArray.size() != 2) {
            throw new LogicException("Неверная команда. Вы ввели недопустимую операцию ( + | - | * | / )");
            //System.out.println("Неверная команда. Вы ввели недопустимую операцию ( + | - | * | / )");
            //continue;
        }

        //отладочные сообщение
       // System.out.println("отладочное сообщение paramsStringArray" + paramsStringArray);
       // System.out.println("отладочное сообщение 2" + Arrays.toString(params));


        //запускаем следующую проверку.
        checkingTheRange();
    }

    private void checkingTheRange(){

        //проверим диапазон - чтобы был от 1 до 10 целые или все допустимые римские в каждом индексе листа(2индекса):
        if (!(paramsStringArray.get(0).equals("1") || paramsStringArray.get(0).equals("2")
                || paramsStringArray.get(0).equals("3") || paramsStringArray.get(0).equals("4")
                || paramsStringArray.get(0).equals("5") || paramsStringArray.get(0).equals("6")
                || paramsStringArray.get(0).equals("7") || paramsStringArray.get(0).equals("8")
                || paramsStringArray.get(0).equals("9") || paramsStringArray.get(0).equals("10")
                || paramsStringArray.get(0).equals("I") || paramsStringArray.get(0).equals("II")
                || paramsStringArray.get(0).equals("III") || paramsStringArray.get(0).equals("IV")
                || paramsStringArray.get(0).equals("V") || paramsStringArray.get(0).equals("VI")
                || paramsStringArray.get(0).equals("VII") || paramsStringArray.get(0).equals("VIII")
                || paramsStringArray.get(0).equals("IX") || paramsStringArray.get(0).equals("X"))

                &&
                        (!(paramsStringArray.get(1).equals("1") || paramsStringArray.get(1).equals("2")
                        || paramsStringArray.get(1).equals("3") || paramsStringArray.get(1).equals("4")
                        || paramsStringArray.get(1).equals("5") || paramsStringArray.get(1).equals("6")
                        || paramsStringArray.get(1).equals("7") || paramsStringArray.get(1).equals("8")
                        || paramsStringArray.get(1).equals("9") || paramsStringArray.get(1).equals("10")
                        || paramsStringArray.get(1).equals("I") || paramsStringArray.get(1).equals("II")
                        || paramsStringArray.get(1).equals("III") || paramsStringArray.get(1).equals("IV")
                        || paramsStringArray.get(1).equals("V") || paramsStringArray.get(1).equals("VI")
                        || paramsStringArray.get(1).equals("VII") || paramsStringArray.get(1).equals("VIII")
                        || paramsStringArray.get(1).equals("IX") || paramsStringArray.get(1).equals("X")))) {
            throw new LogicException("Вы ввели недопустимые значения!");
            //System.out.println("Вы ввели недопустимые значения!");
            //continue;
        }

        //отладочное сообщения
       // System.out.println("отладочное сообщение 2 paramsStringArray" + paramsStringArray);
       // System.out.println("отладочное сообщение 2" + Arrays.toString(params));

        //запускаем следующую проверку.
        checkingTheSystem();
    }


    private void checkingTheSystem(){

        //   ЗДЕСЬ ПРОВЕРКА В paramsStringArray НА ТО, ЧТО ПЕРВЫЙ И ВТОРОЙ АМПЕРСАНТЫ ОДНОЙ СИСТЕМЫ

        //если в первом индексе арабская, то надо чтобы во втором индексе небыло римской
        if ((paramsStringArray.get(0).equals("1") || paramsStringArray.get(0).equals("2")
                || paramsStringArray.get(0).equals("3") || paramsStringArray.get(0).equals("4")
                || paramsStringArray.get(0).equals("5") || paramsStringArray.get(0).equals("6")
                || paramsStringArray.get(0).equals("7") || paramsStringArray.get(0).equals("8")
                || paramsStringArray.get(0).equals("9") || paramsStringArray.get(0).equals("10")
        ) &&
                (paramsStringArray.get(1).equals("I") || paramsStringArray.get(1).equals("II")
                        || paramsStringArray.get(1).equals("III") || paramsStringArray.get(1).equals("IV")
                        || paramsStringArray.get(1).equals("V") || paramsStringArray.get(1).equals("VI")
                        || paramsStringArray.get(1).equals("VII") || paramsStringArray.get(1).equals("VIII")
                        || paramsStringArray.get(1).equals("IX") || paramsStringArray.get(1).equals("X"))) {
            throw new LogicException("Вы ввели значения из разных систем!");
            //System.out.println("Вы ввели значения из разных систем!");
            //continue;
        }

        //и наоборот, если в первом индексе римская, то во втором индексе не должно быть арабской
        if ((paramsStringArray.get(1).equals("1") || paramsStringArray.get(1).equals("2")
                || paramsStringArray.get(1).equals("3") || paramsStringArray.get(1).equals("4")
                || paramsStringArray.get(1).equals("5") || paramsStringArray.get(1).equals("6")
                || paramsStringArray.get(1).equals("7") || paramsStringArray.get(1).equals("8")
                || paramsStringArray.get(1).equals("9") || paramsStringArray.get(1).equals("10")
        ) &&
                (paramsStringArray.get(0).equals("I") || paramsStringArray.get(0).equals("II")
                        || paramsStringArray.get(0).equals("III") || paramsStringArray.get(0).equals("IV")
                        || paramsStringArray.get(0).equals("V") || paramsStringArray.get(0).equals("VI")
                        || paramsStringArray.get(0).equals("VII") || paramsStringArray.get(0).equals("VIII")
                        || paramsStringArray.get(0).equals("IX") || paramsStringArray.get(0).equals("X"))) {
            throw new LogicException("Вы ввели значения из разных систем!");
            //System.out.println("Вы ввели значения из разных систем!");
            //continue;
        }

        //отладочное сообщения
        //System.out.println("отладочное сообщение 3 paramsStringArray" + paramsStringArray);
       // System.out.println("отладочное сообщение 3" + Arrays.toString(params));

        stringListToIntegerList();
    }

    private void stringListToIntegerList(){

        // ТЕПЕРЬ НАДО СТРОКОВЫЙ МАССИВ ПРЕОБРАЗОВАТЬ В ИНТОВЫЙ ДЛЯ ВОЗМОЖНОСТИ ПРОВЕДЕНИЯ МАТ. ОПЕРАЦИЙ НАД ЭЛЕМЕНТАМИ
        //тк в инты надо перевести не только цифровые литералы, но и буквенные(римские) будем делать это каким-нибудь
        //простым одинаковым способом. без parseInt () или valueOf ()

        if (paramsStringArray.get(0).equals("1") || paramsStringArray.get(0).equals("I")) {
           // paramsIntegerArray.add(0, 1);
            paramsIntegerArray.add(1);
        }

        if (paramsStringArray.get(0).equals("2") || paramsStringArray.get(0).equals("II")) {
            paramsIntegerArray.add(2);
        }

        if (paramsStringArray.get(0).equals("3") || paramsStringArray.get(0).equals("III")) {
            paramsIntegerArray.add(3);
        }

        if (paramsStringArray.get(0).equals("4") || paramsStringArray.get(0).equals("IV")) {
            paramsIntegerArray.add(4);
        }

        if (paramsStringArray.get(0).equals("5") || paramsStringArray.get(0).equals("V")) {
            paramsIntegerArray.add(5);
        }

        if (paramsStringArray.get(0).equals("6") || paramsStringArray.get(0).equals("VI")) {
            paramsIntegerArray.add(6);
        }

        if (paramsStringArray.get(0).equals("7") || paramsStringArray.get(0).equals("VII")) {
            paramsIntegerArray.add(7);
        }

        if (paramsStringArray.get(0).equals("8") || paramsStringArray.get(0).equals("VIII")) {
            paramsIntegerArray.add( 8);
        }

        if (paramsStringArray.get(0).equals("9") || paramsStringArray.get(0).equals("IX")) {
            paramsIntegerArray.add(9);
        }

        if (paramsStringArray.get(0).equals("10") || paramsStringArray.get(0).equals("X")) {
            paramsIntegerArray.add(10);
        }
        //------------
        if (paramsStringArray.get(1).equals("1") || paramsStringArray.get(1).equals("I")) {
            paramsIntegerArray.add(1);
        }

        if (paramsStringArray.get(1).equals("2") || paramsStringArray.get(1).equals("II")) {
            paramsIntegerArray.add(2);
        }

        if (paramsStringArray.get(1).equals("3") || paramsStringArray.get(1).equals("III")) {
            paramsIntegerArray.add(3);
        }

        if (paramsStringArray.get(1).equals("4") || paramsStringArray.get(1).equals("IV")) {
            paramsIntegerArray.add(4);
        }

        if (paramsStringArray.get(1).equals("5") || paramsStringArray.get(1).equals("V")) {
            paramsIntegerArray.add(5);
        }

        if (paramsStringArray.get(1).equals("6") || paramsStringArray.get(1).equals("VI")) {
            paramsIntegerArray.add(6);
        }

        if (paramsStringArray.get(1).equals("7") || paramsStringArray.get(1).equals("VII")) {
            paramsIntegerArray.add(7);
        }

        if (paramsStringArray.get(1).equals("8") || paramsStringArray.get(1).equals("VIII")) {
            paramsIntegerArray.add(8);
        }

        if (paramsStringArray.get(1).equals("9") || paramsStringArray.get(1).equals("IX")) {
            paramsIntegerArray.add(9);
        }

        if (paramsStringArray.get(1).equals("10") || paramsStringArray.get(1).equals("X")) {
            paramsIntegerArray.add(10);
        }

        //УРА, у нас есть List<Integer> с 2-я элементами, далее будем их оперировать)

        //отладочные сообщения
        //System.out.println("отладочное сообщение 4 paramsStringArray" + paramsStringArray);
       // System.out.println("отладочное сообщение 4 paramsIntegerArray" + paramsIntegerArray);
       // System.out.println("отладочное сообщение 4" + Arrays.toString(params));

        //в следующий проверочный метод будет работать с уже заполненным List<Integer>.
        systemDefinition();
    }

    private void systemDefinition(){

        //теперь надо определить какая именно система в Array- арабская или римская
        //с этого момента пойдет распараллеривание логики

        //если в любом индексе листа арабское число, то и в другом индексе тоже будет арабское
        //так как все предварительные проверки пройденны и можно массив передать методу-калькулятору арабских чисел
        if (paramsStringArray.get(0).equals("1") || paramsStringArray.get(0).equals("2")
                || paramsStringArray.get(0).equals("3") || paramsStringArray.get(0).equals("4")
                || paramsStringArray.get(0).equals("5") || paramsStringArray.get(0).equals("6")
                || paramsStringArray.get(0).equals("7") || paramsStringArray.get(0).equals("8")
                || paramsStringArray.get(0).equals("9") || paramsStringArray.get(0).equals("10")) {
            //для арабской системы запускаем арабский калькулятор:
            Сalculator.arab(params[1].intern(), paramsIntegerArray);
        }


        //если в любом индексе листа римское число, то и в другом индексе тоже будет римское
        //так как все предварительные проверки пройденны и можно массив передать методу-калькулятору римских чисел
        if (paramsStringArray.get(1).equals("I") || paramsStringArray.get(1).equals("II")
                || paramsStringArray.get(1).equals("III") || paramsStringArray.get(1).equals("IV")
                || paramsStringArray.get(1).equals("V") || paramsStringArray.get(1).equals("VI")
                || paramsStringArray.get(1).equals("VII") || paramsStringArray.get(1).equals("VIII")
                || paramsStringArray.get(1).equals("IX") || paramsStringArray.get(1).equals("X")) {
            //для римской системы запускаем римский калькулятор:
            Сalculator.rim(params[1].intern(), paramsIntegerArray);
        }

    }
}
