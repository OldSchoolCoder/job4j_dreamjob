package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class PostServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        try {
            req.setAttribute("posts", PsqlStore.instOf().findAllPosts());
        } catch (SQLException e) {
            throw new ServletException("Error! SQLException!", e);
        }
        req.setAttribute("user", session.getAttribute("user"));
        req.getRequestDispatcher("posts.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            PsqlStore.instOf().save(
                    new Post(
                            Integer.valueOf(req.getParameter("id")),
                            req.getParameter("name")
                    )
            );
        } catch (SQLException e) {
            throw new ServletException("Error! SQLException!", e);
        }
        resp.sendRedirect(req.getContextPath() + "/posts.do");
    }
}
