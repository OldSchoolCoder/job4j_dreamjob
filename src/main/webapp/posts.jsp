<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ru.job4j.dream.store.PsqlStore" %>
<%@ page import="ru.job4j.dream.model.Post" %>
<%@ page import="java.util.Collection" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!doctype html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-BmbxuPwQa2lc/FVzBcNJ7UAyJxM6wuqIj61tLrc4wSX0szH/Ev+nYRRuWlolflfl" crossorigin="anonymous">

    <link href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" rel="stylesheet">
    <title>Работа мечты</title>
</head>

<body>
<div class="container pt-3">
    <div class="collapse" id="navbarToggleExternalContent">
        <div class="bg p-4">
            <ul class="nav flex-column">
                <li class="nav-item">
                    <a class="nav-link text-secondary fw-light"
                       href="<%=request.getContextPath()%>/posts.do">Вакансии</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-secondary fw-light" href="<%=request.getContextPath()%>/candidates.do">Кандидаты</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-secondary fw-light" href="<%=request.getContextPath()%>/post/edit.jsp">Добавить
                        вакансию</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-secondary fw-light" href="<%=request.getContextPath()%>/candidate/edit.jsp">Добавить
                        кандидата</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-secondary fw-light" href="<%=request.getContextPath()%>/login.jsp">
                        <c:out value="${user.name}"/> | Выйти
                    </a>
                </li>
            </ul>
        </div>
    </div>
    <nav class="navbar navbar-light " style="background-color: #ffffff;">
        <div class="container-fluid">
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarToggleExternalContent" aria-controls="navbarToggleExternalContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
        </div>
    </nav>
</div>
<div class="container pt-3">
    <div class="card border-warning mb-3">
        <div class="card-header fw-light">
            Вакансии
        </div>
        <div class="card-body fw-light">
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">
                        <p class="fw-light">Названия</p>
                    </th>
                    <th scope="col">
                        <p class="fw-light">Edit</p>
                    </th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${posts}" var="post">
                    <tr>
                        <td>
                            <c:out value="${post.name}"/>
                        </td>
                        <td>
                            <a href='<c:url value="/post/edit.jsp?id=${post.id}"/>'>
                    <span style="font-size: 1rem;">
                      <span style="color: rgb(206, 203, 14);">
                        <i class="fas fa-edit"></i>
                      </span>
                    </span>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- JavaScript Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW"
        crossorigin="anonymous"></script>

</body>

</html>
