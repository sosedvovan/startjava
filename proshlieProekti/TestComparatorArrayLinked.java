package test;

import java.util.*;

public class TestComparatorArrayLinked {
    public static void main(String[] args) {
        //создали и инициализировали массив стрингов:
        List<String> listString = new ArrayList<>();
        listString.add("Nik");
        listString.add("Doki");
        listString.add("Bench");
        listString.add("Katyuer");
        listString.add("Go");

        //в массиве стрингов перерелизовали его естественный порядок
        //теперь Collections.sort() работает не по алфавиту, а по длине стринга
        //Comparator<String>() реализовали через анонимный класс
        Collections.sort(listString, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                if (s.length() > t1.length()) {
                    return 1;
                }else if (s.length() < t1.length()) {
                    return -1;
                }else {
                    return 0;
                }
            }
        });
        System.out.println(listString);

        //создали и инициализировали массив стрингов:
        List<Integer> listInteger = new ArrayList<>();
        listInteger.add(1);
        listInteger.add(4);
        listInteger.add(3);
        listInteger.add(2);
        listInteger.add(5);

        //в массиве интенжеров перерелизовали его естественный порядок
        //теперь Collections.sort() работает не по величине значения в сторону возрастания,
        //а в сторону убывания
        //Comparator<String>() реализовали черезобычный  класс:
        // class BackIntegerComporator implements Comparator<Integer>{...} см ниже
        Collections.sort(listInteger, new BackIntegerComparator());
        System.out.println(listInteger);

        //создали ArrayList<People> и инициализировали его,
        // класс People реолизовали ниже
        List<People> listPeople = new ArrayList<>();

        listPeople.add(new People(5,"Tomas"));
        listPeople.add(new People(1,"T"));
        listPeople.add(new People(4,"Tomi"));
        listPeople.add(new People(3,"Tom"));
        listPeople.add(new People(2,"Tj"));

        System.out.println(listPeople + " не сортирован это аррей");//посмотрели на неотсортированный массив

        //с пом анонимного класса определили порядок сортировки в массиве List<People> listPeople:
        Collections.sort(listPeople, new Comparator<People>() {
            @Override
            public int compare(People people, People people2) {
                if (people.getName().length() > people2.getName().length()){
                    return 1;
                }else if (people.getName().length() < people2.getName().length()) {
                    return -1;
                }else {
                    return 0;
                }
            }
        });
        System.out.println(listPeople + " сортирован, это аррей");//показали отсортированный массив People-ов
        System.out.println(listPeople.getClass());//listPeople  это ArrayList

       LinkedList<People> listPeople2 = new LinkedList<>(listPeople);//созд нов LinkedList и в его конструктор
        //отправили ArrayList listPeople
        System.out.println(listPeople2 + " сортирован, это линкед");//вся структура из ArrayList скопировалось в LinkedList
        System.out.println(listPeople2.getClass());//проверили, что listPeople2 это LinkedList

        listPeople = listPeople2;//ArrayList присвоили LinkedList

        System.out.println(listPeople + " сортирован это линкед который раньше был арреем");
        System.out.println(listPeople.getClass());//теперь listPeople это LinkedList

    }
}

class BackIntegerComparator implements Comparator<Integer> {

    @Override
    public int compare(Integer t1, Integer t2) {
        if (t1 > t2) {
            return -1;
        }else if (t1 < t2) {
            return 1;
        }else {
            return 0;
        }
    }
}

class People {
    int id;
    String name;

    public People(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "People{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

