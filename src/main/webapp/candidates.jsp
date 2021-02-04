<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ru.job4j.dream.store.PsqlStore" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page import="java.util.Collection" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<!doctype html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">

    <link href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" rel="stylesheet">
    <title>Hello, world!</title>
</head>

<body>
<div class="container pt-3">
    <div class="card border-warning mb-3">
        <div class="card-header">
            Candidates
        </div>
        <div class="card-body">
            <table class="table border-warning table-hover table-striped">
                <thead class="table-light">
                <tr>
                    <th scope="col">Name</th>
                    <th scope="col">Edit</th>
                    <th scope="col">View</th>
                    <th scope="col">Download</th>
                    <th scope="col">Delete</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${candidates}" var="can">
                    <tr>
                        <td>
                            <c:out value="${can.name}"/>
                        </td>
                        <td>
                            <a href='<c:url value="/candidate/edit.jsp?id=${can.id}"/>'>
                                <span style="font-size: 1rem;">
                                    <span style="color: rgb(206, 203, 14);">
                                        <i class="fas fa-edit"></i>
                                    </span>
                                </span>
                            </a>
                        </td>
                        <td>
                            <img src="<c:url value='/download?name=${can.photo}'/>" width="30px" height="30"/>
                        </td>
                        <td>
                            <a href="<c:url value='/download?name=${can.photo}'/>">
                                <span style="font-size: 1rem;">
                                    <span style="color: rgb(209, 212, 5);">
                                        <i class="fas fa-spin fa-sync-alt"></i>
                                    </span>
                                </span>
                            </a>
                        </td>
                        <td>
                            <a href="">
                                <span style="font-size: 1rem;">
                                    <span style="color: rgb(204, 207, 3);">
                                        <i class="fas fa-trash-alt"></i>
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
</body>

</html>
