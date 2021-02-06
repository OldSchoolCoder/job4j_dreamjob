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
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeleteServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(PsqlStore.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //int id = Integer.parseInt(req.getParameter("id"));
        //int id = Integer.valueOf(req.getParameter("id"));
        String id = req.getParameter("id");
        Candidate candidate = new Candidate(0, "", "TestPhoto");
        if (id != null) {
            candidate = PsqlStore.instOf().findCandidateById(Integer.valueOf(id));
        }
        LOGGER.log(Level.WARNING, "Parameter id = {0}", id);
        //Files.delete(Path.of("images//photo"+ File.separator + name));
        LOGGER.log(Level.WARNING,"Hello from DeleteServlet! Candidate name = {0}", candidate.getName());
        LOGGER.log(Level.WARNING,"Candidate photo = {0}", candidate.getPhoto());
        //Candidate candidate = PsqlStore.instOf().findCandidateById(999);
        //Candidate candidate = PsqlStore.instOf().findCandidateById(999);
        if (candidate.getName().equals("")) {
            LOGGER.warning("Error! Can't find candidate in storage");
        } else {
            PsqlStore.instOf().delete(candidate);
            Files.delete(Path.of("images//photo"+ File.separator + candidate.getPhoto()));
        }

    }
}
