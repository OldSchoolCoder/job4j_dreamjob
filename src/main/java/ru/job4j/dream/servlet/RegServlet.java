package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        int id = 0;
        User user = new User();
        user.setPassword(password);
        user.setEmail(email);
        user.setName(username);
        user.setId(id);
        Store store = PsqlStore.instOf();
        try {
            if (store.findByEmail(email) != null) {
                throw new ServletException(" Registration Error! User already exists! ");
            }
        } catch (SQLException throwables) {
            //если бросет SQLException - бездействие, потому что SQLException возникает, когда он ненаходит нового пользователя
            //throw new ServletException(" Error in user=store.findByEmail(email)! ");
        } catch (ServletException e){
            throw new ServletException(" Registration Error! User already exists! ", e);
        }
        try {
            //if (store.findByEmail(email) == null) {
                store.save(user);
           //} else {
                //cheked
                //throw new ServletException("Registration Error! User already exists!");
            //}
        } catch (SQLException e) {
            throw new ServletException("Registration Error! Can't save user!", e);
        }
        //resp.sendRedirect(req.getContextPath() + "/auth.do");
        req.getRequestDispatcher("login.jsp").forward(req,resp);
    }
}
