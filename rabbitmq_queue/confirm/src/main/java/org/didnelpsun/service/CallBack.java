// CallBack.java
package org.didnelpsun.service;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Service
public class CallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    // 注入RabbitTemplate实例
    @Resource
    private RabbitTemplate rabbitTemplate;

    // 实例化后就执行将这个实例注入到rabbitTemplate中
    @PostConstruct
    public void init() {
        // 消息回调
        rabbitTemplate.setConfirmCallback(this);
        // 消息回退
        rabbitTemplate.setReturnsCallback(this);
    }

    // 确认方法
    // correlationData保存回调消息的ID与相关信息
    // ack交换机是否接收到消息，如果接收到了为true，如果没有为false
    // cause接收失败的原因，如果ack为true则cause为null，如果ack为false，cause为字符串类型
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (correlationData == null) {
            System.out.println("消息回调失败");
            return;
        }
        String id = correlationData.getId();
        if (ack)
            System.out.println("接收到ID为" + id + "的消息:" + (correlationData.getReturned() != null ? new String(correlationData.getReturned().getMessage().getBody(), StandardCharsets.UTF_8) : ""));
        else System.out.println("未接收到ID为" + id + "的消息:" + cause);
    }

    // 消息回退，只有消息不可达才回退，所以只处理失败没有处理成功
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        System.out.printf("消息被回退:[内容:%s,原因:%s,响应码:%s,交换机:%s,路由:$%s]\n", new String(returnedMessage.getMessage().getBody(), StandardCharsets.UTF_8),returnedMessage.getReplyText(),returnedMessage.getReplyCode(),returnedMessage.getExchange(),returnedMessage.getRoutingKey());
    }
}
