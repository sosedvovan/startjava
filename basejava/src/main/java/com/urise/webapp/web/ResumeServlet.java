package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;
import com.urise.webapp.util.DateUtil;
import com.urise.webapp.util.HtmlUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//Alt+F9- при дебаге - перевеси дебаг на курсор и далее Alt+F8 и посмотреть текущие возвраты от методов, к которым обращаемся чз объекты или имена классов
//след. строчка отключает конфиг.проперти тк они пока не работают:
//private Storage storage = new SqlStorage("jdbc:postgresql://localhost:5432/resumes", "postgres", "00821821");



public class ResumeServlet extends HttpServlet {

    private Storage storage;// = Config.get().getStorage();//можно и сразу инициализировать без переопределения init().


    //для того, чтобы вывести табличку сервлету понадобится sqlStorage.
    //инициализировать sqlStorage можно  переопределив метод init():  (Ctrl+O  -  выбрать init())
    //см. жизненный цикл сервлета: с версии 2.3 пулл сервлетов не создается а исп. 1-н инстанс сервлета
    //поэтому в сервлете нельзя создавать поле типа uuid, тк в многопотоке он окажется не валидным(потокобезопасность)
    //Tomcat определяет сервлет по файлу xml. в сервлете должен быть конструктор по умолчанию
    //Tomcat создает сервлет вызывая метод init(), а мы можем в него чтото добавить
    //Tomcat в методе сервис-выбирает тип запроса и в методе дестрой снимает сервлет с эксплуатации
    //еще в классе Config задан абсолютный путь к файлу пропертей, а для Tomcat надо поправить, чтобы он увидел
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    //этот метод принимает все Post запросы из edit.jsp и здесь надо из данных пришедших из формы
    //создать новое резюме или обновить уже существующее
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //говорим, что request принимает русские буквы (имя пишем по русски)
        request.setCharacterEncoding("UTF-8");
        //из тела запроса достанем 2-а параметра:
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");

        //теперь определяем: что мы делали перед вызовом edit.jsp в listJstl.jsp - нажимали на кнопку создать или на кнопку редактировать
        //флаг isCreate будет true, если uuid не существует, значит надо сохранять(save), а не редактировать(update)
        final boolean isCreate = (uuid == null || uuid.length() == 0);
        Resume r;
        if (isCreate) {//true- создаем объект Resume для save
            r = new Resume(fullName);
        } else {
            r = storage.get(uuid);//достаем уже существующий объект Resume из базы для update
            r.setFullName(fullName);
        }

        //то на данном этапе имеем объект Resume внутри которого только fullName

        //ДЕЛАЕМ ОБРАБОТКУ КОНТАКТОВ (из формы из edit.jsp)
        //по uuid возьмем резюме:
        //при добавлении нового резюме(пост запрос) здесь можно сделать проверку uuid- если оно существует-загружаем по
        //старому uuid из стораджа, если нет- не загружаем
//        Resume r = storage.get(uuid);
        //в этом резюме поменяем значение поля fullName на новое, пришедшее в пост запросе
//        r.setFullName(fullName);

        //так же поменяем контактную группу (в цикле по заданному в форме ключу(name-в форме)-это название енума, получаем(request.getParameter()) значение(value в форме) - это текст, кот внесли в поле ):
        for (ContactType type : ContactType.values()) {//итерируемся по Контактам-енумам (values() походу возвратит массив со всеми Енумами)
            //name() в Енуме возвращает имя его Константы - оно(имя) и есть ключ для параметра(значения) в теле запроса из формы.  И берем у request значение по ключу-енуму.
            //те request.getParameter(берет параметр)(type.name()-по ключу EXPERIENCE(напр))
            //то в переменной String value будет лежать текст, который мы ввели в форме напротив соотв. енума.
            String value = request.getParameter(type.name());//getParameter() берет значение(value) по поданному в аргменты ключу(name) из тела пришедшего сюда Post-запроса
            if (value != null && value.trim().length() != 0) {//если значение по текущему в цикле ключу-енуму не null и не пробелы
                r.setContact(type, value);//если есть значение- добавляем в мапу Контакты эту пару- ЕНУМ-ТЕКСТ ПОЛЯ
            } else {
                r.getContacts().remove(type);//если значение было, но мы его удалили при edit, то удаляем
            }
        }//выходим из for для Contact

        /*
        getParameter(String param): возвращает значение определенного параметра, название которого передается в метод.
        Если указанного параметра в запросе нет, то возвращается значение null.
        getParameterValues(String param): возвращает массив значений, который представляет определенный параметр.
        Если указанного параметра в запросе нет, то возвращается значение null.

        те здесь слово параметр эквивалентно слову ключ
         */

        //ДЕЛАЕМ ОБРАБОТКУ СЕКЦИЙ (из формы из edit.jsp)
        //идем по SectionType, те по енумам (values() походу возвратит массив со всеми Енумами):
        for (SectionType type : SectionType.values()) {
            //берем значение(value-в форме) по ключу(name-в форме), этим значением будет стринга с текстом, который мы ввели в форме:
            String value = request.getParameter(type.name());
            //так же берем в массив все пришедшие стринги по одному ключу - это мы в поле несколько строк написали, разделяя их переносом строки \n:
            String[] values = request.getParameterValues(type.name());//для 2-х последних Енумов EDUCATION и EXPERIENCE. из post возьмет список всех Organization
                                                                     //напр по ключу EXPERIENCE в post запросе получим массив String[] values = ["", "Organization2"]

            //далее условие, если пришла пустая секция(String value = null, и в массиве только один элемент- это стринга с ""):
            if (HtmlUtil.isEmpty(value) && values.length < 2) {//метод isEmpty сами писали, но обычно пользуются утильными классами из библиотеки(спринг, апаче команс, гуава)
                //удаляем пустую секцию:      а length < 2 тк первая там будет - которая пустая ""
                r.getSections().remove(type);
                //если секция не пустая-проваливаемся в switch (type):
            } else {
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        //добавляем в объект Резюме, в его мапу Секции
                        r.setSection(type, new TextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        //в конструктор new ListSection подаем строки с разбиением по переводу строки:
                        r.setSection(type, new ListSection(value.split("\\n")));
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        //будем в этот List добавлять организации в ходе выполнения кода ниже
                        List<Organization> orgs = new ArrayList<>();
                        //в этот массив спарсим из json списки URL:    напр по ключу EXPERIENCEurl получим массив String[] urls = ["", "http://Organization2.ru"]
                        String[] urls = request.getParameterValues(type.name() + "url");
                        //и далее идем по списку всех организаций(values - это имена организаций)
                        for (int i = 0; i < values.length; i++) {
                            //присваиваем в String name имя очередной в цикле организации для проверки на "пустое" и создания объекта new Link(name, urls[i]) в дальнейшем
                            String name = values[i];
                            //если имя организации не пустое тогда обрабатываем (если пустое тогда просто пропускаем ее обработку):
                            if (!HtmlUtil.isEmpty(name)) {
                                //создаем лист для списка позиций в текущей (в цикле) организации:
                                List<Organization.Position> positions = new ArrayList<>();
                                //префикс-счетчик для позиций. в edit.jsp это ${counter.index}. то создаем стрингу с порядковой цифрой в конце
                                String pfx = type.name() + i;  //напр EXPERIENCE0 - это префикс для первой организации в массиве values[i=0]
                                                                //а, напр EXPERIENCE1 - это для второй организации в массиве values[i=1] при второй итерации for

                                //далее берем в массив, из post по ключу EXPERIENCE[i]startDate, список всех startDates от одной организации(текущей в for)
                                String[] startDates = request.getParameterValues(pfx + "startDate");//напр по ключу EXPERIENCE1startDate получим массив ["", "01/2015"]
                                //и берем в массив, из post по ключу EXPERIENCE[i]endDate, список всех endDate от одной организации(текущей в for)
                                String[] endDates = request.getParameterValues(pfx + "endDate");
                                //и берем в массив, из post по ключу EXPERIENCE[i]title, список всех title от одной организации(текущей в for)
                                String[] titles = request.getParameterValues(pfx + "title");
                                //и берем в массив, из post по ключу EXPERIENCE[i]description, список всех description от одной организации(текущей в for)
                                String[] descriptions = request.getParameterValues(pfx + "description");
                                //далее цикл по заголовкам titles(это название позиции(должности). если нет названия, то считаем что и startDate,endDate,description тоже нет в post)
                                for (int j = 0; j < titles.length; j++) {
                                    if (!HtmlUtil.isEmpty(titles[j])) {//если заголовок titles пустой, то считаем, что позиции нет и не обрабатываем
                                        //если не пустой(, то спарсиваем даты,) создаем объекты позиции от текущей организации и кладем их в List<Organization.Position> positions:
                                        positions.add(new Organization.Position(DateUtil.parse(startDates[j]), DateUtil.parse(endDates[j]), titles[j], descriptions[j]));
                                    }
                                }
                                //далее создаем объект текущей, во внешнем for, организации и добавляем ее в спец созданный для этого выше List<Organization> orgs
                                orgs.add(new Organization(new Link(name, urls[i]), positions));
                            }
                        }
                        //и обновляем Резюме
                        r.setSection(type, new OrganizationSection(orgs));
                        break;
                }
            }
        }//выходим из for, который для Section

        //определяем: что мы делали перед вызовом edit.jsp в listJstl.jsp - нажимали на кнопку создать или на кнопку редактировать
        if (isCreate) {
            storage.save(r);
        } else {
            storage.update(r);
        }
        //storage.update(r);//добавляем в базу
        response.sendRedirect("resume");//делаем редирект на список резюме(в сервлет)
    }

    //первоначально в этот метод сервлета нас перебросит из index.html.
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //возьмем из request, из его параметров пришедшего get запроса(/resume?uuid=...&action=...) "uuid" и "action"
        //чтобы выполнять действия. те будем принимать эти параметры из гет запроса.
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");

    //тк начали использовать jsp- закоментировали код, который в самом низу страницы.
    //нам надо сделать здесь ссылку на нужный jsp файл
    //в запросе-request есть параметры (request.getParameter("name"))
    //а еще есть атрибуты, которые нужны когда мы находимся внутри Томкат//
    //и с пом. request.setAttribute() можно добавить атрибуты в объект севлета-в request
    //эти атрибуты живут только когда выполняется метод doGet.
    //здесь в атрибуте "resumes" будет список наших резюме.
    //и в самом файле  jsp мы делаем request.getAttribute("resumes") те достаем этот список.

        //если в параметрах гет запроса action==null, то выводим табличку из listJstl.jsp.
        //это происходит при старте сервера(приходит Get запрос без параметров)
        if(action==null) {
            //в атрибуты request кладем list со всеми имеющимися резюме- отсортированными, а в listJstl.jsp достанем
            //его для формирования таблички со всеми имеющимися Резюме:
            request.setAttribute("resumes", storage.getAllSorted());//getAllSorted() возвратит List<Resume>
            //и далее функциональность, которая ссылается(вызывает его) на нужный файл jsp (путь от корня web)
            //forward означает что мы не выходя из Томката мы передаем объекты request, response обработчику jsp Джасперу
            request.getRequestDispatcher("/WEB-INF/jsp/listJstl.jsp").forward(request, response);
            return;//возвращаем из сервлета соответственно
        }

        //если action!=null(те пришел Get запрос с параметрами) то сделаем функциональность в зависимости от значения
        //параметра action по switch. (исп стрингу в свиче).
        Resume r;
        switch (action) {
            case "delete"://если мы хотим удалить резюме, то мы его удаляем из стораджа
                storage.delete(uuid);//напомню, что uuid пришел в параметрах Get запроса и мы его выше достали: String uuid = request.getParameter("uuid");
                //после удаления резюме хорошо бы было чтобы в строке браузера пропали запросы после знака ?
                //для этого сделаем редирект - те браузер сам снова запросит этот сервлет и откроет уже обновленную страницу
                // уже без удаленного резюме в таблице:
                response.sendRedirect("resume");//в параметры здесь подаем строку с URL (root'м является контекст)
                return;//не break а return тк выходим из сервлета тк функциональность кот ниже нам не нужна сейчас
            case "view"://если мы хотим посмотреть или редактировать резюме, то мы его загружаем из стораджа и...//и далее в коде, после закрытия switch, отправляем в нужный jsp
                r = storage.get(uuid);
                break;
            case "add"://берем пустое Resume, чтобы открыть форму в edit.jsp с пустыми полями. далее заполняем и делаем Post (submit из формы из edit.jsp)
                // в котором будет storage.update()  (!!! НЕ save(), а то проверка на существование(Exist) не пройдет)
                r = Resume.EMPTY;
                break;
            case "edit"://при action=edit нам надо отобразить форму для редактирования.
                r = storage.get(uuid);//получаем объект Резюме (берем по uuid из параметров Get запроса)

                //для правильного вывода формы в представлении edit.jsp надо чтобы у полученного объекта Резюме в его мапе section были представленны все возможные ключи-ЕНУМЫ хотя бы даже с пустыми значениями.
                //те далее сформировываем "правильный" объект Резюме, который отошлем в отображение формы edit.jsp.
                //логика дальнейшего кода такова: если в мапе section объекта Резюме присутствует определенные ключ-ЕНУМ-его значение-секция тогда его перезаписываем,
                // если отсутствует- добавляем в мапу section отсутствующий ключ-ЕНУМ со значеним с пустым объектом(EMPTY)
                //в случае с ключами-ЕНУМами EXPERIENCE и EDUCATION (в значениях которых объект OrganizationSection внутри которого Лист с Organization, где в каждой Organization есть Лист с Position и Линк с емейлом)
                //в случае отсутствия этих ключей в мапе- добавляем эти ключи в мапу со значением-объект OrganizationSection с листом с пустым объектом.
                //в случае присутствия значения у ключа(объекта OrganizationSection с листом с Organization )- добавляем на первый(0) индекс в этот лист Organization пустую организацию,
                //а далее в этом листе будут располагаться уже существующие организации. А каждый объект существующей организации (в этом листе) уже обязательно содержит в себе (такой конструктор) лист с позициями
                //и далее в коде добавим в этот лист с позициями (в каждую организацию-в цикле) пустую позицию, причем поставим ее на первое место в листе (на индекс 0)
                //Это добавление пустых объектов на первое место, при редактировании в форме, нужно для добавления новых Organization в Лист объекта OrganizationSection и новых Position в лист объекта Organization.\
                //те что бы в форме редактирования отображались EMPTY поля для их заполнения новыми данными.


                //для этого, далее в цикле пройдем по всем возможным ЕНУМам-ключам мапы в SectionType. (те объект Резюме содержит в себе мапу section, а в этой мапе лежат(имеются) не все ЕНУМЫ из SectionType)
                //те когда определенный ключ-ЕНУМ отсутствует в Мапе (и соответственно отсутствует и значение(SectionType) по ключу) тогда кладем в мапу этот ключ-ЕНУМ с пустым значением EMPTY.
                //а когда определенный ключ-ЕНУМ уже есть в мапе - тогда перезаписываем его в мапе этого объекта
                for (SectionType type : SectionType.values()) {//запускаем цикл в котором перебираем все ЕНУМЫ из SectionType с помощью переменной type(она берет в себя конкретный, очередной ЕНУМ)
                    Section section = r.getSection(type);//ссылке типа интерфейс. присваиваем ей значение мапы(не ключ) от объекта Резюме. это будет TextSection или  ListSection или OrganizationSection или null
                    switch (type) {//запускаем оператор множественного выбора
                        case OBJECTIVE:
                        case PERSONAL:
                            if (section == null) {//в случае отсутствия у ключей OBJECTIVE или PERSONAL в мапе section его значения, в качестве значения переменной Section section назначаем пустой объект TextSection.EMPTY
                                section = TextSection.EMPTY;
                            }
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            if (section == null) {//в случае отсутствия у ключей ACHIEVEMENT или QUALIFICATIONS в мапе section его значения, в качестве значений переменной Section section назначаем пустой объект ListSection.EMPTY
                                section = ListSection.EMPTY;
                            }
                            break;
                        case EXPERIENCE://в случае отсутствия у ключей EXPERIENCE или EDUCATION в мапе section его значения,
                        case EDUCATION:
                            OrganizationSection orgSection = (OrganizationSection) section;//кастуем section к типу OrganizationSection
                            List<Organization> emptyFirstOrganizations = new ArrayList<>();//создаем ArrayList для пустых(EMPTY) организаций
                            emptyFirstOrganizations.add(Organization.EMPTY);//добавляем в этот лист пустую организацию
                            if (orgSection != null) {//проверяем наличие значения(объекта Organization) у ключа EXPERIENCE или EDUCATION и в случае, если ЗНАЧЕНИЕ ЕСТЬ(ключу-ЕНУМУ сопоставленно значение- объект OrganizationSection):
                                for (Organization org : orgSection.getOrganizations()) {//в цикле идем по листу в объекте OrganizationSection в котором лежат Organization
                                    List<Organization.Position> emptyFirstPositions = new ArrayList<>();//создаем ArrayList для позиций (каждая Organization содержит в себе Лист с Position и Linc homePage)
                                    emptyFirstPositions.add(Organization.Position.EMPTY);//в этот ArrayList для позиций добавляем пустую позицию. (на  индекс 0 АррайЛиста она станет)
                                    emptyFirstPositions.addAll(org.getPositions());//и также добавляем все пустые позиции
                                    emptyFirstOrganizations.add(new Organization(org.getHomePage(), emptyFirstPositions));// в АррайЛист, где уже лежит одна пустая организация(на нулевом индексе) мы еще добавляем и существующую
                                }
                            }
                            section = new OrganizationSection(emptyFirstOrganizations);//эта строка выполнится в любом случае(независт что в if())
                                                        //в конструктор OrganizationSection подаем ArrayList в котором лежит пустой объект Organization.EMPTY или пустой на первом месте, а далее существующие
                            break;
                    }
                    r.setSection(type, section);//перезаписываем в мапу
                }
                break;
//                r = storage.get(uuid);//достаем резюме и ниже в коде кладем его в атрибуты и перенаправляем на view.jsp или edit.jsp
//                //далее функционал для добавления организации.
//                //в цикле for пройдемся по массиву состоящему из 2-х енумов
//                for (SectionType type : new SectionType[]{SectionType.EXPERIENCE, SectionType.EDUCATION}) {
//                    //далее берем секцию
//                    OrganizationSection section = (OrganizationSection) r.getSection(type);
//                    //и создаем список организаций с первой пустой организацией
//                    List<Organization> emptyFirstOrganizations = new ArrayList<>();
//                    //добавляем пустую организацию, которая пойдет в jsp на отрисовку (в дб оно сохраняться не будет)
//                    //паттерн спешал-кейс- заранее создаем пустой объект (для специального случая) и сейчас его долбавляем:
//                    //в классе Organization создали константу EMPTY - пустой объект класса Organization
//                    //теперь если в резюме нет секций, то в edit.jsp появится пустая:
//                    emptyFirstOrganizations.add(Organization.EMPTY);
//                    //далее если у нас в резюме уже есть не пустая секция, то все организации, которые есть в этой секции
//                    //мы добавляем в этот список emptyFirstOrganizations:
//                    if (section != null) {
//                        for (Organization org : section.getOrganizations()) {
//                            List<Organization.Position> emptyFirstPositions = new ArrayList<>();
//                            //и тоже самое делаем с позициями- если нам надо вставить в рабочую организацию новую позицию-
//                            //тоже у нас будет первая позиция пустая-EMPTY:
//                            emptyFirstPositions.add(Organization.Position.EMPTY);
//                            //потом добавили все остальные позиции:
//                            emptyFirstPositions.addAll(org.getPositions());
//                            //и в emptyFirstOrganizations мы добавили вот эту организацию, которая текущая в цикле: for (Organization org : section.getOrganizations()){...
//                            emptyFirstOrganizations.add(new Organization(org.getHomePage(), emptyFirstPositions));
//                        }//то мы на каждую организацию и на каждую позицию спереди добавили пустую.
//                    }
//                    //далее вставляем эту секцию и ниже передаем Резюме на перерисовку
//                    r.setSection(type, new OrganizationSection(emptyFirstOrganizations));
//                }
//                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        //отправляем в нужный jsp:
        request.setAttribute("resume", r);//кладем имеющееся или сформированное резюме в атрибуты
        request.getRequestDispatcher(//перенаправляем на view.jsp или edit.jsp
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);//в зависимости от action - форвардим в нужный jsp.
        //отличие форварда от редиректа- форвард оставляет нас на этой же странице, а редирект загружает другую или перегружает текущую заново
    }




//----------------------------------------------------------------------------------------------------------------------
        //всю эту закоментированную функциональность мы воспроизвели с пом jsp без jstl.

        //1 ставим кодировку "UTF-8" в самом начале данного метода чтобы сервлет ее подхватил
        //2 ставим кодировку "UTF-8"  можно еще проверить кодировку в браузере
        //3 альтернатива //4-ке
        //4 во вспомогательный хедер Content-Type еще надо поставить кодировку
        //5 request.getParameter() смотрит что пришло в  параметрах GET запроса из браузера и присваиваем это в String name
        //  у request посмотри Evaluate Expression
        //6 из объекта response берем выходной поток getWriter() (символьный поток) и чего нибудь выводим: write(...)

//        request.setCharacterEncoding("UTF-8");//1
//        response.setCharacterEncoding("UTF-8");//2
//      response.setHeader("Content-Type", "text/html; charset=UTF-8");//3
//        response.setContentType("text/html; charset=UTF-8");//4

//        String name = request.getParameter("name");//5
//        response.getWriter().write(name == null ? "Hello Resumes!" : "Hello " + name + '!');//6

    // вместо "Hello Resumes!" по урлу:контекст/resume покажем табличку resume из дб.
        // storage понадобится в этом сервлете. storage'm будет sqlStorage соотв.
//        Writer writer = response.getWriter();
//        writer.write(
//                "<html>\n" +
//                        "<head>\n" +
//                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
//                        "    <link rel=\"stylesheet\" href=\"css/style.css\">\n" +
//                        "    <title>Список всех резюме</title>\n" +
//                        "</head>\n" +
//                        "<body>\n" +
//                        "<section>\n" +
//                        "<table border=\"1\" cellpadding=\"8\" cellspacing=\"0\">\n" +
//                        "    <tr>\n" +
//                        "        <th>Имя</th>\n" +
//                        "        <th>Email</th>\n" +
//                        "    </tr>\n");
//        for (Resume resume : storage.getAllSorted()) {
//            writer.write(
//                    "<tr>\n" +
//                            "     <td><a href=\"resume?uuid=" + resume.getUuid() + "\">" + resume.getFullName() + "</a></td>\n" +
//                            "     <td>" + resume.getContact(ContactType.MAIL) + "</td>\n" +
//                            "</tr>\n");
//        }
//        writer.write("</table>\n" +
//                "</section>\n" +
//                "</body>\n" +
//                "</html>\n");
   }


