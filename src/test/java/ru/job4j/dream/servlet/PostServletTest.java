package ru.job4j.dream.servlet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.powermock.*;
import ru.job4j.dream.store.MemStore;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MemStore.class)
public class PostServletTest {

    @Test
    public void doGet() {
    }

    @Test
    public void doPost() {
        Assert.assertEquals(1,1);
        //хоть и зачеркнуто, но компилируется
        //assertThat(1,is(1));
    }
}