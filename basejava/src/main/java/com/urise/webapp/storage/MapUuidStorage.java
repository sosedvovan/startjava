package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.*;

//ТЕПЕРЬ РЕАЛИЗУЕМ СВОЕ ХРАНИЛИЩЕ НА ОСНОВЕ МАПЫ причем ключ- это uuid:
//здесь родитель параметризуется <String>-ом, тк SearchKey здесь это String uuid
//помним, что key в Мапе и SearchKey это разные вещи (но сдесь они совпадают по смыслу : Map<String, Resume> map)
//напомню, что по SearchKey мы находим элементы в хранилищах, котрые хотим удалить, добавить, взять итд
//те getSearchKey - главный метод для определения индекса или ключа элемента хранилища над кот мы хотим произвести действия
public class MapUuidStorage extends AbstractStorage<String> {

    //в случае, если интерфейс Storage возьмет эту реализацию (Map- хранилища)
    //то в это реализации в качестве хранилища используем HashMap:
    private Map<String, Resume> map = new HashMap<>();

    @Override
    protected void doUpdate(Resume r, String uuid) {
        map.put(uuid, r);
    }

    @Override
    protected void doSave(Resume r, String uuid) {
        map.put(uuid, r);
    }

    @Override
    protected void doDelete(String uuid) {
        map.remove(uuid);
    }

    @Override
    protected Resume doGet(String uuid) {
        return map.get(uuid);
    }

    @Override
    protected boolean isExist(String uuid) {
        return map.containsKey(uuid);
    }

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }//что пришло, то и вернули

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public List<Resume> doCopyAll() {
        //values()возвр колллекцию и мы делаем ее копию
        return new ArrayList<>(map.values());


        //вернем пока пустой готовый лист из Collections:
        //return Collections.emptyList();
    }

    @Override
    public int size() {
        return map.size();
    }
}
