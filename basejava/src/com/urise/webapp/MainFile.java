package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainFile {

    public static void main(String[] args)  {

        //абстракцию file привязали к реальному файлу
        File file = new File("D:\\1.txt");//абсолютный путь

        try {
            System.out.println(file.getCanonicalFile());//D:\1.txt
        } catch (IOException e) {
            //пробросим еще дальше, передав сообщение и первоначальный ексепшен е
            //первоначальный ексепшен е обязательно надо передавать далее чтобы понятно было где ошибка
            throw new RuntimeException("Error", e);
        }

        //относит путь: ./src/com/urise/webapp  .означает текущ директорию а ..перейдем в родительскую
        File dir = new File("./src/com/urise/webapp");// можно так\\ и так/
        System.out.println(dir.isDirectory());//это директория? : true

        String [] list = dir.list();//list() вернет null, если директория пустая
        //след надо сделать проверку:
        if (list != null) {
            for (String name : dir.list()) {//dir.list() вернет массив сназванием файлов в папке dir
                System.out.println(name);

                //в полученном списке можно проверить isDirectory() и нырнуть глубже
            }
        }

        //посмотрим на классы потоков байтиков:
        FileInputStream fis = null;//создали ссылку здесь, чтобы она была видна в finally
        try {//пробуем
            fis = new FileInputStream(file);//потоку байтиков даем файл
            System.out.println(fis.read());//причитаем первый символ в потоке
        } catch (IOException e) {//отлавливаем 2-а эксепшена с пом родительского IOException
            e.printStackTrace();
        }finally {//если поток пустой(отработал) закрываем его в блоке finally
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //получили трехэтажные эксепшены
        //но с Ява 1.7 появился интерфейс автоКлосабле
        //и теперь можно пользоваться блоком автоматического закрытия ресурсов
        //напишем то же что и выше, тока короче:

        //в() после try вписываем то, что закроется автоматически
        try(FileInputStream fis2 = new FileInputStream(file)) {
            System.out.println(fis2.read());//108
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        //запустим статик рекурсионный метод глубокого обхода:
        printDirectoryDeeply(dir);
    }//закрыли главный метод

    // TODO: make pretty output- рекурсивный метод для глубокого обхода директории
    public static void printDirectoryDeeply(File dir) {
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {//проверка- если это не директория-метод закончит работать
                    System.out.println("File: " + file.getName());
                } else if (file.isDirectory()) {//если это директория- войдем в рекурсию
                    System.out.println("Directory: " + file.getName());
                    printDirectoryDeeply(file);
                }
            }
        }
    }
}
