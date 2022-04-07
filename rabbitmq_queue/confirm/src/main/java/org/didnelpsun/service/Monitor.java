// Monitor.java
package org.didnelpsun.service;

import org.didnelpsun.config.PropertyConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class Monitor {
    // 监控报警队列
    @RabbitListener(queues = PropertyConfig.WARN_QUEUE)
    public void receiveWarning(Message message) {
        System.out.println("消息路由失败:" + new String(message.getBody(), StandardCharsets.UTF_8));
    }
}
