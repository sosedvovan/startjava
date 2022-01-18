<%--
  Created by IntelliJ IDEA.
  User: Vladimir
  Date: 13.01.2022
  Time: 11:03
  To change this template use File | Settings | File Templates.

  Эту страницу мы увидим первой при запуске сервера(редирект на нее с index.html происходит).
  Она отображает страничку со всеми имеющимися в базе резюме.
  На ней имеются ссылки для всех CRUD операций.

--%>

<%@ page import="com.urise.webapp.model.ContactType" %><%--чтобы в табличке выводились емейл адресса импортируем ContactType в директиве page--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><%--настройки jsp--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--подключаем библиотеку тегов jstl к страничке jsp.   jstl-1.2.jar нужен--%>

<html><%--открываем тег--%>
<head><%--открываем тег--%>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><%--настройки html--%>
    <link rel="stylesheet" href="css/style.css"><%--подключили стили. root(папка с этим jsp) : /jsp/css/style.css--%>
    <title>Список всех резюме</title><%--название страницы--%>
</head><%--закрываем тег--%>
<body><%--закрываем тег--%>
<jsp:include page="fragments/header.jsp"/><%--вставка созданной ними шапки. root(папка с этим jsp) : /jsp/fragments/header.jsp --%>
<section><%--открываем тег--%>
    <a href="resume?action=add"><img src="img/add.png"></a><%--ссылка на добавление нового резюме. посылает get запрос в doGet() нашего сервлета
     root для пути - от папки web получается для этого файла. при наведении курсора показывает что путь не найден, но в браузере картинки отображаются???--%>
    <br><%--нарисовали горизонтальную линию--%>
    <table border="1" cellpadding="8" cellspacing="0"><%--Атрибут cellpadding — определяет расстояние между границей ячейки и ее содержимым.
    cellspacing, тега <TABLE>, используется для указания расстояния между границами ячеек--%>
        <tr><%-- <tr> (table row) используется для определения строк в таблице. Он может содержать один или несколько тегов <th> (создает ячейки с заголовками) или <td> (создает ячейки с данными таблицы).--%>
            <th>Имя</th>
            <th>Email</th>
            <th></th>
            <th></th>
        </tr>
        <%--здесь как-бы вместо цикла for- используем тег forEach от jstl--%>
        <%--resumes это List<Resume> кот items сам берет из заданного в doGet() сервлета атрибута и итерируется с пом переменной var="resume"
        для которой в след строчки создали useBean--%>
        <c:forEach items="${resumes}" var="resume">
            <jsp:useBean id="resume" type="com.urise.webapp.model.Resume"/>
            <tr><%--теперь создаем ячейки с данными таблицы (заголовок уже есть)--%>
                <td>
                    <a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a><%--первая ячейка строки, в ней формируем ссылку(Get запрос в doGet() сервлета) для просмотра резюме action=view
                    при нажатии на эту ссылку (напр Name1) мы посылаем Get запрос с определенными здесь параметрами в наш сервлет(в doGet()) по его пути:resume?uuid=${...    root в данном случае это контекст--%>
                </td>
                <td>
                        <%--${resume.getContact(ContactType.MAIL)}--%><%--эту строку заменим на:  --%>
                            <%=ContactType.MAIL.toHtml(resume.getContact(ContactType.MAIL))%><%--вторая ячейка строки, берем Email из контактов--%>
                                <%--к енуму MAIL обращаемся через имя класса(енум-статик), вызываем его метод toHtml() в аргументы которого кладем стрингу(емейл)--%>
                </td>
                <td>
                    <a href="resume?uuid=${resume.uuid}&action=delete"><img src="img/delete.png"></a><%--третья ячейка строки, формируем ссылку(Get запрос в doGet() сервлета) для удаления резюме action=delete --%>
                </td>
                <td>
                    <a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a><%--четвертая ячейка строки, формируем ссылку(Get запрос в doGet() сервлета) для редактирования резюме action=edit --%>
                </td>
            </tr>
        </c:forEach><%--выходим из цикла--%>
    </table><%--закрываем тег--%>
</section><%--закрываем тег--%>
<jsp:include page="fragments/footer.jsp"/><%--вставка созданной ними шапки. root(папка с этим jsp) : /jsp/fragments/header.jsp --%>
</body><%--закрываем тег--%>
</html><%--закрываем тег--%>
