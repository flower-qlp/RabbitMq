package com.happy.rabbit.temolateannotation.producer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest()
@RunWith(SpringJUnit4ClassRunner.class)
public class sendTest {

    @Autowired
    private DirectSender directSender;

    @Autowired
    private TopicSender topicSender;

    @Autowired
    private FanoutSender fanoutSender;

    @Test
    public void send() {
        while (true) {
            Integer rand = (int) (Math.random() * 1000);
            directSender.send("这是第" + rand + "个苹果！！！");
            try {
                Thread.sleep(Long.valueOf(rand));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void sendTopic() {
        while (true) {
            Integer rand = (int) (Math.random() * 10000);
            topicSender.send("这是第" + rand + "个苹果！！！");
            try {
                Thread.sleep(Long.valueOf(rand));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void sendFanout() {
        while (true) {
            Integer rand = (int) (Math.random() * 10000);
            fanoutSender.send("这是第" + rand + "个苹果！！！");
            try {
                Thread.sleep(Long.valueOf(rand));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
