// Consumer.java
package org.didnelpsun;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static org.didnelpsun.RabbitUtil.*;

public class Consumer {
    public String queueName;

    public Consumer(String normalQueue) {
        this.queueName = normalQueue;
    }

    // 正常接收消息
    // 传输一个拒绝条件函数参数用来判断什么时候拒绝这个消息
    public void receive(ICondition iCondition) throws IOException, TimeoutException {
        // 获取信道
        Channel channel = getChannel();
        p("等待接收消息...");
        // 消费消息
        // 这里由于需要拒绝消息，所以必须改为手动应答，否则都默认自动应答全部不会被拒绝
        channel.basicConsume(this.queueName, false, (consumerTag, message) -> {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            if(iCondition.condition(consumerTag, message)){
                p("消息" + consumerTag + "被拒绝：" + msg);
                // 有两个参数，一个是消息标志，一个是是否放回队列
                // 不放回消息就加入死信队列
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            }else{
                // 不进行批量应答
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                p("消息" + consumerTag + "接收成功：" + msg);
            }
        }, consumerTag -> p("消息" + consumerTag + "接收失败"));
    }
}
