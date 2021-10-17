package lesson_3;

import java.util.Scanner;

public class arrayMax {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Введите желаемую длину массива: ");
        int massLength = scan.nextInt();
        int[] mass = new int[massLength];
        System.out.print("Заполните массив целыми числами через пробел: ");
        for(int i = 0; i < mass.length; i++) {
            mass[i] = scan.nextInt();
        }

        //теперь найдем максимальное число в массиве.
        //предположим сначала, что максимальное число лежит в ячейке с индексом 0:
        int max = mass[0];
        //далее в цикле будем сравнивать каждую ячейку массива с последующей и если последующая больше
        //предидущей будем присваивать переменной max значение последующей.
        for(int i = 0; i < mass.length - 1; i++) {
            if(max < mass[i + 1]){
                max = mass[i + 1];
            }
        }

        System.out.print("Максимальное число в массиве: " + max);
    }
}
//        метод для копирования массивов:
//        public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
//        Копирование происходит из массива src, начиная с позиции srcPos, в массив dest, начиная с позиции destPos.
//        Всего копируется length элементов. Обратите внимание, что src и dest имеют тип Object.
//        Это сделано для того, чтобы этот метод мог обрабатывать массивы любого типа. Если src
//        или dest не является массивом, будет инициировано исключение java.lang.ArrayStoreException.
