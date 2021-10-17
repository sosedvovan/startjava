package lesson_3;

import java.util.Arrays;

public class ArraysBuble{

    public static void main(String[] args) {

        int [] mas = {11, 3, 14, 16, 7};

        boolean isSorted = false;//флажок того, отсортирован ли уже массив или нет.
        int buf;//переменная buf пригодится при обмене
        while(!isSorted) {//Так, как мы не знаем, сколько циклов нужно сделать
            isSorted = true;//изначально мы предполагаем, что массив отсортирован
            for (int i = 0; i < mas.length-1; i++) {
                if(mas[i] > mas[i+1]){
                    isSorted = false;//если массив уже отсортирован, то в это тело if уже не входим -> isSorted = true
                                        //и выходим из while.

                    buf = mas[i];
                    mas[i] = mas[i+1];
                    mas[i+1] = buf;
                }
            }
        }
        System.out.println(Arrays.toString(mas));//обращаемся к статическому методу класса Arrays
    }
}
