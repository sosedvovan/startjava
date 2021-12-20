package test;

//создаем полиморфный массив + обходим форичем чз лямду
//List<Animal> animals = Arrays.asList(new Cat(), new Dog(), new Cat());
//animals.forEach(animal -> animal.makeSound());


import javax.swing.*;
import java.util.Arrays;

public class MorzeDeshefration {
    public static void main(String[] args) {
        String morz = "... --- ...   o t ... --- ...";

        //разделяем строку morz по словам и каждое слово заносим в массив String[] mass отдельным элементом
        String[] mass = morz.split("   ");
        System.out.println("Строка морзе, которую надо расшифровать: " + Arrays.toString(mass));


        //в цикле берем слово № i из String[] mass, разбиваем на буквы, эти буквы помещаем в массив String[] myWorld,
        //отдельными элементами. Массив со словом № i отправляем в метод myPrintPlus().
        for(int i = 0; i < mass.length; i++){
            String[] myWorld = mass[i].split(" ");
            System.out.println("Расшифровываем это слово:  ");
            System.out.println(Arrays.toString(myWorld ));
            myPrintPlus(myWorld );
            System.out.println();
        }
    }

    //принимаем массив String[] myWorld в котором зашифрованное слово разложенно на отдельные буквы по элементам массива
    //в цикле, берем каждую букву по очереди и отправляем в метод deshefrator(), который вернет дешефрованную букву,
    // и выводим расшифрованные буквы в консоль.
    private static void myPrintPlus(String[] myWorld){

        for (int i = 0; i < myWorld.length; i++) {
            String lette = myWorld[i];
            String x = deshefrator(lette);
            System.out.print(x);
        }
    }

    private static String deshefrator(String s) {
        switch (s){
            case "..." :
                return "S";
            case "---" :
                return "0";
            default:
                return s;
        }
    }

}
