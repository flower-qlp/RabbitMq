package com.happy.rabbit.templateMq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author happy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RabbitTest {

    @Autowired
    private TemplateSendTest templateSendTest;

    @Test
    public void sendMessage() {
        templateSendTest.sendMessage();
    }


}
