package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;
//ТЕПЕРЬ РЕАЛИЗУЕМ СВОЕ ХРАНИЛИЩЕ НА ОСНОВЕ АРРАЙЛИСТА:
//здесь родитель параметризуется <Integer>-ом, тк SearchKey здесь это Integer searchKey(вместо Object searchKey-раньше)
public class ListStorage extends AbstractStorage<Integer> {
    //выберем реализацию хранилища Резюме(аррей или линкед):
    //обычно самая частая операция для Резюме- это get
    //а операция  get самая дешевая в ArrayList:
    private List<Resume> list = new ArrayList<>();

    @Override
    protected void doUpdate(Resume r, Integer searchKey) {
        list.set(searchKey, r);//сетим по индексу обновленное резюме
        //то же что и в dosave(), только разница в проверке-сущ или несущ.
    }

    @Override
    protected void doSave(Resume r, Integer searchKey) {
        // сетим по индексу новое резюме:
        //  list.set((Integer) searchKey, r);
        //searchKey приходит = null ->
        // -> не прошел тест- надо не сетить а добавить:

        list.add(r);
    }

    @Override
    protected void doDelete(Integer searchKey) {
        list.remove((searchKey).intValue());
        //без intValue() remove не сработает. тк в remove надо отправить индекс-число
        //а searchKey это? не число?
    }

    @Override
    protected Resume doGet(Integer searchKey) {
        return list.get(searchKey);//берем по индексу обновленное резюме
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return searchKey != null;//если != null, значит возвратили true если такой объект существует
        //для массива не можем использовать а здесь можем
    }

    //проверяем- есть ли такое Резюме в базе:
    @Override//возвр Integer тк List поддерживает позицию
    protected Integer getSearchKey(String uuid) {
        //метод листа indexOff() не подходит, тк он будет искать
        // совпадения по всем полям, а нам надо только по uuid -> исп цикл:
        // причем, цикл фор-ич не подходит тк в полях Резюме нет индекса -> исп обычный
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getUuid().equals(uuid)){//если есть совпадение по uuid ->
                return i;//-> вернем индекс этого элемента в коллекции ->
            }
        }
        return null;//-> иначе вернем null
    }//в линкед листе пришлось бы делать через итератор и счетчик встроить вручную?

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public List<Resume> doCopyAll() {
        //вернем просто копию листа
        return new ArrayList<>(list);


       // return list.toArray(new Resume[list.size()]);
        //конвертируем лист в массив- есть много способов
        //в этом способе метод toArray() принимает дженерик -new Resume[]
    }

    @Override
    public int size() {
        return list.size();
    }
}
