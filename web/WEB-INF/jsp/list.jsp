<%--
  Created by IntelliJ IDEA.
  User: Vladimir
  Date: 13.01.2022
  Time: 9:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список всех резюме</title>
</head>
<body>
<%--мы создали include - хеадер и футор, которые можно включать во все странички --%>
<jsp:include page="fragments/header.jsp"/>
<section>
    <table border="1" cellpadding="8" cellspacing="0">
        <%--статический контент --%>
        <tr>
            <th>Имя</th>
            <th>Email</th>
            <th></th><%--столбик для ссылки для делите --%>
            <th></th><%--столбик для ссылки для едит --%>
        </tr>
        <%--динамический контент делаем чз Ява вставки. потом Джаспер в Томкате перекомпилит их в сервлет с пом writer.write(...)
        а request.getAttribute("resumes") это Джаспер имеет доступ к request сервлета и берет оттуда заданные там атрибуты(доступ не только к request)
        <%...%> используем для вставки Ява кода, <%=...%> используем для вывода на аут(внешнего вывода) из Ява кода--%>
        <%
            for (Resume resume : (List<Resume>) request.getAttribute("resumes")) {
        %>
        <tr>
            <%--в ячейке будет отображаться ссылка с параметрами запроса гет после знака ?.
            формируем ее делая ссылку на страницу: контекст/resume?uuid...=resume.getUuid()  --%>
            <td>
                <a href="resume?uuid=<%=resume.getUuid()%>"><%=resume.getFullName()%></a><%--заполняем ячейки поля Имя --%>
            </td>
            <td>
                <%=resume.getContact(ContactType.MAIL)%><%--заполняем ячейки поля Email --%>
            </td>

        </tr>
            <%-- теперь надо закрыть for --%>
        <%
            }
        %>
    </table>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>