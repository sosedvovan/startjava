package com.urise.webapp;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class MainCollections {

    private static final String UUID_1 = "uuid1"; //созд переменные String
    private static final Resume RESUME_1 = new Resume(UUID_1);//созд объекты для массива collection

    private static final String UUID_2 = "uuid2"; //для @Before
    private static final Resume RESUME_2 = new Resume(UUID_2);

    private static final String UUID_3 = "uuid3"; //static- тк они одинак в каждом методе
    private static final Resume RESUME_3 = new Resume(UUID_3);

    private static final String UUID_4 = "uuid4";
    private static final Resume RESUME_4 = new Resume(UUID_4);



    //-----------------------------------------------------------------------------

    public static void main(String[] args) {

        Collection<Resume> collection = new ArrayList<>();
        collection.add(RESUME_1);
        collection.add(RESUME_2);
        collection.add(RESUME_3);


        /**
          //сделаем итерацию в цикле и добавим условие и получим ошибку:
        for(Resume r : collection){
            System.out.println(r);
            if(Objects.equals(r.getUuid(), UUID_1)){
                collection.remove(r);//попытка изменения коллекции при итерации - будет ошибка:
                //- ConcurrentModificationException -- надо использовать итератор
            }
        }
         */
        //ЧТОБЫ ЧТО ТО МОДИФИЦИРОВАТЬ ИЛИ УДАЛИТЬ В КОЛЛЕКЦИИ НАДО ИСП ИТЕРАТОР:
        //ИТЕРАТОР - это интерфейс, кот имеет 2-а метода: hasNext() и next()

        //теперь сделаем итерацию в итераторе и добавим условие в цикле while:
        Iterator<Resume> iterator = collection.iterator();//переменной iterator присвоили
                                                        //нашу коллекцию
        while (iterator.hasNext()) {//hasNext() - есть ли следующий элемент в коллекции
            Resume r = iterator.next();//next() - перевести курсор и взять следующий элемент в коллекции
            //переменной r присваиваем следующий элемент в итераторе
            //первоначально курсор стоит перед [0] элементом и его надо перевести на него ->[0]
            System.out.println(r);
            if(Objects.equals(r.getUuid(), UUID_1)){
                iterator.remove();//попытка изменения коллекции при итерации - все OK
            }
        }


      /**
        //теперь сделаем итерацию в итераторе и добавим условие в цикле do-while:
        //но в этом случае- если коллекция будет пустая - то выбросится рантайм(в его описании нет throws)  -
        // эксепшн- NoSuchElementException -> цикл while будет лучше

        do {
            Resume r = iterator.next();//next() - перевести курсор и взять следующий элемент в коллекции
            //переменной r присваиваем следующий элемент в итераторе
            //первоначально курсор стоит перед [0] элементом и его надо перевести на него ->[0]

            System.out.println(r);
            if(Objects.equals(r.getUuid(), UUID_1)){
            iterator.remove();//попытка изменения коллекции при итерации - все OK
            }

        }while (iterator.hasNext());//hasNext() - есть ли следующий элемент в коллекции

        */

        System.out.println(collection.toString());

    }
}

/**
 * ConcurrentModificationException- когда одновременно кто-то итерируемся по коллекции
 * а кто-то  патается в ней что-то поменять
 *
 *
 * от интерфейса Collection наследуются интерфейсы Set и List. В Set не может быть дубликатов
 * то если из List надо удалить дубликаты мы делаем List.setAll() и Set.addAll()
 * те Set нужен только для уникальности
 *
 * у Set есть 2-е главные реализации- HashSet и TreeSet.(на основе AbstractSet)
 * у элементов Set нет индексов
 *
 * реализация TreeSet основана на TreeMap а HashSet на HashMap
 * те все происходит на основе двоичного поиска по древовидной структуре
 * те из каждой ветки растет еще две ветки, есть левая сторона и есть правая
 * и с пом лефт и райт мы можем дойти до любого значения
 *
 * от Set наследуется еще SortedSet, кот расширяет Set, и у него собственная реализация TreeSet
 * так же основанная на двоичном поиске
 *
 * то Set отличается от Collection тем что в нем элементы не дублируются
 * а list отличается от Collection тем что в нем появляются индексы
 * те мы можем сказать get(int index), remove(int index).
 *
 * главные реализации List  это ArrayList И LinkedList
 *
 * LinkedList кроме служебных интерфейсов имплементирует еще и Deque - это Queue-очередь,
 * которая может с двух сторон удалять-добавлять. те LinkedList это дву-связанный список-
 * те мы можем идти-итерировать сначала или сконца
 * то LinkedList нам нужен чтобы удалять элементы из середины, тк при этом не надо сдвигать
 * вправо, а просто в соседних элементах перепишутся ссылки
 *
 * в LinkedList в оберточке каждого элемента присутствуют еще 2-е ссылки
 * те если есть 100 элементов, то в LinkedList будет 300-та, а в ArrayList- 100 так и будет
 *
 * ArrayList самая простая структура, внутри её находится простой массив (из 9-ти ячеек)
 * и когда заканчивается место в массиве, то ArrayList автоматически-динамически увеличивает
 * свой внутренний массив((старый * 3) / 2 + 1) путем копирования данных из старого
 * массива в новый.
 * но в ArrayList операции вставки и удаления происходят с пом сдвига, поэтому
 * надо брать LinkedList.
 * то ArrayList используем когда надо просто итерировать по индексу без вставок-удалений.
 *
 *
 * Перейдем к Map. На основе Map устроены Set'ы.( реализация TreeSet основана на TreeMap
 * а HashSet на HashMap)
 * Map<K,V> Map параметризован парой- ключ-значение(ассоциативный массив)-
 * особенность- ключи также уникальны как и у Set'а(поэтому в Set нет дубликатов),
 * по ключу мы можем класть какое то значение.
 * те если по ключу uuid класть Resume, то при update мы просто по этому ключу
 * положим другой объект.
 * Map используется в связке- день(ключ)->события(накапливаем значения)-
 * это для анализа- в какие дни произошли определенные собтия
 * те каждому дню будет соответствовать список событий.
 *
 * в Map пользуются в основном SortedMap и HashMap.
 *
 * от Map наследуется еще SortedMap(сортировка по Ключу?) кот расширяет Map и у него
 * собственная реализация TreeMap так же основанная на двоичном поиске(по аналогии с TreeSet)
 *
 * HashMap реализует интерфейс Map. Это когда порядок не важен, а достаточно ключа и значения.
 *
 * КОНТРАКТ МЕЖДУ ИКВАЛС И ХЕШ-КОД: ЕСЛИ ИКВАЛС ТРУ, ТО И ХЕШ-КОДЫ РАВНЫ, А НАОБОРОТ НЕ ОБЯЗАТЕЛЬНО
 * то если бы это не выполнялось, то структуры основанные на HashMap и HashSet не работалибы
 *
 * CTRL + E - показать последние классы и выбрать один
 */