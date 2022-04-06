// Consumer.java
package org.didnelpsun.service;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class Consumer {
    // 使用监听器接收消息
    @RabbitListener(queues = "queue")
    public void receive(Message message, Channel channel) {
        System.out.println(new Date() + ":" + new String(message.getBody(), StandardCharsets.UTF_8));
    }
}
