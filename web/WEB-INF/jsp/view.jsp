<%@ page import="com.urise.webapp.model.TextSection" %><%--директива page для import классов к которым(и их членам) обращаемся на этой странице--%>
<%@ page import="com.urise.webapp.model.ListSection" %><%--директива page для import классов к которым(и их членам) обращаемся на этой странице--%>
<%@ page import="com.urise.webapp.model.OrganizationSection" %><%--директива page для import классов к которым(и их членам) обращаемся на этой странице--%>
<%@ page import="com.urise.webapp.util.HtmlUtil" %><%--директива page для import классов к которым(и их членам) обращаемся на этой странице
  Created by IntelliJ IDEA.
  User: Vladimir
  Date: 14.01.2022
  Time: 10:33
  To change this template use File | Settings | File Templates.

  Показываем одно резюме. Те реализуем операцию Read из CRUD
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %><%--директива page - настройки jsp--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--директива taglib - подключили библиотеку стандартных тегов jstl к этому файлу jsp--%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css"><%--видит или style.css или include(чтобы увидел style.css надо у него путь поменять на абсолютный, но тогда include не будет видить ) --%>
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/><%--достали из атрибутов  резюме (в <head>).
    scope="request" означает что резюме из request'а будет браться. бывает еще scope="page"--%>
    <title>Резюме ${resume.fullName}</title><%--задали динамическое название для этой страницы--%>
</head>
<body>
<jsp:include page="fragments/header.jsp"/><%--include - хеадер--%>
<section><%--открыли секцию--%>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2><%--сделали заголовок с сылкой на редактирование--%>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}"><%--в цикле идем по всем контактам и их отображаем --%>
                <%--создаем Bean contactEntry - переменная для обхода мапы. по умолчанию здесь scope="page" те берет contacts с нашей страницы, а не из request'а--%>
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
                <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/><%--<br/> для перевода строки--%>
        </c:forEach><%--метод toHtml для красивого отображения всех контактов-енумов--%>
    <p>


    <hr><%--<br/> далее надо отобразить секции. это будет табличка в которой мы пойдем по секциям--%>
    <table cellpadding="2">
        <c:forEach var="sectionEntry" items="${resume.sections}"><%--в цикле берем Entry (в переменную var="sectionEntry") у resume.sections--%>
            <%--далее надо объявить тип этой переменной (var="sectionEntry"), чтобы можно было ее использовать в Ява (jsp будет знать ее тип): --%>
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.Section>"/>
            <%--далее, объявляем 2-е переменные, чтобы было удобнее их использовать:--%>
            <c:set var="type" value="${sectionEntry.key}"/>
            <c:set var="section" value="${sectionEntry.value}"/>
            <%--<br/> далее отобразим заголовок--%>
            <%--<br/> сначала для Ява вставок определим переменную id="section"--%>
            <jsp:useBean id="section" type="com.urise.webapp.model.Section"/>


            <%--и отображаем заголовок таблицы  --%>
            <tr>
                <td colspan="2">
                    <h2><a name="type.name">${type.title}</a></h2>
                </td>
            </tr>

            <c:choose><%--оператор  choose - это множественный выбор --%>
                <%--проверка- если ключ-енум 'OBJECTIVE', то в этой же строчке???(</tr> лишние?) делаем еще одну селл ячейку таблички и говорим getContent() иначе проваливаемся--%>
                <c:when test="${type=='OBJECTIVE'}">
                    <tr>
                        <td colspan="2">
                            <h3><%=((TextSection) section).getContent()%></h3>
                        </td>
                    </tr>

                    <%--проверка- если ключ-енум 'PERSONAL', то в этой же строчке???(</tr> лишние?) делаем еще одну селл ячейку таблички и говорим getContent() иначе проваливаемся--%>
                </c:when>
                <c:when test="${type=='PERSONAL'}">
                    <tr>
                        <td colspan="2">
                            <%=((TextSection) section).getContent()%>
                        </td>
                    </tr>

                    <%--проверка- если ключ-енум 'QUALIFICATIONS' или 'ACHIEVEMENT', то в этой же строчке???(</tr> лишние?) делаем еще одну селл ячейку таблички и говорим getContent() иначе проваливаемся--%>
                </c:when>
                <c:when test="${type=='QUALIFICATIONS' || type=='ACHIEVEMENT'}">
                    <tr>
                        <td colspan="2">
                            <ul><%--выводим списком:--%>
                                <c:forEach var="item" items="<%=((ListSection) section).getItems()%>">
                                    <li>${item}</li>
                                </c:forEach>
                            </ul>
                        </td>
                    </tr>
                </c:when>


                <c:when test="${type=='EXPERIENCE' || type=='EDUCATION'}">
                    <%--для 'EXPERIENCE' или 'EDUCATION' идет цикл по организациям:--%>
                    <c:forEach var="org" items="<%=((OrganizationSection) section).getOrganizations()%>">
                        <tr><%--в цикле первой строчкой выводим имя организации и url организации--%>
                            <td colspan="2">
                                <c:choose>
                                    <c:when test="${empty org.homePage.url}"><%--если url пустой, мы просто имя выводим--%>
                                        <h3>${org.homePage.name}</h3>
                                    </c:when>
                                    <c:otherwise><%--а сли url не пустой, тогда выводим в формате гиперссылки:--%>
                                        <h3><a href="${org.homePage.url}">${org.homePage.name}</a></h3>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <%--далее идем по позициям. это тоже не строчка- это цикл по позициям организации
                        каждую позицию мы выводим и пользуемся утильным методом  утильного класса HtmlUtil для форматирования даты--%>
                        <c:forEach var="position" items="${org.positions}">
                            <jsp:useBean id="position" type="com.urise.webapp.model.Organization.Position"/>
                            <tr>
                                <td width="15%" style="vertical-align: top"><%=HtmlUtil.formatDates(position)%><%--форматируем дату у позиции--%>
                                </td>
                                    <%--далее выводим position.title и position.description в след. ячейке с переводом строки--%>
                                <td><b>${position.title}</b><br>${position.description}</td>
                            </tr>
                        </c:forEach>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>
    </table>
    <br/>
    <button onclick="window.history.back()">ОК</button><%--кнопка- возвращает предидущее окно--%>
</section>
<jsp:include page="fragments/footer.jsp"/><%--include - футер--%>
</body>
</html>