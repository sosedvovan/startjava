package calculator;

import java.util.List;

//в классе 2-а static метода которые калькулируют и выводят результат.
//static- чтобы не создавать инстансы.
public class Сalculator {

    public static void rim(String operation, List<Integer> paramsIntegerArray) {
        //Todo: блоки switch-case надо вынести в отдельный private метод
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
        //Todo: для преобразования арабских цифр в римские надо написать private метод в котором знаки арабского числа
        // помещаются в массив и на основе этого массива формируется (StringBuilder) римское число
        if (result < 1)
            //здесь можно бросить наше исключение
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
        if (result == 21)
            System.out.println("Результат: XXI");
        if (result == 22)
            System.out.println("Результат: XXII");
        if (result == 23)
            System.out.println("Результат: XXII");
        if (result == 24)
            System.out.println("Результат: XXIV");
        if (result == 25)
            System.out.println("Результат: XXV");
        if (result == 26)
            System.out.println("Результат: XXVI");
        if (result == 27)
            System.out.println("Результат: XXVII");
        if (result == 28)
            System.out.println("Результат: XXVIII");
        if (result == 29)
            System.out.println("Результат: XXIX");
        if (result == 30)
            System.out.println("Результат: XXX");
        if (result == 31)
            System.out.println("Результат: XXXI");
        if (result == 32)
            System.out.println("Результат: XXXII");
        if (result == 33)
            System.out.println("Результат: XXXII");
        if (result == 34)
            System.out.println("Результат: XXXIV");
        if (result == 35)
            System.out.println("Результат: XXXV");
        if (result == 36)
            System.out.println("Результат: XXXVI");
        if (result == 37)
            System.out.println("Результат: XXXVII");
        if (result == 38)
            System.out.println("Результат: XXXVII");
        if (result == 39)
            System.out.println("Результат: XXXIX");
        if (result == 40)
            System.out.println("Результат: XL");
        if (result == 41)
            System.out.println("Результат: XLI");
        if (result == 42)
            System.out.println("Результат: XLII");
        if (result == 43)
            System.out.println("Результат: XLIII");
        if (result == 44)
            System.out.println("Результат: XLIV");
        if (result == 45)
            System.out.println("Результат: XLV");
        if (result == 46)
            System.out.println("Результат: XLVI");
        if (result == 47)
            System.out.println("Результат: XLVII");
        if (result == 48)
            System.out.println("Результат: XLVIII");
        if (result == 49)
            System.out.println("Результат: XLIX");
        if (result == 50)
            System.out.println("Результат: L");
        if (result == 51)
            System.out.println("Результат: LI");
        if (result == 52)
            System.out.println("Результат: LII");
        if (result == 53)
            System.out.println("Результат: LIII");
        if (result == 54)
            System.out.println("Результат: LIV");
        if (result == 55)
            System.out.println("Результат: LV");
        if (result == 56)
            System.out.println("Результат: LVI");
        if (result == 57)
            System.out.println("Результат: LVII");
        if (result == 58)
            System.out.println("Результат: LVIII");
        if (result == 59)
            System.out.println("Результат: LIX");
        if (result == 60)
            System.out.println("Результат: LX");
        if (result == 61)
            System.out.println("Результат: LXI");
        if (result == 62)
            System.out.println("Результат: LXII");
        if (result == 63)
            System.out.println("Результат: LXIII");
        if (result == 64)
            System.out.println("Результат: LXIV");
        if (result == 65)
            System.out.println("Результат: LXV");
        if (result == 66)
            System.out.println("Результат: LXVI");
        if (result == 67)
            System.out.println("Результат: LXVII");
        if (result == 68)
            System.out.println("Результат: LXVIII");
        if (result == 69)
            System.out.println("Результат: LXIX");
        if (result == 70)
            System.out.println("Результат: LXX");
        if (result == 71)
            System.out.println("Результат: LXXI");
        if (result == 72)
            System.out.println("Результат: LXXII");
        if (result == 73)
            System.out.println("Результат: LXXIII");
        if (result == 74)
            System.out.println("Результат: LXXIV");
        if (result == 75)
            System.out.println("Результат: LXXV");
        if (result == 76)
            System.out.println("Результат: LXXVI");
        if (result == 77)
            System.out.println("Результат: LXXVII");
        if (result == 78)
            System.out.println("Результат: LXXVIII");
        if (result == 79)
            System.out.println("Результат: LXXIX");
        if (result == 80)
            System.out.println("Результат: LXXX");
        if (result == 81)
            System.out.println("Результат: LXXXI");
        if (result == 82)
            System.out.println("Результат: LXXXII");
        if (result == 83)
            System.out.println("Результат: LXXXIII");
        if (result == 84)
            System.out.println("Результат: LXXXIV");
        if (result == 85)
            System.out.println("Результат: LXXXV");
        if (result == 86)
            System.out.println("Результат: LXXXVI");
        if (result == 87)
            System.out.println("Результат: LXXXVII");
        if (result == 88)
            System.out.println("Результат: LXXXVIII");
        if (result == 89)
            System.out.println("Результат: LXXXIX");
        if (result == 90)
            System.out.println("Результат: XC");
        if (result == 91)
            System.out.println("Результат: XCI");
        if (result == 92)
            System.out.println("Результат: XCII");
        if (result == 93)
            System.out.println("Результат: XCIII");
        if (result == 94)
            System.out.println("Результат: XCIV");
        if (result == 95)
            System.out.println("Результат: XCV");
        if (result == 96)
            System.out.println("Результат: XCVI");
        if (result == 97)
            System.out.println("Результат: XCVII");
        if (result == 98)
            System.out.println("Результат: XCVIII");
        if (result == 99)
            System.out.println("Результат: XCIX");
        if (result == 100)
            System.out.println("Результат: C");
    }

    public static void arab(String operation, List<Integer> paramsIntegerArray) {
        int result = 0;
        //Todo: блоки switch-case надо вынести в отдельный метод
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
                    //здесь можно бросить наше исключение
                    System.out.println("На 0 делить нельзя");
                    break;
                }
        }
        System.out.println("Результат: " + result);
    }
}
