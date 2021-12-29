package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.*;


import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
//сериалайзер на основе датаинпутстрим и дата аутпутстрим
public class DataStreamSerializer implements StreamSerializer {

    @Override
    //здесь в аргументах получаем объект резюме, которое надо сохранить
    //и поток типа OutputStream os - FileOutputStream("имя файла в который пишем")
    //далее DataOutputStream обернем FileOutputStream("имя файла в который пишем"): new DataOutputStream(os)
    public void doWrite(Resume r, OutputStream os) throws IOException {
        //создадим стрим кот работает не с байтами а со строками, целыми числами
       //он может читать,писать их из,в поток
        //это есть оберточка
        try (DataOutputStream dos = new DataOutputStream(os)) {
            //передадим в этот поток резюме и контакты
            //Uuid тоже сериализуем, тк нет имени файла и для реад будем доставать этот Uuid - оверхеад
            dos.writeUTF(r.getUuid());//writeUTF()-метод для записи в поток с неадекватн названием
            dos.writeUTF(r.getFullName());//writeUTF() записывает строчку в код. utf


            /**
             * перенесли это в реализацию нашего абстракта ElementWriter<T> ->{...} см в лямбде ниже.
             * Мы сделали свой интерфейс ElementWriter<T> (наподобии консамерра)
             * чтобы его абстракт мог выбрасывать throws IOException,
             * чтобы не оборачивать здесь в трай-кетч методы writeUTF
             * здесь r берет свою пару entry и записывает из нее имя и значение
            for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()){
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }

             далее обращение к служебн методу writeCollection(), который запишет
             в текстовый файл число контактов(стоп-маркер)
             и далее запишет все пары entrySet() из Мапы Резюме с контактами(вызовет абстракт write(T t)).
             запись происходит в коде-реализации абстракта write(T t) нашего интерфейса ElementWriter<T>  ->{...} см в лямбде ниже
             */

            //сохраняем контакты в нашей служебной мапе
            Map<ContactType, String> contacts = r.getContacts();
            //в служ метод записи коллекции - подаем след аргументы:
            //в том числе и реализацию абстракта write(T t) нашего интерфейса ElementWriter<T> с пом. лямбды
            //[интерфейс ElementWriter<T> сделали наподобии комсаммера- что-то принимает и ничего не возвращает+
            // + добавили throws IOException, которого у настоящего консаммера нет]
            //в лямбде и происходит обращение к writeUTF() DataOutputStream'а
            writeCollection(dos, contacts.entrySet(), entry -> {//релиз консаммер не подход тк не бросает ексепшн- делаем свой интерфейс
                //достаем из ентри name и Value для контактов(енум переводим в строку):
                dos.writeUTF(entry.getKey().name());//подбираем интерфейс:чтото примим чтото делает начего не отдает
                dos.writeUTF(entry.getValue());//writeUTF выбр ексепш, след надо чтоб и консаммер тоже выбрасывал
            });

            //Todo реализовать сохранение sections

            //метод entrySet() возвращает набор-Set, имеющий те же элементы, что и хэш-карта.
            //r.getSections().entrySet() - берет у объекта r его секции(Мапа) и превращает их в Сет
            //те один элемент Сета сделанного из Мапы будет выглядеть так: 20=Geeks(КлючМапы=ЗначениеМапы)
            //этот Сет надо передать в служебн метод чтобы взять и записать стоповое слово: collection.size()
            //это будет колличеством секций(это надо больше для контактов, но метод у нас один для контактов и секций)

            //достаем из каждой ентри name и Value для секций в лямбде(реализ абстракт интерф ElementWriter<T>):
            //и отправляем в наш служебный метод(там запуск абстракта[кода в лямбде]]) вместе с потоком dos и Сетом с Маповскими ентри:
            writeCollection(dos, r.getSections().entrySet(), entry -> {
                SectionType type = entry.getKey();//типу енуму присваиваем ключ из Мапы
                Section section = entry.getValue();//типу section присваиваем значение из Мапы
                dos.writeUTF(type.name());//записываем в файл енум(название секции)
                //и далее в зависимости от того-какой именно енум(секция)-действуем:
                switch (type) {
                    case PERSONAL://проваливаемся
                    case OBJECTIVE:
                        //секшин преобраз в текстсекшен и выводим как строчку getContent()
                        dos.writeUTF(((TextSection) section).getContent());
                        break;
                    case ACHIEVEMENT://проваливаемся
                    case QUALIFICATIONS:
                        //секшин преобраз в листсекшен и передаем в метод. [getItems() возвр List<String>]
                        writeCollection(dos, ((ListSection) section).getItems(), dos::writeUTF);
                        //[dos::writeUTF == item -> dos.writeUTF(item)]реализ абстракта или лямбдой или ссылкой на сигнатурный метод
                        break;
                    case EXPERIENCE://проваливаемся
                    case EDUCATION:
                        //секшин преобраз в органайзерсекшен и опять пользуемся нашим служебным методом:
                        //и передаем в него поток dos(для вызова writeUTF), коллекцию List<Organization>,
                        //и реализацию ElementWriter<Organization> для Organization с пом анонимного класса
                        writeCollection(dos, ((OrganizationSection) section).getOrganizations(), new ElementWriter<Organization>() {
                            @Override//переопределяем абстракт функц. интерфейса след. образом:
                            public void write(Organization org) throws IOException {
                                //записываем в файл линк:
                                dos.writeUTF(org.getHomePage().getName());
                                dos.writeUTF(org.getHomePage().getUrl());
                                //изнутри анонима обращаемся к методам через имя этого класса:
                                DataStreamSerializer.this.writeCollection(dos, org.getPositions(), position -> {
                                    DataStreamSerializer.this.writeLocalDate(dos, position.getStartDate());
                                    DataStreamSerializer.this.writeLocalDate(dos, position.getEndDate());
                                    dos.writeUTF(position.getTitle());
                                    dos.writeUTF(position.getDescription());
                                });
                            }
                        });
                        break;
                }
            });
        }
    }

    //утильный метод поможет записывать год и месяц в doWrite
    private void writeLocalDate(DataOutputStream dos, LocalDate ld) throws IOException {
        dos.writeInt(ld.getYear());
        dos.writeInt(ld.getMonth().getValue());
    }

    private LocalDate readLocalDate(DataInputStream dis) throws IOException {
        return LocalDate.of(dis.readInt(), dis.readInt(), 1);
    }

    //здесь нам надо читать в том же самом порядке, что и записали
    @Override
    public Resume doRead(InputStream is) throws IOException {
        //делаем симметрично врайтеру
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();//считали uuid из файла
            String fullName = dis.readUTF();//считали fullName из файла
            Resume resume = new Resume(uuid, fullName);//и сделали резюме

            //далее надо считать контакты, которые были записанны в цикле
            //и после контактов еще записанны секции в этот же файл
            //так что считывать контакты до конца текстового файла нельзя
            //тк словится иоЕксепшен
            //для решения-можно поставить маркер-стоповое слово,
            //но оно может перепутаться с каким нибудь значением
            //значит мы запишем кол-во контактов в служебн методе writeCollection()

            //далее нам надо причитать это колличество контактов
            //чтобы знать когда надо переходить к вычетке секций, кот идут в файле после контактов
            //и передать в объект Резюме.
            //делаем это в служ методе readItems(), в аргументы которого отправляем dis и
            //реализацию абстракта interface ElementProcessor - void process()
            //[interface ElementProcessor наш,- типа консаммер, только throws IOException]
            //в readItems() простем число-маркер с кол-вом строк с контактами
            //и запусти абстракт processor.process();

            //получим название контакт-енума:
            //то, что вернет dis.readUTF() преобразеум в ContactType с пом valueOf() енумовского
            //и добавим в объект resume:
            /**
             * еще так делали
             * //получим значение этого контакт-енума:
             * //String value = dis.readUTF();
             * //и добавим этот воссозданный очередной в цикле контакт к нашему резюме:
             * //resume.addContact(contactType, value); //строка работает в doRead()-от туда мы этот код и перенесли
             */
            //в итоге- чтение контактов- это одна след. строчка
            readItems(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));

            //Todo реализовать чтение sections
            readItems(dis, () -> {
                //сначала прочтем sectionType (valueOf строку переведет в енум)
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                //потом в зависимости от вида секции(енума)- читаем эту секцию в методе readSection и собираем Мапу
                resume.addSection(sectionType, readSection(dis, sectionType));
            });
            return resume;
        }
    }


    //считываем секции в зависимости от енума(SectionType):
    private Section readSection(DataInputStream dis, SectionType sectionType) throws IOException {
        switch (sectionType) {
            //если енум-SectionType PERSONAL или OBJECTIVE, то надо считать String  (TextSection)
            case PERSONAL:
            case OBJECTIVE:
                //для TextSection все просто: из датаинпутстрима читаем строчку контент
                return new TextSection(dis.readUTF());
            //если енум-SectionType ACHIEVEMENT или QUALIFICATIONS, то надо считать List<String>  (ListSection)
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                //для ListSection: читаем из своего метода чтения листа поставляя туда поток и реализацию
                //и получая оттуда готовый Лист
                return new ListSection(readList(dis, new ElementReader<String>() {//ElementReader<String> чем параметризуем, то и возвращает
                    @Override//такая реализация возвращает стрингу:
                    public String read() throws IOException {
                        return dis.readUTF();
                    }
                }));
            //если енум-SectionType EXPERIENCE или EDUCATION, то надо воссоздать OrganizationSection в котором List<Organization> в которых-
            // объект Линк и АррайЛист с объектами Позиция(дата,време,2-е стринги)
            case EXPERIENCE:
            case EDUCATION:
                //в конструктор OrganizationSection надо подать List<Organization> для этого вызовем readList(поток, аноним с реализац. абстракта):
                return new OrganizationSection(
                        readList(dis, new ElementReader<Organization>() {//readList вернет List и в него отправляем абстракт возвращающий new Organization
                            @Override
                            public Organization read() throws IOException {
                                return new Organization(
                                        //из входящ потока прочитаем 2 строчки нейм и урл
                                        new Link(dis.readUTF(), dis.readUTF()),
                                        //далее чтение позиций и создание new Organization.Position(...)
                                        DataStreamSerializer.this.readList(dis, () -> new Organization.Position(
                                                DataStreamSerializer.this.readLocalDate(dis), DataStreamSerializer.this.readLocalDate(dis), dis.readUTF(), dis.readUTF()
                                        ))
                                );
                            }
                        }));
            default:
                throw new IllegalStateException();
        }
    }

    //по сути когда в аргументы передаем реализацию- мы передаем метод(абстракт), который что-то нам вернет
    //те передаем метод в метод и, запуская его, пользуемся тем, что он возвращает
    private <T> List<T> readList(DataInputStream dis, ElementReader<T> reader) throws IOException {
        //считаем снач колво элементов из дата инпут стрима
        int size = dis.readInt();
        //создаем Аррай размерностью с size
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            //читаем, и к листу добавляем элемент, прочитанный из датаинпутстрима
            //reader.read() - ссылка типа интерфейс запускает свой абстракт с реализацией с возвратом стринги
            list.add(reader.read());
        }
        return list;
    }

    //для doRead
    private interface ElementProcessor {
        void process() throws IOException;//абстракт без параметров(его реализацию мы посылаем в метод,
                                         //в котором вызывается его абстракт так Ява понимает
                                          //какой абстракт в какой лямбде мы реализуем при реализации в лямбде)
    }
    //делаем свой интерфейс типа саплаер-поставляет-продюссер-возвращает
    private interface ElementReader<T> {
        T read() throws IOException;//абстракт параметризирован
    }

    //делаем свой интерфейс типа консаммер-потребляет-стратегия, но с ексепшеном
    //его абстракт void write(T t) типа как у консамера абстракт тоже void
    private interface ElementWriter<T> {
        void write(T t) throws IOException;//абстракт с параметрами
    }

    //метод для доРид- воссоздает контакты.
    //добавляет в объект резюме контакты в том же порядке, что и записали
    //сначала считает из входного потока число - это будет кол-во контактов, кот надо воссоздать
    //за которыми уже начнутся секции
    //и в цикле выполнять process()-считывание- это абстракт реализацию которого принес в аргументы
    // processor, который его здесь и запустит processor.process();
    private void readItems(DataInputStream dis, ElementProcessor processor) throws IOException {
        int size = dis.readInt();//получили кол-во строчек в файле с контактами, далее записаны секции
        for (int i = 0; i < size; i++) {//итерируемся по этому кол-ву строчек

            processor.process();
        }
    }

    //вспом метод записи в коллекции(функция высшего порядка, тк принимает функцию-
    // реализацию нашего собственного функц. интерфейса)
    private <T> void writeCollection(DataOutputStream dos, Collection<T> collection, ElementWriter<T> writer) throws IOException {
        //в выходной поток записываем размер коллекции контактов
        //это будет маркер-стоповое слово от которого начнутся секции
        dos.writeInt(collection.size());

        for (T item : collection) {//идем по коллекции Сету сделанному из мапы
            //writer- объект класса реализ наш интерф ElementWriter<T> реализуя его абстракт
            //write- абстракт ElementWriter<T>'а, кот непосредственно записывает в поток
            //здесь мы его вызываем, передавая ему коллекцию из sections
            writer.write(item);//и записываем каждый элемент
            //write-абстракт нашего служебн интерфейса, кот вместо консаммера
        }
    }
}