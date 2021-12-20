package test;

import java.util.*;

public class MapS {
    public static void main(String[] args) {
        Map<Integer, String> hashMap = new HashMap<>();//вывод беспорядочный
        Map<Integer, String> linkedHashMap = new LinkedHashMap<>();//вывод в порядке добавления
        Map<Integer, String> treeMap = new TreeMap<>();//вывод в естественном порядке по ключу

        /**
        //а в Set можно подсунуть свой компаратор:
        Compar compar = new Compar();
        Set<Integer> treeMapComparator = new TreeSet<>(compar);
        //конструктор компаратора, если исп. 2-а компаратора:
        //это если в Set кладем объекты People и написали 2-ф класса Компораторов: один для поля name, другой для поля id:
        //Compar compar = new Compar().thenComparing(new КакойтоВторойКлассКомпоратор);
         */


        mapAdded(hashMap);
        mapAdded(linkedHashMap);
        mapAdded(treeMap);

    }

    private static void mapAdded(Map<Integer, String> map) {
        //ключ должен быть уникальным, а значение может повторяться
        map.put(1, "Tom");
        map.put(78, "Nyrjai");
        map.put(355, "Kolo");
        map.put(19864, "NUYT");
        map.put(2, "Mlfcd");
        map.put(4, "MOPHYTRMH");
        map.put(3, "Tom");

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }


}
//хотел подать Compar в конструктор treeMapComparator, но такое с Map нельзя, a можно с Set
class Compar implements Comparator<Integer>{

    @Override
    public int compare(Integer integer, Integer t1) {
        if(integer > t1){
            return -1;
        }else if(integer < t1){
            return 1;
        } else return 0;
    }
}
