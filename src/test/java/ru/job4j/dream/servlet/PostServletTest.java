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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MemStore.class)
public class PostServletTest {

    @Test
    public void doGet() {
    }

    @Test
    public void doPost() throws ServletException, IOException {
        //создаем заглушку
        MemStore memStore = new MemStore();
        PowerMockito.mockStatic(MemStore.class);

        Mockito.when(MemStore.instOf()).thenReturn(memStore);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getParameter("name")).thenReturn("JavaJob");
        new PostServlet().doPost(req,resp);
        Assert.assertEquals(1,"JavaJob");
        //хоть и зачеркнуто, но компилируется
        //assertThat(1,is(1));
    }
}