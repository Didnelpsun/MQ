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
        channel.exchangeDeclare(exchangeName, "fanout");
        // 循环输入
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            // fanout模式不需要routingKey来指定路由，所以传入任意值
            channel.basicPublish(exchangeName, "", null, scanner.next().getBytes(StandardCharsets.UTF_8));
            System.out.println("发送成功：" + scanner.next());
        }
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        new Publisher().send(EXCHANGE);
    }
}
