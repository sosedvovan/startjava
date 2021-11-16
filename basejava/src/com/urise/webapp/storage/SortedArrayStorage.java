package com.urise.webapp.storage;



import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

//на основе сортированного массива, чтобы большое О стало не линейным(перебор всего массива)
//а большое О стало от логарифма с основанием 2(бинарный поиск в массиве методом деления на 2)
//этот класс implements Storage тк его родительский AbstractArrayStorage implements Storage
//РЕАЛИЗУЕМ СВОЕ ХРАНИЛИЩЕ НА ОСНОВЕ ОБЫЧНОГО МАССИВА С ИСПОЛЬЗОВАНИЕМ МЕТОДА ДВОИЧНОГО ПОИСКА:
public class SortedArrayStorage extends AbstractArrayStorage {

    //Внедрим это поле чтобы обращаться к нему из метода.
    //ПОЛЕ: это объект (анонимного (уже) класса Comparator) только мы написали его как лямбду
    //Будем использовать этот объект в методе бинарного поиска Arrays.binarySearch()
    //В этот объект заложим способ сравнивания 2-х объектов Резюме между собой таким способом, как нам надо
    private static final Comparator<Resume> RESUME_COMPARATOR = ( o1, o2 ) -> o1.getUuid().compareTo(o2.getUuid());


    //ЭТОТ МЕТОД- ЧАСТЬ ЛОГИКИ РЕАЛИЗУЕМОЙ В РОДИТЕЛЬСКОМ КЛАССЕ (ШАБЛОННЫЙ ПАТТЕРН)
    //реализовали  метод родительского абстрактного класса (тоже protected, как сигнатура в родительском. но
    // можно здесь расширить до public, а сузить нельзя
    // для избегания повторения кода. осуществляет поиск элемента по значению методом деления на 2
    //возвращает индекс элемента в массиве по значению этого элемента
    //если элемент не найден- возвращает любое отрицательное число
    @Override
    protected Integer getSearchKey(String uuid) {//принимает значение поля, возвращает индекс или null если не найденно
        Resume searchKey = new Resume(uuid, "dummy" );//создаем новый объект-клон(пока в Resume 1-о поле)
        //"dummy" -фиктивное, тк binarySearch() сравнивает только по uuid

        //сеттер удалим тк стали использовать конструктор
        //searchKey.setUuid(uuid);//новому объекту присваеваем искомое значение поля из аргументов метода

        return Arrays.binarySearch(storage, 0, size, searchKey, RESUME_COMPARATOR);
        //[вместо RESUME_COMPARATOR сначала ставили new ResumeComparator но потом переиграли]
        //где Arrays.binarySearch() уже имеющийся в java метод класса  Arrays для нахождения индекса по значению
        //методом деления на 2. на вход надо подать наш массив, интервал поиска(0-size) и объект содержащий
        //искомое значение-uuid
        //причем storage должен быть отсортирован и по uuid должен быть переопределен equals(в Resume)
        // чтобы сравнивать класс Resume должен implements Comparable<Resume>(изменили на Comparator)
        //если убрать implements Comparable<Resume> с Resume- тест завалится с
        //эксепшеном- что я не могу Resume преобразовать в Comparable - ClassCastException
        //Comparable- значит что объект может сравниться сам с собой
        //Comparator-отдельный в стороночке класс, кот может сравнивать 2-а объекта
        //и методу Arrays.binarySearch() нужно чтобы Resume implements Comparable<Resume>
        //те массив у которого элементы Comparable
        //или сторонний класс Comparator, кот позволяет сравнивать 2-а обекта
        /**
         * если убрать implements Comparable<Resume> с Resume:
         * тогда как следствие в методе getSearchKey() не сработает Arrays.binarySearch
         * чтобы это исправить реализуем внутренний статический класс Comparator<Resume> -
         * (static значит - без скрытой ссылки на внешний класс):
         *
         * private static class ResumeComparator implements Comparator<Resume>{
         * @Override
         * public int compare(Resume o1, Resume o2){
         * return o1.getUuid().compareTo(o2.getUuid());
         *  }
         * }
         * где метод compareTo() сравнивает строки в лексикографическом порядке
         *
         * а в теле метода getSearchKey() [-раннее-getIndex()] в аргументах метода Arrays.binarySearch()
         * добавим объект этого внутреннего класса ResumeComparator :
         * return Arrays.binarySearch(storage, 0, size, searchKey, new ResumeComparator);
         *
         * чтобы каждый раз при вызове метода getSearchKey() [-раннее-getIndex()] не создавать new ResumeComparator
         * вынесем его в константы в полях этого класса:
         * private static final ResumeComparator RESUME_COMPARATOR = new ResumeComparator();
         * и заменим в теле метода getIndex():
         * return Arrays.binarySearch(storage, 0, size, searchKey, RESUME_COMPARATOR);
         *
         * получается, что мы создаем вложенный класс чтобы создать только один объект : new ResumeComparator.
         * мы можем сократить код: объединив создание константы RESUME_COMPARATOR с анонимным классом:
         *
         * private static final Comparator<Resume> RESUME_COMPARATOR = new Comparator<Resume>(){
         * @Override
         * public int compare(Resume o1, Resume o2){
         * return o1.getUuid().compareTo(o2.getUuid());
         * }
         * }
         * тк мы удалили класс ResumeComparator, соответственно тип RESUME_COMPARATOR
         * изменили на Comparator<Resume>.
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

    //ЭТОТ МЕТОД- ЧАСТЬ ЛОГИКИ присущей обычному SortedArray РЕАЛИЗУЕМОЙ В РОДИТЕЛЬСКОМ КЛАССЕ (ШАБЛОННЫЙ ПАТТЕРН)
    //УДАЛЯЕТ элемент из отсортированного массива НЕ ЛОМАЯ СОРТИРОВКИ
    //те сдвигает хвост влево
    @Override
    protected void fillDeletedElement(int index) {//принимает индекс удаляемого элемента
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(storage, index +1, storage, index, numMoved);
            //где storage -массив из которого копируем
            //index +1  индекс с которого начнем копирование
            //storage -массив в который копируем
            //index  индекс в принимающем массиве с которого начнется вставка элементов (его мы и удаляем)
            //numMoved  колличество элементов, которое необходимо перенести
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    //ЭТОТ МЕТОД- ЧАСТЬ ЛОГИКИ присущей обычному SortedArray РЕАЛИЗУЕМОЙ В РОДИТЕЛЬСКОМ КЛАССЕ (ШАБЛОННЫЙ ПАТТЕРН)
    //ДОБАВЛЯЕТ элемент в отсортированный массив НЕ ЛОМАЯ СОРТИРОВКИ
    //те сдвигает хвост вправо и вставляет в нужное место
    //доделывает метод doSave() родитеся способом, присущем сортированному обычному массиву
    //https://codereview.stackexchange.com/questions/36221/binary-search-for-inserting-in-array
    @Override
    protected void insertElement(Resume r, int index) {
        int insertIdx = -index - 1;//индекс сделали отрицательным?
        System.arraycopy(storage, insertIdx, storage, insertIdx + 1, size - insertIdx);
        storage[insertIdx] = r;
        //где storage -массив из которого копируем
        //insertIdx  индекс с которого начнем копирование
        //storage -массив в который копируем
        //insertIdx + 1  индекс в принимающем массиве с которого начнется вставка элементов
        //size - insertIdx колличество элементов, которое необходимо перенести
    }
}
