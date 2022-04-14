// SyncFilterProducer.java
package org.didnelpsun.filter;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.didnelpsun.Producer;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

// 参数同步生产者
public class SyncFilterProducer extends FilterProducer {

    public SyncFilterProducer() {
        super();
    }

    public SyncFilterProducer(Map<String, Object> properties) {
        super();
        this.properties = properties;
    }

    public SyncFilterProducer(String group) {
        super(group);
    }

    public SyncFilterProducer(String nameServer, String group) {
        super(nameServer, group);
    }

    public SendResult send(String topic, String message) throws Exception {
        return send(topic, "", message);
    }

    public SendResult send(String topic, String tag, String message) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        return send(topic, tag, message, 2, 3000);
    }

    public SendResult send(String topic, String tag, String message, int retryTimesWhenSendFailed, int sendMsgTimeout) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = Producer.getDefaultMQProducer(this.nameServer, this.group, retryTimesWhenSendFailed, sendMsgTimeout);
        // 开启生产者
        producer.start();
        // 生产消息
        Message msg = new Message(topic, tag, message.getBytes(StandardCharsets.UTF_8));
        // 发送消息并赋值
        SendResult result = producer.send(this.putUserProperty(msg));
        producer.shutdown();
        System.out.println("SyncFilterProducer发送完成");
        return result;
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> prop = new HashMap<>();
        prop.put("age", 10);
        System.out.println(new SyncFilterProducer(prop).send("filterTopic", "sync", "SyncFilterProducer"));
    }
}
