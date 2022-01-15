<%--
  Created by IntelliJ IDEA.
  User: Vladimir
  Date: 14.01.2022
  Time: 10:33
  To change this template use File | Settings | File Templates.

  Показываем одно резюме
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%--подключили jstl к этому файлу jsp--%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css"><%--видит или style.css или include(чтобы увидел style.css надо у него путь поменять на абсолютный, но тогда include не будет видить ) --%>
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/><%--достали из атрибутов резюме (в шапке). scope="request" означает что резюме из request'а будет браться. бывает еще scope="page"--%>
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
</section>
<jsp:include page="fragments/footer.jsp"/><%--include - футер--%>
</body>
</html>