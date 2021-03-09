package ru.job4j.servlets;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeleteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Candidate candidate = new Candidate(0, "", "TestPhoto");
        try {
            if (id != null) {
                candidate = PsqlStore.instOf().findCandidateById(Integer.valueOf(id));
            }
            if (candidate.getName().equals("")) {
                throw new ServletException("Error! Can't find candidate in storage");
            } else {
                PsqlStore.instOf().delete(candidate);
                Files.delete(Path.of("images//photo" + File.separator + candidate.getPhoto()));
            }
            req.setAttribute("candidates", PsqlStore.instOf().findAllCandidates());
        } catch (SQLException e) {
            throw new ServletException("Error! SQLException!", e);
        }
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }
}
