// Subscriber.java
package org.didnelpsun;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Subscriber {
    public String exchangeName;

    public Subscriber(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public void subscribe() throws IOException, TimeoutException {
        Channel channel = RabbitUtil.getChannel();
        // 声明一个扇出类型交换机
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);
        // 声明一个临时队列
        String queue = channel.queueDeclare().getQueue();
        // 绑定信道与队列
        // 参数为队列名，交换机名，routingKey
        // routingKey是为了找到对应关系的队列，但是此时是广播交换机所以不需要专门找特定的队列，所以可以乱写，路由模式则需要
        channel.queueBind(queue, exchangeName, "");
        System.out.println("等待接受消息...");
        // 消费消息
        channel.basicConsume(queue, true, (consumerTag, message) -> System.out.println("消息" + consumerTag + "接受成功：" + new String(message.getBody(), StandardCharsets.UTF_8)), (consumerTag) -> System.out.println("消息" + consumerTag + "接受失败"));
    }
}
