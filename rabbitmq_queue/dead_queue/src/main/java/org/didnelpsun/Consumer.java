// Consumer.java
package org.didnelpsun;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.didnelpsun.RabbitUtil.*;


public class Consumer {
    // 普通交换机名称
    public String normalExchange;
    // 死信交换机名称
    public String deadExchange;
    // 普通队列名称
    public String normalQueue;
    // 死信队列名称
    public String deadQueue;

    public Consumer() {
        this.normalExchange = "normal_exchange";
        this.deadExchange = "dead_exchange";
        this.normalQueue = "normal_queue";
        this.deadQueue = "dead_queue";
    }

    public Consumer(String normalExchange, String deadExchange, String normalQueue, String deadQueue) {
        this.normalExchange = normalExchange;
        this.deadExchange = deadExchange;
        this.normalQueue = normalQueue;
        this.deadQueue = deadQueue;
    }

    // 正常接收消息
    public void receive() throws IOException, TimeoutException {
        // 获取信道
        Channel channel = getChannel();
        // 声明交换机
        channel.exchangeDeclare(this.normalExchange, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(this.deadExchange, BuiltinExchangeType.DIRECT);
        // 声明普通队列
        // 但是普通队列需要在死信条件满足时将消息发送给死信交换机，所以就需要设置最后的Map参数
        Map<String, Object> arguments = new HashMap<>();
        // 设置消费异常时转发死信交换机的名称，key为固定值
        arguments.put("x-dead-letter-exchange", this.deadExchange);
        // 设置死信交换机发送死信给队列的路由RoutingKey
        arguments.put("x-dead-letter-routing_key", this.deadExchange);
        channel.queueDeclare(this.normalQueue, false, true, false, arguments);
        // 声明死信队列
        channel.queueDeclare(this.deadQueue, false, true, false, null);
        // 消费消息
        channel.basicConsume(this.normalQueue, true, (consumerTag, message) -> {
            p("消息" + consumerTag + "接收成功：" + new String(message.getBody(), StandardCharsets.UTF_8));
        }, consumerTag -> {
            p("消息" + consumerTag + "接收失败");
        });
    }
}
