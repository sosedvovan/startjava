package test;

import java.util.*;

public class TestComparableArraySet {
    public static void main(String[] args) {
        List<Klient> listKlients = new ArrayList<>();//создали коллекцию Array с объектами Klient
        Set<Klient> setKlient = new TreeSet<>();//создали коллекцию TreeSet с объектами Klient
        //в TreeSet объекты сортируются при попадании туда, но для этого надо задать способ сортировки след образом:
        //класс Klient должен implements Comparable<Klient> и переопределить метод compareTo()


        //отправили коллекции на заполнение в addInCollections()
        addInCollections(listKlients);
        addInCollections(setKlient);

        System.out.println(listKlients + "Array до сортировки");//выведет в порядке добавления

        Collections.sort(listKlients);//метод sort() учитывает Comparable и ArrayList  отсортируется
        //в соответствии с ним(Comparable)
        // а в TreeSet происходит сортировка при добавлении(ели бы Klient не implements Comparable<Klient>,
        //то при попытке добавления - РАНТАЙМ ЕКСЕПШЕН
        //hashCod() в классе Klient я не переопределил- хотя советуют)


        //посмотрим что в наших коллекциях получилось:
        System.out.println(listKlients);//здесь естественный порядок Java с
        System.out.println(setKlient);

    }

    public static void addInCollections (Collection collection){
        collection.add(new Klient(7, "Typomina"));
        collection.add(new Klient(3, "Tily"));
        collection.add(new Klient(1, "Ty"));
        collection.add(new Klient(5, "Typomi"));
        collection.add(new Klient(6, "Typomin"));
        collection.add(new Klient(2, "Til"));
        collection.add(new Klient(4, "Tylip"));
    }

}

//--------------------------------------------------------------------------------------------------

class Klient implements Comparable<Klient> {
    int id;
    String name;

    public Klient(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Klient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Klient klient) {
        if (this.getName().length() > klient.getName().length()) {
            return 1;
        }else if(this.getName().length() < klient.getName().length()) {
            return -1;
        } else {
            return 0;
        }
    }
}
