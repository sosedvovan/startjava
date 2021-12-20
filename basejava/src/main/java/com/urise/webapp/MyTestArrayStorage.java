package com.urise.webapp;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.AbstractStorage;

import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;
//реализуем SortedArrayStorage без наследований и проверим здесь в главном методе
public class MyTestArrayStorage {
    //в этом случае(хранилище обычный массив отсортирован пока вручную SortedArrayStorage) searchKey-ем будет <Integer>
// тк это не что иное в данном случае, как индекс в массиве.
// Метод getSearchKey() возвращает индекс в массиве по которому нужно положить элемент (save),
// по кот. надо удалить элемент (delete), или взять (get) элемент
    public static void main(String[] args) {

        //создали 4 объекта
        Resume r1 = new Resume("1", "A");
        Resume r2 = new Resume("2", "B");
        Resume r3 = new Resume("3", "C");
        Resume r4 = new Resume("5", "D");

        //положили их в обычный массив заявленный в поле этого класса
        save(r1);
        save(r2);
        save(r3);
        save(r4);

        //Arrays.sort(storage, RESUME_COMPARATOR);//получим нульпоинтерексепшен тк в массиве есть нули

        //посмотрели- на объекты в массиве
        System.out.println(Arrays.toString(storage));

        //создали 5-й объект
        Resume r5 = new Resume("4", "F");

        //вызвали метод, который должен добавить 5-й объект в массив о отсортировать его
        save(r5);

        //проверили-что получилось
        System.out.println(Arrays.toString(storage));
    }

    //получим реализацию компоратора для объектов класса Резюме
    private static final Comparator<Resume> RESUME_COMPARATOR = (o1, o2) -> o1.getUuid().compareTo(o2.getUuid());
    //получим реализацию логгера
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    //ПОЛЯ КЛАССА присущие Array'ю:

    //ограничение обычного массива-хранилища
    protected static final int STORAGE_LIMIT = 10;

    //это обычный массив- хранилище Резюме
    protected static Resume[] storage = new Resume[STORAGE_LIMIT];

    //переменная, для хранения реального кол-ва элементов в массиве
    protected static int size = 0;
    //---------------------------------------------------------------------------------------------------------------

    //метод  из AbstractStorage<SK> реализует абстракт из интерфейса Storage
    public static void save(Resume r) {//например хотим добавить в хранилище r5 -> 4F
        //в каждом методе добавим логирование:
        LOG.info("save" + r);
        //проверка есть ли такое резюме в бд- надо чтобы его небыло:
        //получим searchKey (в данном хранилище это Integer)
        Integer searchKey = getNotExistedSearchKey(r.getUuid());//в getNotExistedSearchKey(4)//отослали uuid == 4
        //getNotExistedSearchKey() или возвратится не существующий ключ поиска searchKey == null?
        // и он отправится на сохранение
        // или бросится ексепшн что такое резюме уже есть в ДБ

        doSave(r, searchKey);
        //если объект не нашелся делаем doSave()
    }


    //вспомагательный метод для save() из AbstractStorage<SK>
    private static Integer getNotExistedSearchKey(String uuid) {//пришло в аргументы 4
        Integer searchKey = getSearchKey(uuid);//в getSearchKey(4)//отослали uuid == 4; получили -4
        //метод getSearchKey() абстрактный вернет объект по значению поля uuid
        //ему тела будем писать в наследниках- мапе и др

        //метод isExist()(вернет true- если объект уже существует)
        //создали isExist() для распараллеривания проверки в разных реализациях хранилищ:
        if (isExist(searchKey)) {//true- если объект уже существует и следоват. бросаем эксепшен//-4 уйдет -> false и return searchKey -4
            throw new ExistStorageException(uuid);
            //эксепшен унаследован от RuntimeException -> пробрасывать (throws) в сигнатуре- нельзя
        }
        return searchKey;
    }

    //вспомогательный для save() из дважды делегированный в дочку -SortedArrayStorage  ВЕРНЕТ ИНДЕКС, НА КОТОРЫЙ НАДО ПОЛОЖИТЬ НОВЫЙ ОБЪЕКТ
    protected static Integer getSearchKey(String uuid) {//прислали uuid == 5//принимает значение поля, возвращает индекс или null если не найденно
        Resume searchKey = new Resume(uuid, "dummy");//создаем новый объект-клон(пока в Resume 1-о поле)
        //"dummy" -фиктивное, тк binarySearch()(поданный в него компоратор) сравнивает только по uuid

        //сеттер удалим тк стали использовать конструктор
        //searchKey.setUuid(uuid);//новому объекту присваеваем искомое значение поля из аргументов метода

        //ВНИМАНИЕ!!! массив должен быть первоначально отсортирован(при заполнении его вручную в этом случае-
        // добавляли объекты по возврастанию uuid-ов как и реализованно в компарабле)
        // возвращает позицию заданного значения. Если искомый элемент не найден, то возвращается - (position + 1),
        // где position - позиция элемента где он МОГ БЫ БЫТЬ.
        //метод binarySearch по хранилищу storage поищет созданный только что клон r5-"dummy", и не найдя его в хранилище
        // - вернет -4 (минус- потому, что не нашел; 4 - предполагаемое место в массиве- если бы он там был первоначально
        return Arrays.binarySearch(storage, 0, size, searchKey, RESUME_COMPARATOR);//должен вернуть 4
    }


    ////вспомогательный для save() из AbstractArrayStorage реализует абстракт из AbstractStorage<SK>
    protected static boolean isExist(Integer index) {
        return (Integer) index >= 0; //если >= 0 то вернет true -> индекс существует
    }

    //метод  из AbstractArrayStorage  реализует абстракт из AbstractStorage<SK>
    protected static void doSave(Resume r, Integer index) {//Ctrl+I -имплементить от родителя//пришло r5 и -4 (-4 это сёчкей-
        // индекс в массивена который надо сохранить объект)
        if (size == STORAGE_LIMIT) {//проверка-что в массиве еще есть свободные ячейки
            //System.out.println("Storage overflow");
            //для этой ситуации не будем создавать специальное исключение, а бросим родительское:
            throw new StorageException("Storage overflow", r.getUuid());
        } else {// если элемента с таким значением в массиве нет и в массиве есть свободные ячейки:
            insertElement(r, (Integer) index);//ссузили тип индекса-но если в аргументах
            //придет не Integer а String, например то будет ClassCastException- далее поправим это
            //insertElement() здесь абстрактный тк для обычного Array и SortedArray будут разные реализации
            size++;
        }
    }


    //метод из SortedArrayStorage реализует абстракт из AbstractArrayStorage
    protected static void insertElement(Resume r, int index) {//КАК МЫ УЗНАЛИ В НА КАКОЙ ИНДЕКС НАДО СОХРАНИТЬ НОВОЕ РЕЗЮМЕ В ОТСОРТИРОВАННЫЙ МАССИВ?
        int insertIdx = -index - 1;//вместо index всавим -4 получим --4-1 == 3
        System.arraycopy(storage, insertIdx, storage, insertIdx + 1, size - insertIdx);//сдвинем часть массива (от 3 индекса включ)вправо на 1 индекс
        storage[insertIdx] = r;//на третий индекс запишем новое резюме
        //где storage -массив из которого копируем
        //insertIdx  индекс с которого начнем копирование
        //storage -массив в который копируем
        //insertIdx + 1  индекс в принимающем массиве с которого начнется вставка элементов
        //size - insertIdx колличество элементов, которое необходимо перенести
    }

    //-------------------------------------------------------------
    //Теперь удалять будем


    public static void delete(String uuid) {//далее дублирование кода- избавимся в методе getExistedKey()
        LOG.info("delete" + uuid);
        // сначала проверка: есть ли такое резюме в бд- надо чтобы оно было.
        Integer searchKey = getExistedSearchKey(uuid);//помним: что в мапе нет индексов(там беспорядочное распределение пар)
        //getExistedSearchKey() или возвратится существующий ключ поиска searchKey
        // и он отправится на доудаление
        // или бросится ексепшн что такого резюме нет в ДБ

        doDelete(searchKey);//этот абстрактный метод реализуем в дочках(по разному)
        //если объект нашелся делаем doDelete(()
    }


    private  static Integer getExistedSearchKey(String uuid) {

        Integer searchKey = getSearchKey(uuid);
        //метод getSearchKey() абстрактный вернет индекс(или ключ) по значению
        //ему разные реализации будем писать в наследниках: Array, List, Map
        //помним: что в мапе нет индексов(там беспорядочное распределение пар)

        //создадим здесь выше абстрактный метод isExist() для распараллеривания проверки в разных реализациях хранилищ:
        if (!isExist(searchKey)) {
            //если объект найден(-приходит true - !переворачиваем на false-  не выбрасываем ексепшен-блок пропускается)
            //и сделаем еще вывод в лог для удобства отслеживания этого эксепшена:
            LOG.warning("Resume " + uuid + " already exist" + uuid);
            throw new NotExistStorageException(uuid);
            //эксепшен унаследован от RuntimeException -> пробрасывать (throws) в сигнатуре- нельзя
        }
        return searchKey;
    }

    protected static void doDelete(Integer index) {
        fillDeletedElement((Integer) index);//этот абстрактный метод реализуем в дочках(по разному)
        //в fillDeletedElement() удаляемому элементу присвоим крайний элемент
        storage[size - 1] = null;//а на место крайнего элемента запишем null
        size--;                 //можно было еще сдвигать влево все элементы находящиеся справа от удаляемого элемента
    }

    protected static void fillDeletedElement(int index) {//принимает индекс удаляемого элемента
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(storage, index + 1, storage, index, numMoved);
            //где storage -массив из которого копируем
            //index +1  индекс с которого начнем копирование
            //storage -массив в который копируем
            //index  индекс в принимающем массиве с которого начнется вставка элементов (его мы и удаляем)
            //numMoved  колличество элементов, которое необходимо перенести
        }
    }


    //----------------------------------------------------------------------------------------------------------------------

    public static int size() {
        return size;
    }

    //------------------------------------------------------------------------------------------------------------------

    // Метод get() ушел к родителю реализовываться а вместо doget() его дошаблонит:


    protected static Resume doGet(Integer index) {//в аргумент придет индекс искомого элемента (в обертке (Integer))
        return storage[(Integer) index];
    }


    public static void clear() {
        //метод служебного класса Arrays- fill() заполнит значениями часть массива (от 0 до size)
        //где size - инклюдев = включается(это можно уточнить в реализации метода)
        //есть реализация без указания этого интервала (без: от 0 до size)

        //зальет нулями массив storage на котором вызовем этот метод clear()
        Arrays.fill(storage, 0, size, null);
        size = 0;

        //for(int i = 0; i < size; i++){ //один из вариантов очистки массива
        //    storage[i] = null;
        // }
    }


    //----------------------------------------------------------------------------
    //метод принимае объект (Resume r), ищет его индекс по значению поля, если находит - перезаписывает им же)
    public static void update(Resume r) {
        //в каждом методе добавим логирование:
        LOG.info("update" + r);
        // сначала проверка: есть ли такое резюме в бд- надо чтобы оно было.
        Integer searchKey = getExistedSearchKey(r.getUuid());
        //getExistedSearchKey() или возвратится существующий ключ поиска searchKey
        // и он отправится на обновление
        // или бросится ексепшн что такого резюме нет в ДБ

        doUpdate(r, searchKey);
        //если объект нашелся делаем doUpdate()
    }

    protected static void doUpdate(Resume r, Integer index) {
        storage[(Integer) index] = r;
        //положили наш апдатный объект Резюме в обычный массив(перезатрем старый объект)
    }


}
