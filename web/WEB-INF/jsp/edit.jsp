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
<%@ page import="com.urise.webapp.model.ContactType" %><%--директива: импортировали класс ContactType--%>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page import="com.urise.webapp.model.ListSection" %>
<%@ page import="com.urise.webapp.model.OrganizationSection" %>
<%@ page import="com.urise.webapp.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><%--задали настройки этой jsp страницы--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--подключили к этой jsp страницы jstl - модуль стандартных тегов--%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><%--задали настройки html-составляющей этой jsp страницы--%>
    <link type= "text/css" rel="stylesheet" media="screen" href="css/style.css"><%--подключили style.css  понравившиеся стили можно копипастить с др сайтов --%>
    <style><%--без этого css стили не подключались к этой странице--%>
        <%@include file='css/style.css' %>
    </style>

    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/><%--в хедере сказали что в jsp вставках будем использовать специально сформированный
    Bean Resume  кот берем из реквеста scope="request"--%>
    <title>Резюме ${resume.fullName}</title><%--название страницы динамически берем из бина resume, кот получили на предидущей строчке  --%>
</head>
<body>
<jsp:include page="fragments/header.jsp"/><%--include - шапка страницы (вставляемый фрагмент)--%>
<section><%--открываем section и далее создаем форму-ввода для post запроса, action="resume" означает, что данные из формы мы отправляем по URL "resume" (те
в наш сервлет в его метод doPost(), где они будут проанализированы для получения данных в формате листа с парами ключ/значение)--%>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded"><%--enctype=... означает, что в форме будут поля--%>
        <input type="hidden" name="uuid" value="${resume.uuid}"><%--скрытое поле в котором лежит скрытый от пользователя uuid резюме, Этот uuid должен содержаться
        в этой форме тк он пригодится в методе doPost() сервлета(куда данная форма и отправит этот пост запрос там его достанем как параметр)--%>
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
                <dt>${type.title}</dt><%--у каждого енума берем его поле title- это стринга с переводом енума на русский--%>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd><%--${resume.getContact(type)} иначе Ява вставкой можно заменить,
                 если старая версия Томката не поддерживает.   type.name() в Енуме возвращает имя его Константы--%>
            </dl>
        </c:forEach>
        <%--то вывели в форму все возможные контакты(в виде title енумов) и их значения(если значениея нет, то пустое поле отображается)--%>

        <%--заготовка для вывода секций- попробуем в один ключ-параметр-name положить несколько разных значений
        <h3>Секции:</h3>
        <input type="text" name="section" size=30 value="1"><br/>
        <input type="text" name="section" size=30 value="2"><br/>
        <input type="text" name="section" size=30 value="3"><br/>--%>
        <%-- <br/> для переноса строки--%>


        <hr><%--подчеркивание--%>
        <%--далее в форме делаем поля для редактирования секций--%>
        <%--цикл по всем секциям(по всем SectionType-енумам с пом переменной var="type" (jsp сам знает походу тип переменной var="type")):--%>
        <c:forEach var="type" items="<%=SectionType.values()%>">
            <%--определяем переменную var="section" как ссылку на объект класса Section для дальнейшего ее использования--%>
            <c:set var="section" value="${resume.getSection(type)}"/>
            <jsp:useBean id="section" type="com.urise.webapp.model.Section"/>

            <h2><a>${type.title}</a></h2><%--создаем заголовок- выводится имя очередного енума в цикле--%>
            <c:choose><%--оператор  choose - это множественный выбор --%>
                <c:when test="${type=='OBJECTIVE'}"><%--если енум 'OBJECTIVE' ниже создаем поле ввода (в котором еще отображаются старые данные, кот можно изменить) --%>
                    <input type='text' name='${type}' size=75 value='<%=section%>'>
                </c:when><%--выводим <%=section%>  - это в section toString() вызывается и выводится содержимое секции--%>

                <c:when test="${type=='PERSONAL'}"><%--если енум 'PERSONAL' ниже создаем поле ввода (в котором еще отображаются старые данные, кот можно изменить) --%>
                    <textarea name='${type}' cols=75 rows=5><%=section%></textarea><%--исп textarea тк там места побольше и можно переносить строку--%>
                </c:when><%--выводим <%=section%>  - это в section toString() вызывается и выводится содержимое секции--%>

                <%--если енум 'QUALIFICATIONS' или 'ACHIEVEMENT' ниже создаем поле ввода (в котором еще отображаются старые данные, кот можно изменить) --%>
                <c:when test="${type=='QUALIFICATIONS' || type=='ACHIEVEMENT'}">
                    <textarea name='${type}' cols=75
                              rows=5><%=String.join("\n", ((ListSection) section).getItems())%></textarea>
                </c:when><%--берем getItems()- (кот возвращает List<String>) и склеиваем все строки чз "\n"--%>
                <%--когда будем делать save- разобьем по "\n" и получится список--%>

                <%--теперь самое сложное. у нас есть список организаций у которого внутри список позиций--%>
                <%--вспомним что в форме ключ это name=... а его значение это value="${...--%>
                <%--в сервлете есть getParametrValues() кот возвращает массив стрингов и по имени параметра берет все значения--%>
                <%--и если у нас нес несколько организаций, то мы можем их все вытащить в массив с их url--%>
                <%--но списки позиций перемешаются и непонятно будет- какая позиция к какой организации подходит--%>
                <%--и надо вводить префиксы чтобы отнести startDate к нужной организации--%>

                <%--секцию берем по организации, выводим название учреждения и сайт учреждения:--%>
                <c:when test="${type=='EXPERIENCE' || type=='EDUCATION'}">
                    <c:forEach var="org" items="<%=((OrganizationSection) section).getOrganizations()%>"
                               varStatus="counter"><%--ввели "counter", чтобы эти организации считать и будем их по номеру разделять
                               те внутри 'EXPERIENCE' будет своя номерация и внутри 'EDUCATION' будет своя--%>

                        <dl><%--выводим в форме заголовки и поля со старыми данными--%>
                            <dt>Название учереждения:</dt>
                            <dd><input type="text" name='${type}' size=100 value="${org.homePage.name}"></dd>
                        </dl>
                        <dl>
                            <dt>Сайт учереждения:</dt>
                            <dd><input type="text" name='${type}url' size=100 value="${org.homePage.url}"></dd>
                            </dd>
                        </dl>

                        <br><%--далее делаем отступ и идем по датам:--%>
                        <div style="margin-left: 30px">
                                <%--делаем цикл по позициям:--%>
                            <c:forEach var="pos" items="${org.positions}">
                                <jsp:useBean id="pos" type="com.urise.webapp.model.Organization.Position"/>
                                <dl>
                                    <dt>Начальная дата:</dt>
                                    <dd>
                                        <input type="text" name="${type}${counter.index}startDate" size=10<%--начальные даты разделяем по типу и по индексу,
                                        кот начинается с 0 : name="${type}${counter.index}startDate"  те для организации №0 в списке будет своя startDate, для №1-своя... --%>
                                               value="<%=DateUtil.format(pos.getStartDate())%>" placeholder="MM/yyyy"><%--показали старое значение и оно же отправится в сабмит--%>
                                    </dd>
                                </dl>

                                <%--далее по аналогии - как и в начальной дате (будет повторение кода и можно делать типизирование):--%>
                                <dl>
                                    <dt>Конечная дата:</dt>
                                    <dd>
                                        <input type="text" name="${type}${counter.index}endDate" size=10<%--counter.index это префикс String pfx в методе doPost в сервлете--%>
                                               value="<%=DateUtil.format(pos.getEndDate())%>" placeholder="MM/yyyy">
                                </dl>
                                <dl>
                                    <dt>Должность:</dt>
                                    <dd><input type="text" name='${type}${counter.index}title' size=75
                                               value="${pos.title}">
                                </dl>
                                <dl>
                                    <dt>Описание:</dt>
                                    <dd><textarea name="${type}${counter.index}description" rows=5
                                                  cols=75>${pos.description}</textarea></dd>
                                </dl>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>
        <button type="submit">Сохранить</button><%--кнопка сохранить- отправить(сабмитит) пост запрос в сервлет resume в его метод doPost()--%>
        <button onclick="window.history.back()">Отменить</button><%--кнопка отменить--%>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/><%--include вставка--%>
</body>
</html>
