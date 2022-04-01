// Producer.java
package org.didnelpsun.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.didnelpsun.util.Property;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.didnelpsun.util.Property.QUEUE_NAME;

public class Producer {

    public void send() throws IOException, TimeoutException {
        send(QUEUE_NAME);
    }

    // 发送消息
    public void send(String message) throws IOException, TimeoutException {
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
        // 生成队列，有五个参数
        // 第一个队列名字
        // 第二个是否持久化，true保存消息到磁盘，false只在内存中保存消息
        // 第三个是否需要排他，该队列是否只供一个消费者消费，true为排他，false共享
        // 第四个表示是否自动删除，最后一个消费者断开连接后是否自动删除该队列
        // 第五个是队列参数，如延迟等
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 发布消息
        // 第一个参数为交换机名
        // 第二个参数为路由Key，可以直接写队列名
        // 第三个参数为附加参数
        // 第四个参数为消息体
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送成功");
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        new Producer().send();
    }
}
