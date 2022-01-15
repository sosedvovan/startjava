package com.urise.webapp;

import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.storage.Storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//синглетон.Конфигурирование данных в Java проекте. Читаем пароли к дб и пути для сериализации объектов
// из файла resumes.properties
/**
//как пользоваться классом:
//статик объект этого класса INSTANCE создается при загрузке класса классЛоадером.
//через имя класса обращаемся к объекту INSTANCE с пом метода get(): Config.get()
//на этом объекте INSTANCE - вызываем нужный нам геттер.
//например:  protected static final File STORAGE_DIR = Config.get().getStorageDir();
*/

public class Config {

    //константа с расположением файла с паролями. но в таком виде Томкат ее не увидит тк у него свой корневой каталог
    //можно сделать чз переменные среды, можно сделать фиксированное место в файловой системе-тильда в home или от рута,
    //можно в дб конфигурить, те по разному. мы передадим в качестве параметра: в Run->Edit Configuration->VM options:
    //-DhomeDir="C:/Users/Vladimir/IdeaProjects/startjava" зададим домашний каталог(исп. двойные слеши или юниксовые)
    //те при запусе Томката мы передадим ему переменную окружения homeDir, а в коде Ява(в этом классе)
    //возьмем  homeDir как системПроперти. для этого создадим статический метод getHomeDir() в котором скажем,
    //что если есть переменная homeDir, то возвращаем ее, если ее нет, то возвращаем текущий каталог.
    //тестам тоже понадобится это переменная homeDir.
    //private static final File PROPS = new File("basejava\\config\\resumes.properties");
    private static final File PROPS = new File(getHomeDir(), "basejava\\config\\resumes.properties");



    //объект этого класса-синглетон(тк однопоточка у нас)
    private static final Config INSTANCE = new Config();//вызываем пустой конструктор, но с кодом в теле
    //и получаем объект этого класса INSTANCE кот несет в себе поля с данными из файла с паролями

    private final File storageDir;//это поле инициализируется папкой куда складываем сериализованные объекты
    private final Storage storage;//это поле инициализируется url, логином и паролем от дб

    //извне обращаемся к этому методу(через имя класса)
    //на возврате этого метода(на INSTANCE этого класса) вызываем нужный геттер с паролем или с путём
    //пример вызова: protected static final File STORAGE_DIR = Config.get().getStorageDir();
    public static Config get() {
        return INSTANCE;
    }

    //приватный пустой конструктор, создает INSTANCE этого класса, и инициализирует поле File storageDir
    //с пом классов InputStream и Properties. File storageDir это у нас директория, куда сериализуем объекты
    //[для класса AbstractStorageTest]
    private Config() {
        //создаем поток байтиков подавая в него наш файл с паролями
        //зачитываем в props из is
        try (InputStream is = new FileInputStream(PROPS)) {

            //объект Properties props - в него зачитываем из файла с паролями с пом. InputStream
            //Properties наследует HashTable(устаревшая Мапа), те обладает всеми методами Мапы + доп свои
            //props- это Мапа + доп методы (Этот класс иссп. для работы с файлами-проперти с паролями)
            Properties props = new Properties();
            props.load(is);
            //возьмем из проперти значение по ключу storage.dir(в файле с паролями лежит) : props.getProperty("storage.dir")
            //и инициализируем этим File storageDir
            storageDir = new File(props.getProperty("storage.dir"));//"storage.dir" это ключик, а этот метод
                                                                  //возвращает его значение(из файла с паролями)

            //инициализируем поле Storage url, логином и паролем от дб
            storage = new SqlStorage(props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db.password"));
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS.getAbsolutePath());
        }
    }

    public File getStorageDir() {
        return storageDir;
    }

    public Storage getStorage() {
        return storage;
    }

    private static File getHomeDir() {
        String prop = System.getProperty("homeDir");
        File homeDir = new File(prop == null ? "." : prop);//"."-текущий каталог
        if (!homeDir.isDirectory()) {//если homeDir не папка, то ексепшен
            throw new IllegalStateException(homeDir + " is not directory");
        }
        return homeDir;
    }

}
