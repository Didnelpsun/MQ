// BatchProducer.java
package org.didnelpsun.batch;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.didnelpsun.Producer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// 批量生产者
public class BatchProducer extends Producer {

    public BatchProducer() {
        super("batch");
    }

    public BatchProducer(String nameServer, String group) {
        super(nameServer, group);
    }

    public ArrayList<SendResult> send(List<Message> messages) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        return send(messages, 2, 3000);
    }

    public ArrayList<SendResult> send(String topic, List<String> tags, ArrayList<String> messages) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        return send(topic, tags, messages, 2, 3000);
    }

    public ArrayList<SendResult> send(String topic, List<String> tags, ArrayList<String> messages, int retryTimesWhenSendFailed, int sendMsgTimeout) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        if (tags.size() == 0 || messages.size() == 0) {
            System.out.println("tags和messages不应为空");
            return null;
        }
        // 封装发送消息
        List<Message> messageList = new ArrayList<>();
        // 循环，针对message大小，如果tag大小不一致则不一一对应，如果tag数量小于message数量则取模运算
        for (int i = 0; i < messages.size(); i++) {
            // 包装消息
            Message message = new Message(topic, tags.get(i % tags.size()), messages.get(i).getBytes(StandardCharsets.UTF_8));
            messageList.add(message);
        }
        return send(messageList, retryTimesWhenSendFailed, sendMsgTimeout);
    }

    public ArrayList<SendResult> send(List<Message> messages, int retryTimesWhenSendFailed, int sendMsgTimeout) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        if (messages.size() == 0) {
            System.out.println("messages不应为空");
            return null;
        }
        DefaultMQProducer producer = Producer.getDefaultMQProducer(this.nameServer, this.group, retryTimesWhenSendFailed, sendMsgTimeout);
        // 开启生产者
        producer.start();
        // 定义一个返回数组
        ArrayList<SendResult> results = new ArrayList<>();
        // 定义消息分割器
        MessageSplitter splitter = new MessageSplitter(messages);
        while (splitter.hasNext()){
            try {
                // 获取每一批切割的消息集合
                List<Message> messagesItem = splitter.next();
                // 发送消息
                SendResult result = producer.send(messagesItem);
                results.add(result);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        producer.shutdown();
        System.out.println("BatchProducer发送完成");
        return results;
    }

    public static void main(String[] args) throws Exception {
        String topic = "batchTopic";
        String tag = "batch";
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(topic, tag, "A".getBytes(StandardCharsets.UTF_8)));
        messages.add(new Message(topic, tag, "B".getBytes(StandardCharsets.UTF_8)));
        messages.add(new Message(topic, tag, "C".getBytes(StandardCharsets.UTF_8)));
        messages.add(new Message(topic, tag, "D".getBytes(StandardCharsets.UTF_8)));
        messages.add(new Message(topic, tag, "E".getBytes(StandardCharsets.UTF_8)));
        System.out.println(new BatchProducer().send(messages));
    }
}
