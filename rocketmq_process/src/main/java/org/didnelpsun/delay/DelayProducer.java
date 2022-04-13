// DelayProducer.java
package org.didnelpsun.delay;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.didnelpsun.Producer;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

// 延迟生产者
public class DelayProducer extends Producer {

    // 延迟等级，默认为5s
    private int level = 2;

    public DelayProducer() {
        super("delay");
    }

    public DelayProducer(int level) {
        super("delay");
        this.level = level;
    }

    public DelayProducer(String nameServer, String group) {
        super(nameServer, group);
    }

    public DelayProducer(String nameServer, String group, int level) {
        super(nameServer, group);
        this.level = level;
    }

    public SendResult send(String topic, String message) throws Exception {
        return send(topic, "", message);
    }

    // 延迟发送，所以不会是同步
    public SendResult send(String topic, String tag, String message) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = Producer.getDefaultMQProducer(this.nameServer, this.group);
        // 开启生产者
        producer.start();
        // 生产消息
        Message msg = new Message(topic, tag, message.getBytes(StandardCharsets.UTF_8));
        // 设置延迟等级
        msg.setDelayTimeLevel(this.level);
        // 发送消息
        SendResult result = producer.send(msg);
        producer.shutdown();
        System.out.println("DelayProducer定时发送等级" + this.level + "->" + new SimpleDateFormat("mm:ss").format(new Date()));
        return result;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new DelayProducer().send("delayTopic", "delay", "DelayProducer"));
    }
}
