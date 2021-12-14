package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import org.junit.Assert;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;//статический импорт чтобы не писать имя класса
                                                //перед его методом
import org.junit.Before;
import org.junit.Test;

import java.time.Month;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractStorageTest {
    /**
     * каждый тестовый метод состоит из action и assertion:
     *
     * action в каждом тестовом методе создаем сценарий действия (можно в @Before)
     * те создаем необходимые объекты, процесс создания которых надо проверить, или надо
     * этити объекты использовать для вызова проверяемых методов. Так же здесь инициализируем
     * эти объекты нужными нам значениями, инициализируем (заполняем) нужные нам массивы.
     * Так же создаем подключения к БД и Серверу для проверки и др.
     *
     * assertion - сопоставление ожидания и полученного результата (с пом assertEquals() и др.)
     */


    /**
     * чтобы класс заработал сампосебе допиши сюда: = new ArrayStorage(); и убери abstract из названия класса
      */

    //для теста надо импортировать в этот класс объект проверяемого класса, чтобы был доступ
    // к полям и методам проверяемого класса:
    protected Storage storage;
    //это указатель на какой-то конкретный объект Storage (SortedArrayStorage, ArrayStorage ...)
    //сюда будем подставлять объект проверяемого класса(при вызове конструкторов дочек этого класса)
    //для инициализации этого поля исп конструктор

    //далее для теста надо будет заполнить storage (тремя) объектами резюме:
    //создадим эти объекты с помощью полей этого класса со static final переменными:
    private static final String UUID_1 = "uuid1"; //созд final переменные String для поля объекта Resume
    private static final Resume R1;//созд объекты final для массива storage

    private static final String UUID_2 = "uuid2"; //для @Before
    private static final Resume R2;

    private static final String UUID_3 = "uuid3"; //static- тк они одинак в каждом тест-методе
    private static final Resume R3;

    private static final String UUID_4 = "uuid4";
    private static final Resume R4;

    /**   ТЕОРИЯ: СТАТИЧЕСКИЕ БЛОКИ:
     * Инициализировать объекты, кот нам нужны для тестов,
     * мы могли бы еще в статическом блоке.
     * те в полях класса можно только объявить переменные след образом:
     * (здесь это объекты для помещения их в хранилище)
     */
    static {
        R1 = new Resume(UUID_1, "Name1");
        R2 = new Resume(UUID_2, "Name2");
        R3 = new Resume(UUID_3, "Name3");
        R4 = new Resume(UUID_4, "Name4");
        R1.addContact(ContactType.MAIL, "mail1@ya.ru");
        R1.addContact(ContactType.PHONE, "11111");
        R1.addSection(SectionType.OBJECTIVE, new TextSection("Objective1"));
        R1.addSection(SectionType.PERSONAL, new TextSection("Personal data"));
        R1.addSection(SectionType.ACHIEVEMENT, new ListSection("Achivment11", "Achivment12", "Achivment13"));
        R1.addSection(SectionType.QUALIFICATIONS, new ListSection("Java", "SQL", "JavaScript"));
        R1.addSection(SectionType.EXPERIENCE,
                new OrganizationSection(
                        new Organization("Organization11", "http://Organization11.ru",
                                new Organization.Position(2005, Month.JANUARY, "position1", "content1"),
                                new Organization.Position(2001, Month.MARCH, 2005, Month.JANUARY, "position2", "content2"))));
        R1.addSection(SectionType.EDUCATION,
                new OrganizationSection(
                        new Organization("Institute", null,
                                new Organization.Position(1996, Month.JANUARY, 2000, Month.DECEMBER, "aspirant", null),
                                new Organization.Position(2001, Month.MARCH, 2005, Month.JANUARY, "student", "IT facultet")),
                        new Organization("Organization12", "http://Organization12.ru")));
        R2.addContact(ContactType.SKYPE, "skype2");
        R2.addContact(ContactType.PHONE, "22222");
        R1.addSection(SectionType.EXPERIENCE,
                new OrganizationSection(
                        new Organization("Organization2", "http://Organization2.ru",
                                new Organization.Position(2015, Month.JANUARY, "position1", "content1"))));
    }

     /**
     * а в статическом блоке проинициализировать их:
     *
     * static {
     *         RESUME_1 = new Resume(UUID_1);
     *         RESUME_2 = new Resume(UUID_2);
     *         RESUME_3 = new Resume(UUID_3);
     *         RESUME_4 = new Resume(UUID_4);
     *     }
     *
     *****************************
     *  Это исп. в след. случае: если бы конструктор Resume бросал чекед эксепшн(не сейчас), то при создании
     *  объекта (инициализации его полей) в конструкторе - new в поле класса(как у нас сейчас):
     *
     *  ------   private static final Resume RESUME_1 = new Resume(UUID_1);   --------
     *
     *  мы не сможем его обработать,
     *  а в статическом блоке мы можем:
     *
     *  -----  RESUME_1 = new Resume(UUID_1); ------
     *
     *  взять в блок try-catch
     *  и отловить эксепшн
     ******************************
     *
     *  статические блоки выполняются один раз при загрузке класса после создания(инициализации других)
     *  полей, но перед конструктором
     *  а если блок не статический, он выполняется каждый раз, когда конструируется объект-
     *  тоже самое что и в конструкторе.
     *
     *  static final переменные инициализируем только в статическом блоке, тк они должны быть
     *  проинициализированны сразу(дефолтные значения им не присваиваются при загрузке класса)
     */

    /**
     * *********************************!!!!!!!!!!!!!!!!!!!!!!!!
     * то, в ТЕСТОВОМ классе мы создем нужные нам объекты и здесь же используем их для тестов.
     * так же в дочерних классах создаем объект дочернего класса (исп. super в его конструкторе)-
     * и на этом объекте будут вызываться тэстовые методы.
     *
     * ТО ТЕСТОВЫМ МЕТОДАМ НУЖНЫ ТЕСТОВЫЕ ОБЪЕКТЫ НАШЕЙ МОДЕЛИ-Резюме
     * И ОБЪЕКТ КЛАССА НА КОТОРОМ НИ БУДУТ ВЫЗЫВАТЬСЯ
     *
     * получается, что тестовые классы надо делать зависимыми от проверяемых-
     * (в их поле надо всегда добавлять объект проверяемого класса
     * чтобы был доступ доступ к полям и методам проверяемого класса )
     * мы сдесь так и сделали: ---- private Storage storage; -----
     *
     * *********************************!!!!!!!!!!!!!!!!!!!!!!!!
     */

    //этот super конструктор вызывается при создании объекта дочернего класса
    // [принимает объект класса имплементирующего интерфейс Storage(полиморфизм через интерфейс Storage)]
    //и проинициализирует поле дочернего класса --- private Storage storage; ---- объектом Storage(Array,Map...)  ->
    //-> нижележащие тестовые методы будут срабатывать на объекте дочернего класса содержащего в себе объект Storage
    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before//этот метод вызывается перед каждым тестовым методом(задействует созданные выше объекты)
    public void setUp() throws Exception {
        storage.clear();
        storage.save(R1);//ctrl + alt + C -> выделить в константы
        storage.save(R2);//shift + F6 -> рефактор ренейм
        storage.save(R3);
    }
    //перед запуском каждого тестового метода будет запускаться метод @Before
    //и очищаться и инициализироваться storage (в нашем случае)
    //Вместо @Before можно @BeforeClass(тогда он и поля класса должны быть static)
    //при запуске каждого тестового метода будет создаваться объект тестового класса
    //и на этом объекте будет запускаться тело тестового метода
    //в названиях тестовых методов принято исп слово Should - (ожидается).

    @Test
    public void clear() throws Exception{
        // тестовый метод может выбрасывать throw new IllegalAccessException(); - не сопоставимые данные
        storage.clear();//почистили
        assertSize(0);//убедились, что size стал 0
    }

    @Test
    public void update() throws Exception{//тест метода update(
        Resume newResume = new Resume(UUID_1, "New Name");//создали объект Resume
        storage.update(newResume);//записали этот объект в хранилище-наш массив storage
        assertTrue(newResume == storage.get(UUID_1));//запросили из хранилища этот объект Resume по
        // значению его поля и сравнили с ожидаемым-кот только что создали
        //это будет работать пока хранилище в памяти а не в реальной ДБ. тк из ДБ мы будем
        //доставать объект с теми же полями, но физически в куче это будет другой объект
    }

    @Test
    public void getAllSorted() throws Exception{//тк метод getAll() возвращает массив:
        List<Resume> list = storage.getAllSorted();//получим этот массив(он не отсортирован- в порядке занесения)
        Assert.assertEquals(3, list.size());//ожидаем что длина этого массива 3
        //в иквалс у листа сравнивается по элементам:
        Assert.assertEquals(list, Arrays.asList(R1, R2, R3));


        //Assert.assertEquals(RESUME_2, array[1]);//ожидаем что RESUME_2 лежит во второй ячейке
        //Assert.assertEquals(RESUME_3, array[2]);//ожидаем что RESUME_3 лежит в третьей ячейке

    }

    @Test
    public void save() throws Exception{//проверка метода save()
        storage.save(R4);//сначала запишем в storage новый элемент
        assertSize(4);//теперь можем проверить Size

        //теперь найдем объект по значению его поля с пом get() и ожидаем RESUME_4 соответственно
        assertGet(R4);//воссп своим служебным прайвет методом
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
        assertGet(R1);//воссп своим служебным прайвет методом
        assertGet(R2);//там мы по uuid'у достаем RESUME_? из storage
        assertGet(R3);//и сравниваем с правильным ответом
    }

    //дописали руками- тест на NotExist:
    @Test(expected = NotExistStorageException.class)//тесту сказали что ожитаем этот эксепшен
    public void getNotExist() throws Exception{
        storage.get("dummi");//и попробуем взять несуществующее значение.
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception{ //проверка нашего собственого ексепшена
        storage.delete("dummu");//пробуем удалить объект которого нет
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception{ //проверка нашего собственого ексепшена
       storage.get("dummy");//пробуем обновить объект которого нет  //сверь с исходн автора это тело
    }

    @Test(expected = ExistStorageException.class)
    public void saveNotExist() throws Exception{ //проверка нашего собственого ексепшена
        storage.save(R1);//пробуем сохранить объект который уже есть в массиве
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
 *     для этого добавьте конструктор в AbstractArrayStorageTest, который инициализирует Storage storage,
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