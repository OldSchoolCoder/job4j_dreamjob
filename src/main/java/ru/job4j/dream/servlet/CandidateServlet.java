package ru.job4j.dream.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Photo;
import ru.job4j.dream.store.PsqlStore;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CandidateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("candidates", PsqlStore.instOf().findAllCandidates());
        } catch (SQLException e) {
            throw new ServletException("Error! SQLException!", e);
        }
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = null;
        String photo = null;
        int id = 0;
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> items = upload.parseRequest(req);
            File folder = new File("images//photo");
            if (!folder.exists()) {
                folder.mkdir();
            }
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    File file = new File(folder + File.separator + item.getName());
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        out.write(item.getInputStream().readAllBytes());
                    }
                    photo = item.getName();
                    PsqlStore.instOf().save(new Photo(id, photo));
                } else {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    outputStream.write(item.get());
                    String itemValue = outputStream.toString();
                    if (item.getFieldName().equals("name")) {
                        name = itemValue;
                    }
                    if (item.getFieldName().equals("id")) {
                        id = Integer.valueOf(itemValue);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Candidate tempCandidate = null;
        if (photo != null && name != null) {
            tempCandidate = new Candidate(id, name, photo);
            try {
                PsqlStore.instOf().save(tempCandidate);
            } catch (SQLException e) {
                throw new ServletException("Error! SQLException!", e);
            }
        } else {
            throw new ServletException("Error! Photo or Name is NULL");
        }
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
