// QueueConsumer.java
package org.didnelpsun.org.didnelpsun.service;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import javax.jms.JMSException;
import javax.jms.TextMessage;

@Service
public class QueueConsumer {
    // 添加对目的地的监听
    @JmsListener(destination = "${queueName}")
    public void receive(TextMessage textMessage) throws JMSException {
        System.out.println("receive:" + textMessage.getText());
    }
}
