import exception.LogicException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) throws IOException {

        List<String> paramsStringArray;
        List<Integer> paramsIntegerArray = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

      //  while (true) {

            System.out.println("Введите через пробел: operand#1 operation operand#2");
            System.out.println("В качестве операндов можно использовать арабские целые числа от 1 до 10 включительно");
            System.out.println("В качестве операндов можно использовать римские числа от I до X включительно");
            System.out.println("В качестве операций над числами используйте только ( + | - | * | / )");

            //разобьем введенную строку на элементы и поместим эти элементы в обычный массив:
            String[] params = reader.readLine().trim().toUpperCase().split(" ");

            //------------------------------------------------------------------------------------------------------
            //                                ПРОВЕРКА №1

            //проверка, что ввели только 3 элемента:
            if (params.length != 3) {
                //System.out.println("Неверная команда.");
                throw new LogicException("Неверная команда.");
                //continue;
            }

            //------------------------------------------------------------------------------------------------------

            //операцию запомним в в этой переменной:
            String operation = params[1].intern();

            //------------------------------------------------------------------------------------------------------

            //преобразуем обычный массив в ArrayList, чтобы использовать метод removeIf() из интерфейса Collection.
            //и удалим из листа средний элемент - операцию.
            //тодо: можно избавиться от обычного массива в коде.
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

            //-------------------------------------------------------------------------------------------------
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
                    || paramsStringArray.get(0).equals("IX") || paramsStringArray.get(0).equals("X")) &&
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
            //----------------------------------------------------------------------------------------------
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

            //-------------------------------------------------------------------------------------------------
            // ТЕПЕРЬ НАДО СТРОКОВЫЙ МАССИВ ПРЕОБРАЗОВАТЬ В ИНТОВЫЙ ДЛЯ ВОЗМОЖНОСТИ ПРОВЕДЕНИЯ МАТ. ОПЕРАЦИЙ НАД ЭЛЕМЕНТАМИ
            //тк в инты надо перевести не только цифровые литералы, но и буквенные(римские) будем делать это каким-нибудь
            //простым одинаковым способом. без parseInt () или valueOf ()
            //надо бы вынести в отдельный метод и чз итератор организовать

            if (paramsStringArray.get(0).equals("1") || paramsStringArray.get(0).equals("I")) {
                paramsIntegerArray.add(0, 1);
            }

            if (paramsStringArray.get(0).equals("2") || paramsStringArray.get(0).equals("II")) {
                paramsIntegerArray.add(0, 2);
            }

            if (paramsStringArray.get(0).equals("3") || paramsStringArray.get(0).equals("III")) {
                paramsIntegerArray.add(0, 3);
            }

            if (paramsStringArray.get(0).equals("4") || paramsStringArray.get(0).equals("IV")) {
                paramsIntegerArray.add(0, 4);
            }

            if (paramsStringArray.get(0).equals("5") || paramsStringArray.get(0).equals("V")) {
                paramsIntegerArray.add(0, 5);
            }

            if (paramsStringArray.get(0).equals("6") || paramsStringArray.get(0).equals("VI")) {
                paramsIntegerArray.add(0, 6);
            }

            if (paramsStringArray.get(0).equals("7") || paramsStringArray.get(0).equals("VII")) {
                paramsIntegerArray.add(0, 7);
            }

            if (paramsStringArray.get(0).equals("8") || paramsStringArray.get(0).equals("VIII")) {
                paramsIntegerArray.add(0, 8);
            }

            if (paramsStringArray.get(0).equals("9") || paramsStringArray.get(0).equals("IX")) {
                paramsIntegerArray.add(0, 9);
            }

            if (paramsStringArray.get(0).equals("10") || paramsStringArray.get(0).equals("X")) {
                paramsIntegerArray.add(0, 10);
            }
            //------------
            if (paramsStringArray.get(1).equals("1") || paramsStringArray.get(0).equals("I")) {
                paramsIntegerArray.add(1, 1);
            }

            if (paramsStringArray.get(1).equals("2") || paramsStringArray.get(0).equals("II")) {
                paramsIntegerArray.add(1, 2);
            }

            if (paramsStringArray.get(1).equals("3") || paramsStringArray.get(0).equals("III")) {
                paramsIntegerArray.add(1, 3);
            }

            if (paramsStringArray.get(1).equals("4") || paramsStringArray.get(0).equals("IV")) {
                paramsIntegerArray.add(1, 4);
            }

            if (paramsStringArray.get(1).equals("5") || paramsStringArray.get(0).equals("V")) {
                paramsIntegerArray.add(1, 5);
            }

            if (paramsStringArray.get(1).equals("6") || paramsStringArray.get(0).equals("VI")) {
                paramsIntegerArray.add(1, 6);
            }

            if (paramsStringArray.get(1).equals("7") || paramsStringArray.get(0).equals("VII")) {
                paramsIntegerArray.add(1, 7);
            }

            if (paramsStringArray.get(1).equals("8") || paramsStringArray.get(0).equals("VIII")) {
                paramsIntegerArray.add(1, 8);
            }

            if (paramsStringArray.get(1).equals("9") || paramsStringArray.get(0).equals("IX")) {
                paramsIntegerArray.add(1, 9);
            }

            if (paramsStringArray.get(1).equals("10") || paramsStringArray.get(0).equals("X")) {
                paramsIntegerArray.add(1, 10);
            }

            //УРА, у нас есть Аррай<Интеджер> с 2-я элементами, далее будем их оперировать)

            //-------------------------------------------------------------------------------------------------
            //теперь надо определить какая именно система в Аррае- арабская или римская
            //с этого момента пойдет распараллеривание логики

            //если в любом индексе листа арабское число, то и в другом индексе тоже будет арабское
            //так как все предварительные проверки пройденны и можно массив передать методу-калькулятору арабских чисел
            if (paramsStringArray.get(0).equals("1") || paramsStringArray.get(0).equals("2")
                    || paramsStringArray.get(0).equals("3") || paramsStringArray.get(0).equals("4")
                    || paramsStringArray.get(0).equals("5") || paramsStringArray.get(0).equals("6")
                    || paramsStringArray.get(0).equals("7") || paramsStringArray.get(0).equals("8")
                    || paramsStringArray.get(0).equals("9") || paramsStringArray.get(0).equals("10")) {
                arab(operation, paramsIntegerArray);
                //break;
            }


            //если в любом индексе листа римское число, то и в другом индексе тоже будет римское
            //так как все предварительные проверки пройденны и можно массив передать методу-калькулятору римских чисел
            if (paramsStringArray.get(1).equals("I") || paramsStringArray.get(1).equals("II")
                    || paramsStringArray.get(1).equals("III") || paramsStringArray.get(1).equals("IV")
                    || paramsStringArray.get(1).equals("V") || paramsStringArray.get(1).equals("VI")
                    || paramsStringArray.get(1).equals("VII") || paramsStringArray.get(1).equals("VIII")
                    || paramsStringArray.get(1).equals("IX") || paramsStringArray.get(1).equals("X")) {
                rim(operation, paramsIntegerArray);
                //break;
            }


            //-------------------------------------------------------------------------------------


           // break;

        //}


        //System.out.println(paramsStringArray);
        // System.out.println(paramsStringArray.get(0));
    }

    private static void rim(String operation, List<Integer> paramsIntegerArray) {
        int result = 0;
        switch (operation) {
            case "+":
                result = paramsIntegerArray.get(0) + paramsIntegerArray.get(1);
                break;
            case "-":
                result = paramsIntegerArray.get(0) - paramsIntegerArray.get(1);
                break;
            case "*":
                result = paramsIntegerArray.get(0) * paramsIntegerArray.get(1);
                break;
            case "/":
                if (paramsIntegerArray.get(1) != 0) {
                    result = paramsIntegerArray.get(0) / paramsIntegerArray.get(1);
                } else {
                    System.out.println("На 0 делить нельзя");
                    break;
                }
        }
        if (result < 1)
            System.out.println("Результат: Ошибка, в римской системе нет отрицательных чисел и нуля");
        if (result == 1)
            System.out.println("Результат: I");
        if (result == 2)
            System.out.println("Результат: II");
        if (result == 3)
            System.out.println("Результат: III");
        if (result == 4)
            System.out.println("Результат: IV");
        if (result == 5)
            System.out.println("Результат: V");
        if (result == 6)
            System.out.println("Результат: VI");
        if (result == 7)
            System.out.println("Результат: VII");
        if (result == 8)
            System.out.println("Результат: VIII");
        if (result == 9)
            System.out.println("Результат: IX");
        if (result == 10)
            System.out.println("Результат: X");
        if (result == 11)
            System.out.println("Результат: XI");
        if (result == 12)
            System.out.println("Результат: XII");
        if (result == 13)
            System.out.println("Результат: XIII");
        if (result == 14)
            System.out.println("Результат: XIV");
        if (result == 15)
            System.out.println("Результат: XV");
        if (result == 16)
            System.out.println("Результат: XVI");
        if (result == 17)
            System.out.println("Результат: XVII");
        if (result == 18)
            System.out.println("Результат: XVIII");
        if (result == 19)
            System.out.println("Результат: XIX");
        if (result == 20)
            System.out.println("Результат: XX");
        if (result == 30)
            System.out.println("Результат: XXX");
        if (result == 40)
            System.out.println("Результат: XL");
        if (result == 50)
            System.out.println("Результат: L");
        if (result == 60)
            System.out.println("Результат: LX");
        if (result == 70)
            System.out.println("Результат: LXX");
        if (result == 80)
            System.out.println("Результат: LXXX");
        if (result == 90)
            System.out.println("Результат: XC");
        if (result == 100)
            System.out.println("Результат: C");
    }

    private static void arab(String operation, List<Integer> paramsIntegerArray) {
        int result = 0;
        switch (operation) {
            case "+":
                result = paramsIntegerArray.get(0) + paramsIntegerArray.get(1);
                break;
            case "-":
                result = paramsIntegerArray.get(0) - paramsIntegerArray.get(1);
                break;
            case "*":
                result = paramsIntegerArray.get(0) * paramsIntegerArray.get(1);
                break;
            case "/":
                if (paramsIntegerArray.get(1) != 0) {
                    result = paramsIntegerArray.get(0) / paramsIntegerArray.get(1);
                } else {
                    System.out.println("На 0 делить нельзя");
                    break;
                }
        }
        System.out.println("Результат: " + result);
    }
}
