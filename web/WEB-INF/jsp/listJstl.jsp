<%--
  Created by IntelliJ IDEA.
  User: Vladimir
  Date: 13.01.2022
  Time: 11:03
  To change this template use File | Settings | File Templates.
--%>
<%--чтобы в табличке выводились емейл адресса импортируем ContactType--%>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--подключаем библиотеку тегов jstl к страничке jsp.   jstl-1.2.jar нужен--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список всех резюме</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Имя</th>
            <th>Email</th>
            <th></th>
            <th></th>
        </tr>
        <%--здесь вместо цикла for используем тег forEach --%>
        <%--resumes это list кот items сам берет из заданного атрибута и итерируется с пом переменной var="resume"
        для которой в след строчки создали useBean--%>
        <c:forEach items="${resumes}" var="resume">
            <jsp:useBean id="resume" type="com.urise.webapp.model.Resume"/>
            <tr>
                <td>
                    <a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a><%--формируем ссылку для просмотра резюме action=view --%>
                </td>
                <td>
                        <%--${resume.getContact(ContactType.MAIL)}--%><%--эту строку заменим на:  --%>
                            <%=ContactType.MAIL.toHtml(resume.getContact(ContactType.MAIL))%><%--заполняем ячейки поля Email --%>
                </td>
                <td>
                    <a href="resume?uuid=${resume.uuid}&action=delete"><img src="img/delete.png"></a><%--формируем ссылку для удаления резюме action=delete --%>
                </td>
                <td>
                    <a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a><%--формируем ссылку для редактирования резюме action=edit --%>
                </td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
