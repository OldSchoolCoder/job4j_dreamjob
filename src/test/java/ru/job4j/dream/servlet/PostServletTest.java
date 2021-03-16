package ru.job4j.dream.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.PowerMockUtils;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
//
//import org.powermock.*;
import ru.job4j.dream.store.MemStore;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class PostServletTest {

    @Test
    public void doGet() {
    }

    @Test
    public void doPost() throws ServletException, IOException, SQLException {
        //создаем заглушку
        Store storeStub = new PsqlStoreStub();
        PowerMockito.mockStatic(PsqlStore.class);
        //когда инициализируется хранилище - возвращаем заглушку
        Mockito.when(PsqlStore.instOf()).thenReturn(storeStub);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        //создаем правило для заглушки
        when(req.getParameter("id")).thenReturn("0");
        when(req.getParameter("name")).thenReturn("JavaJob");
        //вызываем сервлет для теста
        new PostServlet().doPost(req,resp);
        Assert.assertEquals(storeStub.findById(1).getName(),"JavaJob");
        //хоть и зачеркнуто, но компилируется
        //assertThat(1,is(1));
    }
}