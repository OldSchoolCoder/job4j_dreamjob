package ru.job4j.servlets;

import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CityDownload extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter writer = new PrintWriter(resp.getOutputStream());
        writer.print("<option selected>Choose...</option>");
        Store store = PsqlStore.instOf();
        Collection<String> cityList = new ArrayList<>();
        try {
            cityList = store.getCityList();
        } catch (SQLException e) {
            throw new ServletException("Error! SQLException!", e);
        }
        for (int i = 0; i < cityList.size(); i++) {
            writer.print("<option value=\"" + (i + 1) + "\">" + cityList.toArray()[i] + "</option>");
        }
        writer.flush();
    }
}
