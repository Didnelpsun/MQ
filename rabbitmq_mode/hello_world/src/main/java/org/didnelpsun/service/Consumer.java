// Consumer.java
package org.didnelpsun.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.didnelpsun.util.Property;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static org.didnelpsun.util.Property.QUEUE_NAME;

public class Consumer {

    public void receive() throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置参数
        connectionFactory.setHost(Property.host);
        connectionFactory.setUsername(Property.username);
        connectionFactory.setPassword(Property.password);
        // 建立连接
        Connection connection = connectionFactory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        // 消费消息，有四个参数
        // 第一个参数为队列名
        // 第二个参数为消费成功后是否自动确认
        // 第三个参数为成功消费的回调函数实现
        // 第四个参数为失败消费的回调函数实现
        channel.basicConsume(QUEUE_NAME, true, (consumerTag, message) -> {
            System.out.println("成功接受：" + new String(message.getBody(), StandardCharsets.UTF_8));
        }, (consumerTag) -> {
            System.out.println("消费中断：" + new String(consumerTag.getBytes(StandardCharsets.UTF_8)));
        });
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        new Consumer().receive();
    }
}
