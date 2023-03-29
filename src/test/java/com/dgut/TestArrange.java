package com.dgut;

import com.dgut.mapper.OrderMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestArrange {

    @Autowired
    private OrderMapper orderMapper;
    @Test
    public void contextLoads() {
        String date = "2021-04-01";
        String substring = date.substring(0, 4);
        int i = Integer.parseInt(substring);
        System.out.println(i);
    }

    public synchronized void aaa() throws InterruptedException {
     System.out.println("资源正在被占用");
     Thread.sleep(3000);
    }
    @Test
    public void testAAA() throws InterruptedException {
        for (int i = 0;i<10;i++){
            aaa();
        }
    }




}