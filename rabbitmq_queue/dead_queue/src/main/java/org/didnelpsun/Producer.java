// Producer.java
package org.didnelpsun;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import static org.didnelpsun.Property.*;
import static org.didnelpsun.RabbitUtil.*;

public class Producer {
    // 普通交换机名称
    public String normalExchange;
    // 普通发送RoutingKey
    public String normalRoute;

    public Producer() {
        this.normalExchange = NORMAL_EXCHANGE;
        this.normalRoute = NORMAL_ROUTE;
    }

    public Producer(String normalExchange, String normalRoute) {
        this.normalExchange = normalExchange;
        this.normalRoute = normalRoute;
    }

    public void send() throws IOException, TimeoutException {
        Channel channel = getChannel();
        p("输入消息：");
        // 设置消息与TTL
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            p("输入TTL：");
            int ttl = scanner.nextInt();
            // 设置过期时间参数，单位为毫秒
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration(String.valueOf(ttl)).build();
            channel.basicPublish(this.normalExchange, this.normalRoute, properties, message.getBytes(StandardCharsets.UTF_8));
            p("输入消息：");
        }
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        new Producer().send();
    }
}
