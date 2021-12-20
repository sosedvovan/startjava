//Todo Реализовать ObjectStreamPathStorage (через java.nio.file.Path) и добавить ObjectStreamPathStorageTest
// хорошая статья про интерфейс Path и его утильный класс Files:
// https://javarush.ru/groups/posts/2275-files-path

package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializer.StreamSerializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    //помним, что Path это усовершенствованный File
    private Path directory;

    private StreamSerializer streamSerializer;

    protected PathStorage(String dir, StreamSerializer streamSerializer) {
        Objects.requireNonNull(dir, "directory must not be null");

        this.streamSerializer = streamSerializer;
        directory = Paths.get(dir);
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    public void clear() {
        getFilesList().forEach(this::doDelete);
    }

    @Override
    public int size() {// в Path появился удобный метод count()

        return (int) getFilesList().count();
        //в видео в блоке трай-кэтч эта строка, видимо перенесли в getFilesList()
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);//один путь склеим с другим
    }

    @Override
    protected void doUpdate(Resume r, Path path) {
        try {
            streamSerializer.doWrite(r, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path write error", r.getUuid(), e);
        }
    }

    @Override//существует ли файл?
    protected boolean isExist(Path path) {
        return Files.isRegularFile(path);
    }

    @Override//сначала создаем подом doUpdate
    protected void doSave(Resume r, Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Couldn't create path " + path, getFileName(path), e);
        }
        doUpdate(r, path);
    }

    @Override//пасс преобразовывает в резюме(десериализация?)
    protected Resume doGet(Path path) {
        try {
            return streamSerializer.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path read error", getFileName(path), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path delete error", getFileName(path), e);
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        //получаем поток с пассами,с каждым пассом делаем doGet(преобразуем в резюме) и кладем в Лист
        return getFilesList().map(this::doGet).collect(Collectors.toList());
    }

    //убрали дублирование кода
    private String getFileName(Path path) {
        return path.getFileName().toString();
    }

    //над списком файлов оборачиваем ленивую оберточку Stream:
    private Stream<Path> getFilesList() {//ексепшены не пробрасывает а сразу обрабатывает
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Directory read error", e);
            //надо пробрасывать е, а то не узнаем-что случилось
        }
    }
}