// OnewayProducer.java
package org.didnelpsun.normal;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.didnelpsun.Producer;

import java.nio.charset.StandardCharsets;

// 单向生产者
public class OnewayProducer extends Producer {

    public OnewayProducer() {
        super("normal");
    }

    public OnewayProducer(String nameServer, String group) {
        super(nameServer, group);
    }

    public SendResult send(String topic, String message) throws Exception {
        return send(topic, "", message);
    }

    // 由于不会收到回复和确认，所以只需要发消息而不用重发
    public SendResult send(String topic, String tag, String message) throws MQClientException, RemotingException, InterruptedException {
        DefaultMQProducer producer = Producer.getDefaultMQProducer(this.nameServer, this.group);
        // 开启生产者
        producer.start();
        // 生产消息
        Message msg = new Message(topic, tag, message.getBytes(StandardCharsets.UTF_8));
        // 发送消息
        producer.sendOneway(msg);
        producer.shutdown();
        System.out.println("OnewayProducer发送完成");
        return null;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new OnewayProducer().send("normalTopic", "oneway", "OnewayProducer"));
    }
}
