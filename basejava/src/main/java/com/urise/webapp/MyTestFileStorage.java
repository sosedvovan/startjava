package com.urise.webapp;

import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.FileStorage;
import com.urise.webapp.storage.serializer.ObjectStreamSerializer;
import com.urise.webapp.storage.serializer.StreamSerializer;

import java.io.*;

public class MyTestFileStorage {

    private static final String UUID_1 = "uuid98";

    public static void main(String[] args) throws IOException {

        File file = new File("D:\\startjava\\basejava\\storage2");//привязали директрию к сущности File
        //file.createNewFile();

       StreamSerializer SS = new ObjectStreamSerializer();//ссылке типа интерфейс присвоили реализацию

       FileStorage fs = new FileStorage(file, SS);//подали все это в конструктор хранилища FileStorage

        Resume MyResume = new Resume(UUID_1, "Name1");//создали тестовый объект Резюме

        fs.save(MyResume);//сериализовали его абстрактом Storage с реализацией FileStorage

        //ObjectStreamSerializer OSS = new ObjectStreamSerializer();
        //OSS.doWrite(MyResume, new FileOutputStream(file));

       // fs.delete("uuid");

        /**
        FileOutputStream fis = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fis);
        oos.writeObject(MyResume);
        oos.close();
         */



        //System.out.println(fs.get("uuid"));

        //FileInputStream fis = new FileInputStream("MyStorage_\\uuid");
        //System.out.println(fs.getAllSorted());


    }
}
