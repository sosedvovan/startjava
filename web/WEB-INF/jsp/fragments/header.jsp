<%--
  Created by IntelliJ IDEA.
  User: Vladimir
  Date: 13.01.2022
  Time: 10:36
  To change this template use File | Settings | File Templates.

    include- это родной тег jsp, но есть еще стандартные тэги jstl, подключаемые чз библиотечку или из фреймверков или собственные.
    jstl нужен для того чтобы выносить из jsp файла Ява код. В Томкат надо нести с собой библиотечку jstl.
  include (делаем тег для вставки в jsp файл) с сылкой на наш сервлет
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<header><a href="resume">Управление резюме</a></header>
<hr/>
