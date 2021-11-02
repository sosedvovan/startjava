package com.urise.webapp.storage;



import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

//на основе сортированного массива, чтобы большое О стало не линейным(перебор всего массива)
//а большое О стало от логарифма с основанием 2(бинарный поиск в массиве методом деления на 2)
//этот класс implements Storage тк его родительский AbstractArrayStorage implements Storage

public class SortedArrayStorage extends AbstractArrayStorage {

    private static final Comparator<Resume> RESUME_COMPARATOR = ( o1, o2 ) -> o1.getUuid().compareTo(o2.getUuid());


    //ЭТОТ МЕТОД- ЧАСТЬ ЛОГИКИ РЕАЛИЗУЕМОЙ В РОДИТЕЛЬСКОМ КЛАССЕ (ШАБЛОННЫЙ ПАТТЕРН)
    //реализовали  метод родительского абстрактного класса (тоже protected, как сигнатура в родительском. но
    // можно расширить до public, а сузить нельзя)
    // для избегания повторения кода. осуществляет поиск элемента по значению методом деления на 2
    //возвращает индекс элемента в массиве по значению этого элемента
    //если элемент не найден- возвращает любое отрицательное число
    @Override
    protected int getIndex(String uuid) {//получаем значение поля
        Resume searchKey = new Resume(uuid);//создаем новый объект

        //сеттер удалим тк стали использовать конструктор
        //searchKey.setUuid(uuid);//новому объекту присваеваем искомое значение поля из аргументов метода

        return Arrays.binarySearch(storage, 0, size, searchKey, RESUME_COMPARATOR);
        //где Arrays.binarySearch() уже имеющийся в java метод класса  Arrays для нахождения индекса по значению
        //методом деления на 2. на вход надо подать наш массив, интервал поиска(0-size) и объект содержащий
        //искомое значение-uuid
        //причем storage должен быть отсортирован и по uuid должен быть переопределен equals(в Resume)
        // чтобы сравнивать класс Resume должен implements Comparable<Resume>
        //если убрать implements Comparable<Resume> с Resume- тест завалится с
        //эксепшеном- что я не могу Resume преобразовать в Comparable - ClassCastException
        //Comparable- значит что объект может сравниться сам с собой
        //Comparator-отдельный в стороночке класс, кот может сравнивать 2-а объекта
        //и методу Arrays.binarySearch() нужно чтобы Resume implements Comparable<Resume>
        //те массив у которого элементы Comparable
        //или сторонний класс Comparator, кот позволяет сравнивать 2-а обекта
        /**
         * если убрать implements Comparable<Resume> с Resume:
         * тогда реализуем внутренний статический класс -
         * (без скрытой ссылки на внешний класс) после метода getIndex():
         *
         * private static class ResumeComparator implements Comparator<Resume>{
         * @Override
         * public int compare(Resume o1, Resume o2){
         * return o1.getUuid().compareTo(o2.getUuid());
         *  }
         * }
         *
         * а в теле метода getIndex() в аргументах метода Arrays.binarySearch()
         * добавим объект этого внутреннего класса ResumeComparator :
         * return Arrays.binarySearch(storage, 0, size, searchKey, new ResumeComparator);
         *
         * чтобы каждый раз при вызове метода getIndex() не создавать new ResumeComparator
         * вынесем его в константы в полях этого класса:
         * private static final ResumeComparator RESUME_COMPARATOR = new ResumeComparator;
         * и заменим в теле метода getIndex():
         * return Arrays.binarySearch(storage, 0, size, searchKey, RESUME_COMPARATOR);
         *
         * получается, что мы создаем вложенный класс чтобы создать только один объект : new ResumeComparator
         * мы можем сократить код: объединив создание константы RESUME_COMPARATOR с анонимным классом:
         *
         * private static final Comparator<Resume> RESUME_COMPARATOR = new Comparator<Resume>(){
         * @Override
         * public int compare(Resume o1, Resume o2){
         * return o1.getUuid().compareTo(o2.getUuid());
         * }
         * }
         * тк мы убрали класс ResumeComparator, соответственно при объявлении RESUME_COMPARATOR
         * тип ResumeComparator изменили на Comparator<Resume>.
         *
         * то образом Ява создаст класс без названия(анонимный) и один объект этого класса
         * с сылкой RESUME_COMPARATOR. при обращении по этой ссылки запускается метод
         * этого анонимного класса, возвращающий индекс элемента по значению его поля,
         * или если элемент не найден возвращается отрицательное число
         *
         * пойдем дальше:
         * new Comparator<Resume>() будет серенького цвета-> Идеа хочет оптимизировать код:
         * Alt+Enter -> преобразует в лямда выражение:
         *
         * private static final Comparator<Resume> RESUME_COMPARATOR =
         * ( o1, o2 ) -> o1.getUuid().compareTo(o2.getUuid());
         *
         * те мы принимаем ( o1, o2 ) и возвращаем o1.getUuid().compareTo(o2.getUuid())
         *
         * лямда выражение можно вернуть к обычному виду- стать курсором на ->
         * и выбрать Replace lambda with anonymous class
         *
         * те если есть интерфейс с единственным методом, который мы можем реализовать,
         * то мы можем заменить его на лямбду
         *
         * лямбда реализована внутри JVM и мы не можем увидить ее реализацию
         * лямбда потребляет меньше ресурсов, чем анонимный класс
         *
         * если Идеа подчеркнула лямбду желтым -> Alt+Enter и возможна дальнейшая оптимизация
         */

    }

    //------------------------------------------------------------------------------------------------------------------

    //ЭТОТ МЕТОД- ЧАСТЬ ЛОГИКИ РЕАЛИЗУЕМОЙ В РОДИТЕЛЬСКОМ КЛАССЕ (ШАБЛОННЫЙ ПАТТЕРН)
    //удаляет элемент из отсортированного массива не ломая сортировку
    //те сдвигает хвост влево
    @Override
    protected void fillDeletedElement(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(storage, index +1, storage, index, numMoved);
            //где storage -массив из которого копируем
            //index +1  индекс с которого начнем копирование
            //storage -массив в который копируем
            //index  индекс в принимающем массиве с которого начнется вставка элементов
            //numMoved  колличество элементов, которое необходимо перенести
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    //ЭТОТ МЕТОД- ЧАСТЬ ЛОГИКИ РЕАЛИЗУЕМОЙ В РОДИТЕЛЬСКОМ КЛАССЕ (ШАБЛОННЫЙ ПАТТЕРН)
    //добавляет элемент в отсортированный массив не ломая сортировку
    //те сдвигает хвост вправо и вставляет в нужное место
    //https://codereview.stackexchange.com/questions/36221/binary-search-for-inserting-in-array
    @Override
    protected void insertElement(Resume r, int index) {
        int insertIdx = -index - 1;
        System.arraycopy(storage, insertIdx, storage, insertIdx + 1, size - insertIdx);
        storage[insertIdx] = r;
        //где storage -массив из которого копируем
        //insertIdx  индекс с которого начнем копирование
        //storage -массив в который копируем
        //insertIdx + 1  индекс в принимающем массиве с которого начнется вставка элементов
        //size - insertIdx колличество элементов, которое необходимо перенести
    }
}
