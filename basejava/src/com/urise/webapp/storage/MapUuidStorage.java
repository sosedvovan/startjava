package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.*;

//ТЕПЕРЬ РЕАЛИЗУЕМ СВОЕ ХРАНИЛИЩЕ НА ОСНОВЕ МАПЫ причем ключ- это uuid:
//здесь родитель параметризуется <String>-ом, тк SearchKey здесь это String uuid
public class MapUuidStorage extends AbstractStorage<String> {

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
    }

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
