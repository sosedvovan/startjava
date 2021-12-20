package com.urise.webapp.storage;



//Это вариант массива- хранилища без сортировки внутри.

import com.urise.webapp.model.Resume;
//РЕАЛИЗУЕМ СВОЕ ХРАНИЛИЩЕ НА ОСНОВЕ ОБЫЧНОГО МАССИВА:
//этот класс - дочка доделывает недоделанные в родителе) методы обычного Array присущим обычному Array способом)
//ЦЕПОЧКА НАСЛЕДОВАНИЯ:ЭТОТ КЛАСС extends AbstractArrayStorage КОТОРЫЙ extends AbstractStorage КОТОРЫЙ implements Storage
public class ArrayStorage extends AbstractArrayStorage {//так же implements Storage тк AbstractArrayStorage implements Storage

    //реализовали  метод родительского абстрактного класса (тоже protected, как сигнатура в родительском. но
    // можно расширить до public, а сузить нельзя)

    // для избегания повторения кода, делаем getSearchKey() методом перебора
    // (а в сортед-варианте-методом бинарного деления массива на 2)
    //ЭТОТ МЕТОД- ЧАСТЬ ЛОГИКИ РЕАЛИЗУЕМОЙ В РОДИТЕЛЬСКОМ КЛАССЕ (ШАБЛОННЫЙ ПАТТЕРН)
    //делегированная дочкам часть логики из метода родительского класса(шаблонный петтерн):
    @Override//возвращает индекс элемента в массиве по значению этого элемента. работает методом простого перебора.
    protected Integer getSearchKey(String uuid) {//возвр Integer тк ковариация в его родителе
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        /*
        private int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }
         */
        }
        return -1;//будет автоматический инбоксинг- приведение к Integer -возвращ типу
        //если сделаем явный инбоксинг- Ява подчеркнет желтым- типа не обязательно с Ява 5
        //можно еще и так ретурнуть:
        //return Integer.valueOf(-1);//вместо new Integer лучше писать с  valueOf сказал?
        //ответ: valueOf() создаст новый объект только в том случае если к нему  в аргумент
        //придет число больше 128 и меньше минус 127. а если в этих пределах придет,
        //то он вернет объект из кеша.
        // -1 возвращается если элемент с таким значением отсутствует
    }

    //------------------------------------------------------------------------------------------------------------------

    //ЭТОТ МЕТОД- ЧАСТЬ ЛОГИКИ РЕАЛИЗУЕМОЙ В РОДИТЕЛЬСКОМ КЛАССЕ (ШАБЛОННЫЙ ПАТТЕРН)
    /*здесь без сортировки:

     */
    @Override//удалим элемент из массива-поместив на его место крайний элемент массива
    protected void fillDeletedElement(int index) {
        storage[index] = storage[size - 1];
    }
     /* вариант реализации автора курса- эдентичен почти
          public void save(Resume r) {
        if (getIndex(r.getUuid()) != -1) {
            System.out.println("Resume " + r.getUuid() + " already exist");
        } else if (size >= storage.length) {
            System.out.println("Storage overflow");
        } else {
            storage[size] = r;
            size++;
        }
    }
         */
    //вариант моей реализации:
    // for (int i = 0; i < size; i++) {//чтобы обновить ячейку c Resume сначала найдем ее в цикле for
    //     if (!r.getUuid().equals(storage[i].getUuid())) {//в массиве storage лежат объекты Resume -> они вызывают свой геттер
    //        storage[size] = r;
    //        size++;
    //    }
    // }

    //------------------------------------------------------------------------------------------------------------------

    //ЭТОТ МЕТОД- ЧАСТЬ ЛОГИКИ РЕАЛИЗУЕМОЙ В РОДИТЕЛЬСКОМ КЛАССЕ (ШАБЛОННЫЙ ПАТТЕРН)
    //ЭТОТ МЕТОД- доделывает) родительский метод doSave(), в котором только проверки.
    @Override//в этом классе (обычного Array) записывает пришедшее Резюме в крайнюю ячейку след. способом:
    protected void insertElement(Resume r, int index) {
        storage[size] = r;
    }
}


// Теория: Использование ArrayList для удаления элемента из обычного массива:
  /*   Если мы не можем удалить элемент в обычном массиве,
        мы можем преобразовать массив в структуру, позволяющую удалять элементы. А потом преобразовать эту структуру
        обратно в массив.
        Выполнить вышеописанную схему нам поможет java.util.List или ArrayList. Дело в том, что в ArrayList реализован
        специальный метод, позволяющий удалять элементы — remove. В общем виде всё выглядит так:
        String[] array = new String[]{"foo", "bar", "baz"};
        List<String> list = new ArrayList<>(Arrays.asList(array));
        list.remove("foo");
        array = list.toArray(new String[list.size()]);
        System.out.println(Arrays.toString(array));
        */
