package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Todo: Подумайте, что еще, может выступать в качестве search key (в предыдущем дз это был uuid)
// в реализации на основе Map (не путайте key и search key: key - это ключ в мапе,
// а search key - используется для поиска по мапе). Выразите свои идеи в коде
// (в итоге у вас в проекте должны быть два класса, реализованных на основе мапы)

//ТЕПЕРЬ РЕАЛИЗУЕМ СВОЕ ХРАНИЛИЩЕ НА ОСНОВЕ МАПЫ причем ключ- это само Резюме:
//здесь родитель параметризуется <Resume>-ом, тк SearchKey здесь это Resume resume(вместо Object resume-раньше)
public class MapResumeStorage extends AbstractStorage<Resume> {

    private Map<String, Resume> map = new HashMap<>();

    @Override
    protected void doUpdate(Resume r, Resume resume) {
        map.put(r.getUuid(), r);
    }

    @Override
    protected void doSave(Resume r, Resume resume) {
        map.put(r.getUuid(), r);
    }

    @Override
    protected void doDelete(Resume resume) {
        map.remove((resume).getUuid());
    }

    @Override
    protected Resume doGet(Resume resume) {
        return resume;
    }

    @Override
    protected boolean isExist(Resume resume) {
        return resume != null;
    }

    @Override//ключ это Resume в этой мапе
    protected Resume getSearchKey(String uuid) {
        return map.get(uuid);
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
