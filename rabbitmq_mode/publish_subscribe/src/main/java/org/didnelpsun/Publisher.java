// Publisher.java
package org.didnelpsun;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import static org.didnelpsun.Property.EXCHANGE;

public class Publisher {
    public void send(String exchangeName) throws IOException, TimeoutException {
        Channel channel = RabbitUtil.getChannel();
//        channel.exchangeDeclare(exchangeName, "fanout");
        // 循环输入
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            // fanout模式不需要指定队列名，只需要指定交换机名
            channel.basicPublish(exchangeName, "", null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("发送成功：" + message);
        }
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        new Publisher().send(EXCHANGE);
    }
}
