<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ru.job4j.dream.store.PsqlStore" %>
<%@ page import="ru.job4j.dream.model.Post" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page import="java.util.Collection" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <!-- CSS only -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script>
        function validate() {
            let counter = 0;
            $('form').find('input, select').each(function () {
                if ($(this).val() == '' || $(this).val() == 'Choose...') {
                    alert('Enter ' + $(this).attr('id'));
                    return false;
                } else {
                    counter++;
                }
            });
            if (counter == 4) {
                $("form").submit();
            }
            return false;
        }
    </script>
</head>

<body>
<%
    String id = request.getParameter("id");
    Candidate candidate = new Candidate(0, "", "TestPhoto", "TestCity");
    if (id != null) {
        candidate = PsqlStore.instOf().findCandidateById(Integer.valueOf(id));
    }
%>
<script>
    $(function () {
        $('#city').click(function () {
            $.get('http://localhost:8080/dreamjob/city').done(function (response) {
                $('#city').html(response);
            });
        });
    });
</script>
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
            <% if (id == null) { %>
            Новый кандидат
            <% } else { %>
            Редактирование кандидата
            <% } %>
        </div>
        <div class="card-body fw-light">
            <form action="<%=request.getContextPath()%>/candidates.do" enctype="multipart/form-data" method="post">
                <div class="input-group mb-3 fw-light">
                    <span class="input-group-text fw-light" id="basic-addon1">Enter</span>
                    <input type="text" name="name" value="<%=candidate.getName()%>" class="form-control"
                           placeholder="Name" aria-label="Username"
                           aria-describedby="basic-addon1" id="name">
                    <input type="hidden" name="id" value="<%=candidate.getId()%>" id="hidden">
                </div>
                <div class="input-group mb-3">
                    <select class="form-select border-warning fw-light" id="city" name="city">
                        <option selected>Choose...</option>
                    </select>
                    <label class="input-group-text fw-light" for="city">City</label>
                </div>
                <div class="mb-3">
                    <label class="form-label fw-light">Photo</label>
                    <input type="file" name="photo" class="form-control fw-light" id="photo">
                </div>
                <button type="submit" onclick="return validate()" class="btn btn-warning fw-light">Submit</button>
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


