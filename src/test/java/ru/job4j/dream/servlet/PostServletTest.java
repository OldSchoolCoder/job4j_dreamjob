package ru.job4j.dream.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class PostServletTest {

    Store storeStub = new PsqlStoreStub();
    HttpServletRequest req = mock(HttpServletRequest.class);
    HttpServletResponse resp = mock(HttpServletResponse.class);

    @Test
    public void doGet() throws ServletException, IOException, SQLException {
        List<Post> testPosts = new ArrayList<>();
        User user = new User();
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher reqDispatcher = mock(RequestDispatcher.class);
        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.instOf()).thenReturn(storeStub);
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(req.getRequestDispatcher("posts.jsp")).thenReturn(reqDispatcher);
        new PostServlet().doGet(req, resp);
        Assert.assertEquals(storeStub.findAllPosts(), testPosts);
    }

    @Test
    public void doPost() throws ServletException, IOException, SQLException {
        PowerMockito.mockStatic(PsqlStore.class);
        Mockito.when(PsqlStore.instOf()).thenReturn(storeStub);
        when(req.getParameter("id")).thenReturn("0");
        when(req.getParameter("name")).thenReturn("JavaJob");
        new PostServlet().doPost(req, resp);
        Assert.assertEquals(storeStub.findById(1).getName(), "JavaJob");
    }
}