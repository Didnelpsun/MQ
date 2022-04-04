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
    public void send(String exchangeName, ArrayList<String> routingKeys) throws IOException, TimeoutException {
        Channel channel = RabbitUtil.getChannel();
        // 循环输入
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            for(String routingKey : routingKeys){
                channel.basicPublish(exchangeName, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("发送给：" + routingKey  + "成功：" + message);
            }
        }
    }

    public void send(String exchangeName, String routingKey) throws IOException, TimeoutException {
        ArrayList<String> routingKeys = new ArrayList<>();
        routingKeys.add(routingKey);
        send(exchangeName, routingKeys);
    }

    public static void main(String[] args) throws IOException, TimeoutException {
//        new Producer().send(EXCHANGE, "1");
        ArrayList<String> keys = new ArrayList<>();
        keys.add("1");
        keys.add("2");
        new Producer().send(EXCHANGE, keys);
    }
}
