package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.*;

//ТЕПЕРЬ РЕАЛИЗУЕМ СВОЕ ХРАНИЛИЩЕ НА ОСНОВЕ МАПЫ:
public class MapUuidStorage extends AbstractStorage {

    private Map<String, Resume> map = new HashMap<>();

    @Override
    protected void doUpdate(Resume r, Object uuid) {
        map.put((String) uuid, r);
    }

    @Override
    protected void doSave(Resume r, Object uuid) {
        map.put((String) uuid, r);
    }

    @Override
    protected void doDelete(Object uuid) {
        map.remove((String) uuid);
    }

    @Override
    protected Resume doGet(Object uuid) {
        return map.get((String) uuid);
    }

    @Override
    protected boolean isExist(Object uuid) {
        return map.containsKey((String) uuid);
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
