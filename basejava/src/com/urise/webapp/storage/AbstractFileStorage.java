package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
//объекты этого класса являются хранилищами для Резюме в файл, и имеют свои методы, кот помогут сериализации
public abstract class AbstractFileStorage extends AbstractStorage<File>{

    private File directory;//каталог-здесь будут хранится резюме

    //конструктор с проверками:
    protected AbstractFileStorage(File directory){
        //проверка, что не подали нуль
        Objects.requireNonNull(directory, " directory must not be null");
        //проверка, что это является директорией
        if(!directory.isDirectory()){
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        //проверка, что в эту директорию разрешенно чтение-запись
        if(!directory.canRead() || !directory.canWrite()){
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        //конструируем объект:
        this.directory = directory;

    }
    //далее в аргументах File searchKey переименуем в File file (Ctrl+R)

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("File write error", r.getUuid(), e);
        }
    }

    @Override//приходит резюме и сущность файла-уже до этого в isExist проверенно, что его не существует
    protected void doSave(Resume r, File file) {//и надо записать резюме в файл
        try {
            file.createNewFile();//создаем реальный файл
           // doWrite(r, file);//дозаписывание через этот абстрактный метод. перенесли в doUpdate
        } catch (IOException e) {
            //выбросим наш собств эксепшен(добавив там нов конструктор для этих аргументов)
            throw new StorageException("Couldn't create file ", file.getName(), e);
        }
        doUpdate(r, file);
    }

    //throws IOException отлавливается в doUpdate(), кот вызывает этот метод
    protected abstract void doWrite(Resume r, File file) throws IOException;
    protected abstract Resume doRead(File file) throws IOException;


    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("File delete error", file.getName());
        }
    }

    @Override//просто читаем
    protected Resume doGet(File file) {
        try {
            return doRead(file);
        } catch (IOException e) {
            throw new StorageException("File read error", file.getName(), e);
        }
    }

    //прелести шаблонного метода- все проверки(сущест или не существ) уже реализованны
    @Override
    protected boolean isExist(File file) {
        //существует ли файл? У класса File есть для этого свой спец метод
        return file.exists();
    }

    @Override
    protected File getSearchKey(String uuid) {
        //SearchKey будет находится в папке directory и иметь данный uuid
        //возвращает сущность еще не созданного файла(в doSave(), напр)
        return new File(directory, uuid);
    }

    @Override//читаем католог и делаем doGet поочереди в лист и отдаем его
    protected List<Resume> doCopyAll() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Directory read error", null);
        }
        List<Resume> list = new ArrayList<>(files.length);
        for (File file : files) {
            list.add(doGet(file));
        }
        return list;
    }


    @Override
    public void clear() {
        File[] files = directory.listFiles();//взяли список
        if (files != null) {//если он не пустой
            for (File file : files) {//прошлись и удалили
                doDelete(file);
            }
        }
    }

    @Override//вернет кол-во имеющихся резюме
    public int size() {
        String[] list = directory.list();//удобный list() вернет список строк, а не файлов
        if (list == null) {
            throw new StorageException("Directory read error", null);//null вместо uuid подадим тк uuid сейчас тут нет
        }
        return list.length;
    }
}
