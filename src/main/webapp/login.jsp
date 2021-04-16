<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page import="ru.job4j.dream.store.PsqlStore" %>
<%@ page import="java.util.Collection" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!doctype html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-BmbxuPwQa2lc/FVzBcNJ7UAyJxM6wuqIj61tLrc4wSX0szH/Ev+nYRRuWlolflfl" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script>
        function validate() {
            let counter = 0;
            $('form').find('input').each(function () {
                if ($(this).val() == '') {
                    alert('Enter ' + $(this).attr('id'));
                    return false;
                } else {
                    counter++;
                }
            });
            if (counter == 2) {
                $("form").submit();
            }
            return false;
        }
    </script>
    <title>Авторизация</title>
</head>

<body>
<div class="container pt-3">
    <div class="collapse" id="navbarToggleExternalContent">
        <div class="bg p-4">
            <ul class="nav flex-column">
                <li class="nav-item">
                    <a class="nav-link text-secondary fw-light" href="/dreamjob/posts.do">Вакансии</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-secondary fw-light" href="/dreamjob/candidates.do">Кандидаты</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-secondary fw-light" href="/dreamjob/post/edit.jsp">Добавить
                        вакансию</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-secondary fw-light" href="/dreamjob/candidate/edit.jsp">Добавить
                        кандидата</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-secondary fw-light" href="/dreamjob/login.jsp">
                        | Выйти
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
            <div class="btn-group">
                <button type="button" class="btn btn-warning dropdown-toggle fw-light" data-bs-toggle="dropdown"
                        aria-expanded="false">
                    Авторизация
                </button>
                <ul class="dropdown-menu">
                    <li><a class="dropdown-item fw-light" href="/dreamjob/reg.jsp">Регистрация</a></li>
                </ul>
            </div>
        </div>
        <div class="card-body fw-light">
            <form action="<%=request.getContextPath()%>/auth.do" method="post">
                <div class="mb-3">
                    <label>Почта</label>
                    <input type="text" class="form-control" name="email" id="email">
                </div>
                <div class="mb-3">
                    <label>Пароль</label>
                    <input type="text" class="form-control" name="password" id="password">
                </div>
                <button type="submit" onclick="return validate()" class="btn btn-warning fw-light">Войти</button>
            </form>
        </div>
    </div>
</div>
<!-- JavaScript Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW"
        crossorigin="anonymous"></script>
</body>
</html>

