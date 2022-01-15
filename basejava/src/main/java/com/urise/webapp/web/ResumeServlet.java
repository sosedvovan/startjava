package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.ContactType;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
//Alt+F9- при дебаге - перевеси дебаг на курсор и далее Alt+F8 и посмотреть текущие возвраты от методов, к которым обращаемся чз объекты или имена классов

public class ResumeServlet extends HttpServlet {

    private Storage storage;// = Config.get().getStorage();//можно и здесь инициализировать без переопределения init().

    //след. строчка отключает конфиг.проперти тк они пока не работают:
    //private Storage storage = new SqlStorage("jdbc:postgresql://localhost:5432/resumes", "postgres", "00821821");

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

    //этот метод принимает все Post запросы
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //говорим, что request принимает русские буквы (имя пишем по русски)
        request.setCharacterEncoding("UTF-8");
        //из тела запроса достанем 2-а параметра:
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        //по uuid возьмем резюме:
        //при добавлении нового резюме(пост запрос) здесь можно сделать проверку uuid- если оно существует-загружаем по
        //старому uuid из стораджа, если нет- не загружаем
        Resume r = storage.get(uuid);
        //в этом резюме поменяем значение поля fullName на новое, пришедшее в пост запросе
        r.setFullName(fullName);
        //так же поменяем контактную группу:
        for (ContactType type : ContactType.values()) {//итерируемся по Контактам-енумам
            String value = request.getParameter(type.name());//name() в Енуме возвращает имя его Константы. Берем у request значение по ключу-енуму.
            if (value != null && value.trim().length() != 0) {//если значение по текущему в цикле ключу-енуму не null и не пробелы
                r.addContact(type, value);//если есть значение- добавляем
            } else {
                r.getContacts().remove(type);//если значение было, но мы его удалили при edit, то удаляем
            }
        }
        storage.update(r);//добавляем в базу
        response.sendRedirect("resume");//делаем редирект на список резюме(в сервлет)
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //зададим в request параметр "action" чтобы выполнять действия.
        //те будем принимать эти параметры из тела гет запроса.
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");

    //тк начали использовать jsp- закоментировали код ниже.
    //нам надо сделать зздесь ссылку на нужный jsp файл
    //в запросе-request есть параметры (request.getParameter("name"))
    //а еще есть атрибуты, которые нужны когда мы находимся внутри Томкат//
    //и с пом. request.setAttribute() можно добавить атрибуты в объект севлета-в request
    //эти атрибуты живут только когда выполняется метод doGet.
    //здесь в атрибуте "resumes" будет список наших резюме.
    //и в самом файле  jsp мы делаем request.getAttribute("resumes") те достаем этот список.

        //если в параметрах гет запроса action==null, то выводим табличку из listJstl.jsp.
        if(action==null) {
            request.setAttribute("resumes", storage.getAllSorted());
            //и далее фенкциональность, которая ссылается на нужный файл jsp (путь от корня web)
            //forward означает что мы не выходя из Томката мы передаем объекты request, response обработчику jsp Джасперу
            request.getRequestDispatcher("/WEB-INF/jsp/listJstl.jsp").forward(request, response);
            return;//возвращаем из сервлета соответственно
        }

        //если action!=null то сделаем функциональность делит по switch. (исп стрингу в свиче)
        Resume r;
        switch (action) {
            case "delete"://если мы хотим удалить резюме, то мы его удаляем из стораджа
                storage.delete(uuid);
                //после удаления резюме хорошо бы было чтобы в строке браузера пропали запросы после знака ?
                //для этого сделаем редирект - те браузер сам снова запросит этот сервлет и откроет уже обновленную страницу
                // уже без удаленного резюме в таблице:
                response.sendRedirect("resume");
                return;//выходим из сервлета тк функциональность кот ниже нам не нужна сейчас
            case "view"://если мы хотим посмотреть или редактировать резюме, то мы его загружаем из стораджа и...
            case "edit"://и далее отправляем в нужный jsp
                r = storage.get(uuid);//достаем резюме и ниже в коде кладем его в атрибуты и перенаправляем на view.jsp или edit.jsp
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        //отправляем в нужный jsp:
        request.setAttribute("resume", r);//кладем резюме в атрибуты
        request.getRequestDispatcher(//перенаправляем на view.jsp или edit.jsp
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);//в зависимости от action - форвардим в нужный jsp.
        //отличие форварда от редиректа- форвард оставляет нас на этой же странице, а редирект загружает другую или перегружает текущую заново
    }




//----------------------------------------------------------------------------------------------------------------------
        //всю эту закоментированную функциональность мы воспроизвели с пом jsp.

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


