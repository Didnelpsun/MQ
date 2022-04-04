// Consumer.java
package org.didnelpsun;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Consumer {
    // 交换机名
    public String exchangeName;
    // RoutingKey，为一个字符串集合
    public ArrayList<String> routingKeys;

    public Consumer(String exchangeName, ArrayList<String> routingKeys) {
        this.exchangeName = exchangeName;
        this.routingKeys = routingKeys;
    }

    public Consumer(String exchangeName, String routingKey){
        this.exchangeName = exchangeName;
        this.routingKeys = new ArrayList<>();
        this.routingKeys.add(routingKey);
    }

    // 添加RoutingKey
    public boolean addKey(String routingKey){
        return this.routingKeys.add(routingKey);
    }

    // 移除RoutingKey
    public boolean removeKey(String routingKey){
        return this.routingKeys.remove(routingKey);
    }

    public void receive() throws IOException, TimeoutException {
        Channel channel = RabbitUtil.getChannel();
        // 声明一个路由类型交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
        // 声明一个临时队列
        String queue = channel.queueDeclare().getQueue();
        // 绑定信道、队列、RoutingKey
        // 参数为队列名，交换机名，RoutingKey
        for(String routingKey : routingKeys){
            channel.queueBind(queue, exchangeName, routingKey);
        }
        System.out.println("等待接受消息...");
        // 消费消息
        channel.basicConsume(queue, true, (consumerTag, message) -> System.out.println("消息" + consumerTag + "接受成功：" + new String(message.getBody(), StandardCharsets.UTF_8)), (consumerTag) -> System.out.println("消息" + consumerTag + "接受失败"));
    }
}
