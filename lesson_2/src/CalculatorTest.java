import Model.Calculator;

import java.util.Scanner;

public class CalculatorTest{

    public static void main(String[] args){

        System.out.println("Calc START!");

        String answer = "yes";
        while (answer.equals("yes")) {
            Scanner scan = new Scanner(System.in);
            Calculator calc = new Calculator();

            System.out.print("Input first number: ");
            int firstNumber = scan.nextInt();
            calc.setFirstNumber(firstNumber);

            System.out.print("Input arithmetic operation: ");
            char calcOperation = scan.next().charAt(0);
            calc.setCalcOperation(calcOperation);


            System.out.print("Input second number: ");
            int secondNumber = scan.nextInt();
            calc.setSecondNumber(secondNumber);

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