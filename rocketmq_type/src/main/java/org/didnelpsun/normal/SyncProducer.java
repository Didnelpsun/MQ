// SyncProducer.java
package org.didnelpsun.normal;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.didnelpsun.Producer;

import java.nio.charset.StandardCharsets;

// 同步生产者
public class SyncProducer extends Producer {

    public SyncProducer() {
        super("normal");
    }

    public SyncProducer(String nameServer, String producerGroup) {
        super(nameServer, producerGroup);
    }

    public SendResult send(String topic, String message) throws Exception {
        return send(topic, "", message);
    }

    public SendResult send(String topic, String tag, String message) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        return send(topic, tag, message, 2, 3000);
    }

    public SendResult send(String topic, String tag, String message, int retryTimesWhenSendFailed, int sendMsgTimeout) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        // 创建一个Producer，参数为Producer Group名称，我们是普通消息所以使用normal
        DefaultMQProducer producer = new DefaultMQProducer(this.producerGroup);
        // 设置NameServer地址
        producer.setNamesrvAddr(this.nameServer);
        // 设置当同步发送失败是重试发送的次数，默认为为2次
        producer.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);
        // 设置发送超时时限，默认为3s
        producer.setSendMsgTimeout(sendMsgTimeout);
        // 开启生产者
        producer.start();
        // 生产消息
        Message msg = new Message(topic, tag, message.getBytes(StandardCharsets.UTF_8));
        // 发送消息
        SendResult result = producer.send(msg);
        producer.shutdown();
        return result;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new SyncProducer().send("normalTopic", "sync", "SyncProducer"));
    }
}
