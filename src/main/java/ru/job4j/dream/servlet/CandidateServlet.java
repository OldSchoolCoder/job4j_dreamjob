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
        String name=null;
        String photo=null;
        int id = 0;
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            //Получаем список всех данных в запросе
            List<FileItem> items = upload.parseRequest(req);
            //File folder = new File("images");
            //FileNotFoundException: images/photo (Is a directory) - если не нажать кнопку загрузки, то в любом случае так будет
            File folder = new File("images//photo");
            //работает.
            //File folder = new File("images//test");
            if (!folder.exists()) {
                folder.mkdir();
            }
            for (FileItem item : items) {
                //Если элемент не поле, то это файл и из него можно прочитать весь входной поток и записать его в файл или напрямую в базу данных.
                if (!item.isFormField()) {
                    File file = new File(folder + File.separator + item.getName());
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        out.write(item.getInputStream().readAllBytes());
                    }
                    //после создания файла - записываем имя в базу
                    //PsqlStore.instOf().save(photo);
                    //PsqlStore.instOf().save(new Photo(0,"FirstPhoto"));
                    photo=item.getName();
                    //id будет актуальным (!=0) потому что оно читается первым, а суда зайдет после чтения из параметра
                    PsqlStore.instOf().save(new Photo(id,photo));
                } else {
                    //Если элемент не файл, то это параметр = id или name
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    outputStream.write(item.get());
                    String itemValue = outputStream.toString();
                    if (item.getFieldName().equals("name")) {
                        name=itemValue;
                    }
                    if (item.getFieldName().equals("id")) {
                        id=Integer.valueOf(itemValue);
                    }
//                    Candidate tempCandidate = new Candidate(Integer.valueOf(req.getParameter("id")), req.getParameter("name"), req.getParameter("photo"));
//                    PsqlStore.instOf().save(tempCandidate);

                }
            }
        //} catch (FileUploadException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }

        //req.setCharacterEncoding("UTF-8");
        //Candidate tempCandidate = new Candidate(Integer.valueOf(req.getParameter("id")), req.getParameter("name"), req.getParameter("photo"));
        Candidate tempCandidate = null;
        if (photo!=null && name!=null) {
            tempCandidate = new Candidate(id, name, photo);
            PsqlStore.instOf().save(tempCandidate);
        } else {
            LOGGER.warning("Error! Photo or Name is NULL");
        }
        //перенаправляем на candidates.jsp
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
