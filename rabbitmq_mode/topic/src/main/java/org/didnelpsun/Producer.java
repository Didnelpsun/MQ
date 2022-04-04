// Producer.java
package org.didnelpsun;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import static org.didnelpsun.Property.EXCHANGE;

public class Producer {
    // 循环输入
    // 先输入主题，后输入消息
    public void send(String exchangeName) throws IOException, TimeoutException {
        Channel channel = RabbitUtil.getChannel();
        // 循环输入
        Scanner scanner = new Scanner(System.in);
        System.out.println("主题：");
        while (scanner.hasNext()) {
            String topic = scanner.next();
            System.out.println("消息：");
            String message = scanner.next();
            channel.basicPublish(exchangeName, topic, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("发送给：" + topic  + "成功：" + message);
            System.out.println("主题：");
        }
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        new Producer().send(EXCHANGE);
    }
}
