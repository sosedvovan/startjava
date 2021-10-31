package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Assert;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;//статический импорт чтобы не писать имя класса
                                                //перед его методом
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractArrayStorageTest {
    /**
     * action в каждом тестовом методе создаем сценарий действия (можно в @Before)
     * те создаем необходимые объекты, процесс создания которых надо проверить, или надо
     * этити объекты использовать для вызова проверяемых методов. Так же здесь инициализируем
     * эти объекты нужными нам значениями, инициализируем (заполняем) нужные нам массивы.
     * Так же создаем подключения к БД и Серверу для проверки и др.
     *
     * assertion - сопоставление ожидания и полученного результата (с пом assertEquals() и др.)
     */

    //для теста заполним storage тремя резюме:
    /**
     * чтобы класс заработал сампосебе допиши сюда new ArrayStorage и убери abstract
      */

    private Storage storage;//указатель на какой-то Storage (SortedArrayStorage или dArrayStorage)
    //для его инициализации исп конструктор
    private static final String UUID_1 = "uuid1"; //созд переменные String
    private static final Resume RESUME_1 = new Resume(UUID_1);//созд объекты для массива storage

    private static final String UUID_2 = "uuid2"; //для @Before
    private static final Resume RESUME_2 = new Resume(UUID_2);

    private static final String UUID_3 = "uuid3"; //static- тк они одинак в каждом тест-методе
    private static final Resume RESUME_3 = new Resume(UUID_3);

    private static final String UUID_4 = "uuid4";
    private static final Resume RESUME_4 = new Resume(UUID_4);

    /**
     * Инициализировать объекты, кот нам нужны для тестов,
     * мы могли бы еще в статическом блоке:
     *
     * static {
     *         RESUME_1 = new Resume(UUID_1);
     *         RESUME_2 = new Resume(UUID_2);
     *         RESUME_3 = new Resume(UUID_3);
     *         RESUME_4 = new Resume(UUID_4);
     *     }
     */

    //в этот конструктор(принимает интерфейс) из дочернего класса придет объект этого
    //дочернего класса и проинициализирует поле private Storage storage; ->
    //-> нижележащие тестовые методы будут срабатывать на объекте дочернего класса
    protected AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before//этот метод вызывается перед каждым тестовым методом
    public void setUp() throws Exception {
        storage.clear();
        storage.save(RESUME_1);//ctrl + alt + C -> выделить в константы
        storage.save(RESUME_2);//shift + F6 -> рефактор ренейм
        storage.save(RESUME_3);
    }
    //перед запуском каждого тестового метода будет запускаться метод @Before
    //и очищаться и инициализироваться storage (в нашем случае)
    //Вместо @Before можно @BeforeClass(тогда он и поля класса должны быть static)
    //при запуске каждого тестового метода будет создаваться объект этого класса
    //и на этом объекте будет запускаться тело тестового метода
    //в названиях тестовых методов принято исп слово Should - (ожидается).
    @Test
    public void clear() throws Exception{
        // throw new IllegalAccessException();
        storage.clear();//почистили
        assertSize(0);//убедились, что size стал 0
    }

    @Test
    public void update() throws Exception{//тест метода update(
        Resume newResume = new Resume(UUID_1);//создали объект Resume
        storage.update(newResume);//записали этот объект в хранилище-наш массив storage
        assertTrue(newResume == storage.get(UUID_1));//запросили из хранилища этот объект Resume по
        // значению его поля и сравнили с ожидаемым-кот только что создали
        //это будет работать пока хранилище в памяти а не в реальной ДБ. тк из ДБ мы будем
        //доставать объект с теми же полями, но физически в куче это будет другой объект
    }

    @Test
    public void getAll() throws Exception{//тк метод getAll() возвращает массив:
        Resume[] array = storage.getAll();//получим этот массив(он не отсортирован- в порядке занесения)
        Assert.assertEquals(3, array.length);//ожидаем что длина этого массива 3
        Assert.assertEquals(RESUME_1, array[0]);//ожидаем что RESUME_1 лежит в первой ячейке
        Assert.assertEquals(RESUME_2, array[1]);//ожидаем что RESUME_2 лежит во второй ячейке
        Assert.assertEquals(RESUME_3, array[2]);//ожидаем что RESUME_3 лежит в третьей ячейке

    }

    @Test
    public void save() throws Exception{//проверка метода save()
        storage.save(RESUME_4);//сначала запишем в storage новый элемент
        assertSize(4);//теперь можем проверить Size

        //теперь найдем объект по значению его поля с пом get() и ожидаем RESUME_4 соответственно
        assertGet(RESUME_4);//воссп своим служебным прайвет методом
        //assertEquals(RESUME_4, storage.get(UUID_4));//иссп статический импорт при обращении к методу

    }

    @Test(expected = NotExistStorageException.class)
    public void delete() throws Exception{
        storage.delete(UUID_1);//сначала удалим элемент
        assertSize(2);//теперь можем проверить Size
        storage.get(UUID_1);//теперь попробуем взять удаленный
        //(уже не существующий) элемент - соотв должен быть эксепшен-
        // вынесем его в @Test
    }

    @Test
    public void size() throws Exception{
        //проверим этот метод. 3- это сколько ожидается.
        assertSize(3);
    }

    @Test
    public void get() throws Exception{
        assertGet(RESUME_1);//воссп своим служебным прайвет методом
        assertGet(RESUME_2);//там мы по uuid'у достаем RESUME_? из storage
        assertGet(RESUME_3);//и сравниваем с правильным ответом
    }

    //дописали руками- тест на NotExist:
    @Test(expected = NotExistStorageException.class)//тесту сказали что ожитаем этот эксепшен
    public void getNotExist() throws Exception{
        storage.get("dummi");//и попробуем найти несуществующее значение.
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception{ //проверка нашего собственого ексепшена
        storage.delete("dummu");//пробуем удалить объект которого нет
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception{ //проверка нашего собственого ексепшена
       storage.delete("dummu");//пробуем удалить объект которого нет
    }

    @Test(expected = ExistStorageException.class)
    public void saveNotExist() throws Exception{ //проверка нашего собственого ексепшена
        storage.save(RESUME_1);//пробуем сохранить объект который уже есть в массиве
    }

    @Test(expected = StorageException.class)//проверка переполнения нашего массива storage
    public void saveOverflow() throws Exception{ //проверка нашего собственого ексепшена
        try {
            for (int i = 4; i <= AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume());//заполним весь массив
            }
        }catch (StorageException e){
            Assert.fail();
        }
        storage.save(new Resume());//пробуем после заполнения массива записать туда еще один объект
        //на этой крайней строчке должен выбросится StorageException -тогда тест пройден
        //но если StorageException выбросится в цикле for, тогда тест завалится-> возьмем в try-catch
    }

    private void assertSize(int size){//приватный внутренний служебный метод
        assertEquals(size, storage.size());//подаем ожидаемый size и сравниваем с имеющимся
    }

    private void assertGet(Resume r){//приватный внутренний служебный метод
        assertEquals(r, storage.get(r.getUuid()));//подаем ожидаемый Resume r и сравниваем
                    // с полученным из нашего метода get()
    }
}


/**
 * Тестирование с помощью JUnit (Test Case):
 * http://web.archive.org/web/20190829153452/http://www.javenue.info/post/19
 *
 * Тестирование кода Java с помощью фреймворка JUnit от SpecialistTV:
 * https://www.youtube.com/watch?v=z9jEVLCF5_w
 *
 * В каких случаях использовать fail():
 * https://stackoverflow.com/questions/3869954/whats-the-actual-use-of-fail-in-junit-test-case
 **/

/**
 *
 * Подсказки по HW4:
 *
 *     SortedArrayStorageTest должен запускаться с SortedArrayStorage
 *     ArrayStorageTest c ArrayStorage
 *     для этогодобавьте конструктор в AbstractArrayStorageTest, который инициализирует Storage storage,
 *     а в наследниках добавьте конструкторы, которые будут вызывать super() с нужным хранилищем
 *
 *     тестировать правильность сортировки не надо
 *
 *     в тестах проверяйте Resume целиком, а не их uuid
 *
 *     иерархия наследования тестовых классов должна совпадать с иерархией тестируемых
 *
 *     логика реализации теста на переполнение массива (StorageException):
 *     заполняем массив, но не вызываем у него переполнение
 *     если при заполнении вылетит исключение, то тест должен провалиться (используйте Assert.fail())
 *     в fail() выводите сообщение о том, что переполнение произошло раньше времени
 *     тест считается успешно пройденным, когда переполнение происходит при попытке добавить
 *     в полностью заполненный массив еще одно резюме
 *
 */