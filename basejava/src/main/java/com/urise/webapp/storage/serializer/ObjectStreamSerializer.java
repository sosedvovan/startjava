package com.urise.webapp.storage.serializer;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
//применяем паттерн стратегия
/**
 * Created by val on 2017-08-05.
 */
public class ObjectStreamSerializer implements StreamSerializer {

    @Override//сериализуем    ObjectOutputStream бросает 1 ексепшен
    public void doWrite(Resume r, OutputStream os) throws IOException {
        //ObjectOutputStream -враппер, обертывающий врапер-буффер с OutputStream
        //класс ObjectOutputStream делает сериализацию
        try(ObjectOutputStream oos = new ObjectOutputStream(os)){
            oos.writeObject(r);
        }
    }

    @Override//десериализуем  ObjectInputStream бросает 2-а ексепшена
    public Resume doRead(InputStream is) throws IOException {
        try(ObjectInputStream ois = new ObjectInputStream(is)){
            return (Resume) ois.readObject();
        }catch (ClassNotFoundException e){
            throw new StorageException("Error read resume", null, e);
        }
    }
}
