// Controller.java
package org.didnelpsun.controller;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.didnelpsun.config.PropertyConfig.*;

@RestController
public class Controller {
    @Resource
    private RabbitTemplate rabbitTemplate;

    // 发送消息
    @GetMapping("/send/{message}")
    public String send(@PathVariable String message) {
        // 设置回退消息
        CorrelationData data = new CorrelationData(UUID.randomUUID().toString());
        // ReturnedMessage参数：
        // Message message
        // int replyCode;
        // String replyText
        // String exchange
        // String routingKey
        data.setReturned(new ReturnedMessage(new Message(message.getBytes(StandardCharsets.UTF_8)), 200, "成功", CONFIRM_EXCHANGE, CONFIRM_ROUTING_KEY));
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE, "d", message, data);
        System.out.println("发送消息成功:" + message);
        return message;
    }
}
