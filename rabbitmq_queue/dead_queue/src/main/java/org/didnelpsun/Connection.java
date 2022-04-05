// Connection.java
package org.didnelpsun;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.didnelpsun.RabbitUtil.*;
import static org.didnelpsun.Property.*;

public class Connection {
    // 普通交换机名称
    public String normalExchange;
    // 死信交换机名称
    public String deadExchange;
    // 普通队列名称
    public String normalQueue;
    // 死信队列名称
    public String deadQueue;
    // 普通发送RoutingKey
    public String normalRoute;
    // 死信队列RoutingKey
    public String deadRoute;
    // 最大长度限制，最大值为非零值
    public int maxLen;

    public Connection() {
        this.normalExchange = NORMAL_EXCHANGE;
        this.deadExchange = DEAD_EXCHANGE;
        this.normalQueue = NORMAL_QUEUE;
        this.deadQueue = DEAD_QUEUE;
        this.normalRoute = NORMAL_ROUTE;
        this.deadRoute = DEAD_ROUTE;
        this.maxLen = 0;
    }

    public Connection(int maxLen) {
        this.normalExchange = NORMAL_EXCHANGE;
        this.deadExchange = DEAD_EXCHANGE;
        this.normalQueue = NORMAL_QUEUE;
        this.deadQueue = DEAD_QUEUE;
        this.normalRoute = NORMAL_ROUTE;
        this.deadRoute = DEAD_ROUTE;
        this.maxLen = maxLen;
    }

    public Connection(String normalExchange, String deadExchange, String normalQueue, String deadQueue, String normalRoute, String deadRoute, int maxLen) {
        this.normalExchange = normalExchange;
        this.deadExchange = deadExchange;
        this.normalQueue = normalQueue;
        this.deadQueue = deadQueue;
        this.normalRoute = normalRoute;
        this.deadRoute = deadRoute;
        this.maxLen = maxLen;
    }

    // 开始建立连接并绑定关系
    public void build() throws IOException, TimeoutException {
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
        arguments.put("x-dead-letter-routing-key", this.deadRoute);
        // 设置队列最大值
        if (maxLen > 0) {
            arguments.put("x-max-length", this.maxLen);
        }
        channel.queueDeclare(this.normalQueue, false, false, false, arguments);
        // 声明死信队列
        channel.queueDeclare(this.deadQueue, false, false, false, null);
        // 绑定交换机与队列
        channel.queueBind(normalQueue, normalExchange, normalRoute);
        channel.queueBind(deadQueue, deadExchange, deadRoute);
        p("连接建立");
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        new Connection().build();
    }
}
