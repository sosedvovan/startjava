package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO: Завершите решение так, чтобы оно разбило строку на пары из двух символов.
// Если строка содержит нечетное количество символов, то она должна заменить отсутствующий
// второй символ последней пары символом подчеркивания ('_').

public class Test2String {

    public static void main(String[] args) {

        //другой способ:
        System.out.println(Arrays.toString("12345678912345".split("(?<=\\G.{2})")));

        System.out.println(Arrays.toString(solution("Мишаесткотлету1")));

    }

    public static String[] solution(String s) {

        String bottomLine = "_";
        String newS;

        if (s.length() % 2 != 0) {
            newS = s + bottomLine;
        } else {
            newS = s;
        }


        List<String> SS = new ArrayList<>();
      //  int k = 0;  //счетчик для индексов  массива - нужен если в обычный массив будем добавляем
        for (int i = 0; i < newS.length() - 1; i = i + 2) { // проход по массиву через каждые 2 символа для нахождения новой подстроки
            String S1 = newS.substring(i, i + 2); // нахождение подстроки с длиной в 2 символа
            SS.add(S1); // присваивание  подстроки к элементу массива Array SS
        }


        int chet = SS.size();// перенесем Array SS в обычный массив, тк на надо вернуть обычный
        String[] mass = new String[chet];
        for (int i = 0; i < chet; i++) {
            mass[i] = SS.get(i);
        }
        System.out.println(Arrays.toString(mass)); //распечатаем обычный массив

        for (String st : SS) {//распечатаем Array SS
            System.out.print(st + " ");
        }

        System.out.println("/n");
        return mass;
    }
}

