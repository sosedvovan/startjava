package lesson_3;

import java.util.Scanner;

public class CalculatorTest{

    public static void main(String[] args){

        System.out.println("Calc START!");

        String answer = "yes";
        while (answer.equals("yes")) {
            Scanner scan = new Scanner(System.in);
            Calculator calc = new Calculator();

            System.out.print("Input first number (int): ");
            if (scan.hasNextInt()){// возвращает истинну если с потока ввода можно считать целое число
                int firstNumber = scan.nextInt();// считывает целое число с потока ввода и сохраняем в переменную
                calc.setFirstNumber(firstNumber);
            }else
            {System.out.println("Error: not int!");
                continue;//прервет данный виток цикла while и запустит новый виток сначала
            }


            System.out.print("Input arithmetic operation: ");
            char calcOperation = scan.next().charAt(0);//из того, что вернет next() - charAt(0) возьмет первый символ
            calc.setCalcOperation(calcOperation);



            System.out.print("Input second number (int): ");
            if(scan.hasNextInt()) {
                int secondNumber = scan.nextInt();
                calc.setSecondNumber(secondNumber);
            }else {
                System.out.println("Error: not int!");
                continue;//прервет данный виток цикла while и запустит новый виток сначала
            }





            System.out.println(calc);

            calc.calculat();

            do{//пока ответ не "yes" или не "no" (пользователь вводит лабуду)- будет выводится "You want continuum? [yes/no]"
                System.out.println("You want continuum? [yes/no]");
                answer = scan.next();
            } while (!answer.equals("yes") && !answer.equals("no"));
        }

        System.out.println("Calc STOP!");

    }
}