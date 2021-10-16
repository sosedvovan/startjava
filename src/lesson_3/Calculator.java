package lesson_3;

public class Calculator {

    private int firstNumber;
    private char calcOperation;
    private int secondNumber;

    public void setFirstNumber(int firstNumber) {
        this.firstNumber = firstNumber;
    }

    public void setCalcOperation(char calcOperation) {
        this.calcOperation = calcOperation;
    }

    public void setSecondNumber(int secondNumber) {
        this.secondNumber = secondNumber;
    }

    public void calculat() {

        switch (calcOperation) {

            case '+':
                System.out.println(firstNumber + secondNumber);
                break;

            case '-':
                System.out.println(firstNumber - secondNumber);
                break;

            case '*':
                System.out.println(firstNumber * secondNumber);
                break;

            case '/':
                System.out.println(firstNumber / secondNumber);
                break;

            case '%':
                System.out.println(firstNumber % secondNumber);
                break;

            case '^':
                pov();
                break;

            default:
                System.out.println("Error");
        }

    }

    private void pov() {
        int result = 1;
        for(int i = 1; i <= secondNumber; i++){
            result = result * firstNumber;
        }
    }

    @Override
    public String toString() {
        return "Calculator{" +
                "firstNumber=" + firstNumber +
                ", calcOperation=" + calcOperation +
                ", secondNumber=" + secondNumber +
                '}';
    }
}