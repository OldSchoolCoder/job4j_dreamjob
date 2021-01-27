package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CandidateServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(PsqlStore.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Collection<Candidate> candidates =PsqlStore.instOf().findAllCandidates();
                req.setAttribute("candidates", PsqlStore.instOf().findAllCandidates());
        //LOGGER.log(Level.INFO,"candidates = ", PsqlStore.instOf().findAllCandidates());
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
        //getServletContext().log();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Candidate tempCandidate = new Candidate(Integer.valueOf(req.getParameter("id")), req.getParameter("name"), req.getParameter("photo"));
        PsqlStore.instOf().save(tempCandidate);
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}