package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//синглетон.Конфигурирование данных в Java проекте. Читаем пароли к дб и пути для сериализации объектов
// из файла resumes.properties
/**
//как пользоваться классом:
//статик объект этого класса INSTANCE создается при загрузке класса классЛоадером
//через имя класса обращаемся к объекту INSTANCE с пом метода get(): Config.get()
//на этом объекте INSTANCE - вызываем нужный нам геттер.
//например:  protected static final File STORAGE_DIR = Config.get().getStorageDir();
*/

public class Config {
    //константа с расположением файла с паролями
    private static final File PROPS = new File("config\\resumes.properties");
    //объект этого класса-синглетон(тк однопоточка у нас)
    private static final Config INSTANCE = new Config();//вызываем пустой конструктор, но с кодом в теле
    //и получаем объект этого класса INSTANCE кот несет в себе поля с данными из файла с паролями

    //объект Properties props - в него зачитываем из файла с паролями с пом. InputStream
    //Properties наследует HashTable(устаревшая Мапа), те обладает всеми методами Мапы + доп свои
    private Properties props = new Properties();//props- это Мапа + доп методы (Этот класс иссп. для работы с файлами-проперти с паролями)
    private File storageDir;//это поле инициализируется папкой куда складываем сериализованные объекты

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
        try (InputStream is = new FileInputStream(PROPS)) {
            //зачитываем в props из is
            props.load(is);
            //возьмем из проперти значение по ключу storage.dir(в файле с паролями лежит) : props.getProperty("storage.dir")
            //и инициализируем этим File storageDir
            storageDir = new File(props.getProperty("storage.dir"));//"storage.dir" это ключик, а этот метод
                                                                    //возвращает его значение(из файла с паролями)
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS.getAbsolutePath());
        }
    }

    public File getStorageDir() {
        return storageDir;
    }
}
