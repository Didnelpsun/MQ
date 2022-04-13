// OrderedSyncProducer.java
package org.didnelpsun.order;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.didnelpsun.Producer;

import java.nio.charset.StandardCharsets;

// 同步顺序消息生产者
public class OrderedSyncProducer extends Producer {
    public OrderedSyncProducer() {
        super("ordered");
    }

    public OrderedSyncProducer(String group) {
        super(group);
    }

    public OrderedSyncProducer(String nameServer, String group) {
        super(nameServer, group);
    }

    public SendResult send(String topic, String tag, String message, long orderId) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        return send(topic, tag, message, orderId, 2, 3000);
    }

    // 需要多传入一个选择id
    public SendResult send(String topic, String tag, String message, long orderId, int retryTimesWhenSendFailed, int sendMsgTimeout) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = Producer.getDefaultMQProducer(this.nameServer, this.group, retryTimesWhenSendFailed, sendMsgTimeout);
        // 开启生产者
        producer.start();
        // 生产消息
        Message msg = new Message(topic, tag, message.getBytes(StandardCharsets.UTF_8));
        // 将orderId作为消息key
        msg.setKeys(String.valueOf(orderId));
        // 发送消息，传入一个消息选择器
        // select为选择算法，有三个参数
        // list为消息队列的集合
        // message为消息
        // o为参数，即后面的orderId
        // 这里使用模运算来选择队列MessageQueue
        SendResult result = producer.send(msg, (list, message1, o) -> {
            // 获取设置的消息key
            String id = msg.getKeys();
//            Long id = (Long) o;
            int index = Integer.parseInt(id) % list.size();
            return list.get(index);
        }, orderId);
        producer.shutdown();
        System.out.println("OrderedSyncProducer发送完成");
        return result;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new OrderedSyncProducer().send("orderedTopic", "sync", "OrderedSyncProducer", 0));
    }
}
