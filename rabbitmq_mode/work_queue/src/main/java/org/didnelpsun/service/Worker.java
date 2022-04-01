// Worker.java
package org.didnelpsun.service;

import com.rabbitmq.client.Channel;
import org.didnelpsun.util.RabbitUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static org.didnelpsun.util.Property.QUEUE_NAME;

public class Worker {
    public int id;

    public Worker() {
        this.id = 0;
    }

    public Worker(int id) {
        this.id = id;
    }

    public void receive() throws IOException, TimeoutException {
        // 创建信道
        Channel channel = RabbitUtil.getChannel();
        System.out.println("工作队列" + this.id + "等待消息...");
        // 消费消息，有四个参数
        // 第一个参数为队列名
        // 第二个参数为消费成功后是否自动确认
        // 第三个参数为成功消费的回调函数实现
        // 第四个参数为失败消费的回调函数实现
        channel.basicConsume(QUEUE_NAME, true, (consumerTag, message) -> {
            System.out.println("工作队列" + this.id + "成功接受：" + new String(message.getBody(), StandardCharsets.UTF_8));
        }, (consumerTag) -> {
            System.out.println("工作队列" + this.id + "消费中断：" + new String(consumerTag.getBytes(StandardCharsets.UTF_8)));
        });
    }

    // 传输一个参数表示确认消息的发送时间，确认不会是立刻发送
    public void receive(int second) throws IOException, TimeoutException {
        // 创建信道
        Channel channel = RabbitUtil.getChannel();
        System.out.println("工作队列" + this.id + "等待消息...");
        // 消费消息，有四个参数
        // 第一个参数为队列名
        // 第二个参数为消费成功后是否自动确认
        // 第三个参数为成功消费的回调函数实现
        // 第四个参数为失败消费的回调函数实现
        channel.basicConsume(QUEUE_NAME, false, (consumerTag, message) -> {
            // 沉睡
            RabbitUtil.sleep(second);
            System.out.println("工作队列" + this.id + "成功接受：" + new String(message.getBody(), StandardCharsets.UTF_8));
            // 使用消息的传输tag进行应答，并不进行批量应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        }, (consumerTag) -> {
            System.out.println("工作队列" + this.id + "消费中断：" + new String(consumerTag.getBytes(StandardCharsets.UTF_8)));
        });
    }
}
