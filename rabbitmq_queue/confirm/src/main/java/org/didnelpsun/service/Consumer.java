// Consumer.java
package org.didnelpsun.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static org.didnelpsun.config.PropertyConfig.CONFIRM_QUEUE;

@Service
public class Consumer {
    @RabbitListener(queues = CONFIRM_QUEUE)
    public void receive(Message message){
        System.out.println("接收消息:" + new String(message.getBody()));
    }
}
