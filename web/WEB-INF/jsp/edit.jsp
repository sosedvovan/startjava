<%--
  Created by IntelliJ IDEA.
  User: Vladimir
  Date: 15.01.2022
  Time: 8:51
  To change this template use File | Settings | File Templates.

  несколько слов про пути: у нас так: css/style.css если вначале поставить /, нпример так : /css/style.css, то пути будут отчитываться от localhost:8080, те, например,
  путь resume?uuid отчитывается от сервлета resume/resume?uuid, а если написать /resume?uuid то получим localhost:8080/resume?uuid те выйдем на контекст, а не на сервлет.

Код похож на view.jsp только добавим едит и сейф функции.
--%>
<%@ page import="com.urise.webapp.model.ContactType" %><%--импортировали класс ContactType--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><%--задали настройки этой jsp страницы--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--подключили к этой jsp страницы jstl - модуль стандартных тегов--%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><%--задали настройки html-составляющей этой jsp страницы--%>
    <link type= "text/css" rel="stylesheet" media="screen" href="css/style.css"><%--подключили style.css  понравившиеся стили можно копипастить с др сайтов --%>
    <style><%--без этого css стили не подключались к этой странице--%>
        <%@include file='css/style.css' %>
    </style>

    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/><%--в хедере сказали что в jsp вставках будем использовать Bean Resume кот берем из реквеста scope="request"--%>
    <title>Резюме ${resume.fullName}</title><%--название страницы динамически берем из бина resume, кот получили на предидущей строчке  --%>
</head>
<body>
<jsp:include page="fragments/header.jsp"/><%--include - шапка страницы (вставляемый фрагмент)--%>
<section><%--открываем section и далее создаем форму-ввода для post запроса, action="resume" означает, что данные из формы мы отправляем по URL "resume" (те
в наш сервлет в его метод doPost(), где они будут проанализированы для получения данных в формате листа с парами ключ/значение)--%>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded"><%--enctype=... означает, что в форме будут поля--%>
        <input type="hidden" name="uuid" value="${resume.uuid}"><%--скрытое поле в котором лежит скрытый от пользователя uuid резюме, Этот uuid должен содержаться
        в этой форме тк он пригодится в методе doGet() сервлета(куда данная форма и отправит этот пост запрос там его достанем как параметр)--%>
        <dl><%--<dl> это тег для списка определений--%>
            <dt>Имя:</dt><%--заголовок определения и ниже содержание определения--%>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd><%--поле в кот будет отображаться текущее значение fullName, кот можно изменить
            по ключу-параметру name="fullName" будем доставать значение value=... в методе doPost() сервлета--%>
        </dl>
        <h3>Контакты:</h3><%--заголовок- расмер шрифта h3--%>
        <%--далее исп тег jstl - forEach. итерируемся по ContactType.values- это все типы(енумы) (берем из объекта резюме),--%>
        <%--итерируемся с пом переменной var="type". выводим все контакты, если они пустые - выводим пустые поля--%>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd><%--${resume.getContact(type)} иначе Ява вставкой можно заменить,
                 если старая версия Томката не поддерживает.   type.name() в Енуме возвращает имя его Константы--%>
            </dl>
        </c:forEach>
        <h3>Секции:</h3><%--заготовка для вывода секций- попробуем в один ключ-параметр-name положить несколько разных значений--%>
        <input type="text" name="section" size=30 value="1"><br/><%-- <br/> для переноса строки--%>
        <input type="text" name="section" size=30 value="2"><br/>
        <input type="text" name="section" size=30 value="3"><br/>
        <hr><%--подчеркивание--%>
        <button type="submit">Сохранить</button><%--кнопка сохранить- отправить(сабмитит) пост запрос в сервлет resume в его метод doPost()--%>
        <button onclick="window.history.back()">Отменить</button><%--кнопка отменить--%>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/><%--include вставка--%>
</body>
</html>
