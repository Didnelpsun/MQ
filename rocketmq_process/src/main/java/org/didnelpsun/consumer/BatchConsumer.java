// BatchConsumer.java
package org.didnelpsun.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.text.SimpleDateFormat;
import java.util.Date;

// 拉式消费者
public class BatchConsumer extends PushConsumer {

    public BatchConsumer(String topic) {
        super("batch", topic);
    }

    public BatchConsumer(String group, String topic) {
        super(group, topic);
    }

    public BatchConsumer(String group, String topic, String tag) {
        super(group, topic, tag);
    }

    public BatchConsumer(String group, ConsumeFromWhere consumeType, String topic) {
        super(group, consumeType, topic);
    }

    public BatchConsumer(String nameServer, String group, String topic, String tag) {
        super(nameServer, group, topic, tag);
    }

    public BatchConsumer(String group, ConsumeFromWhere consumeType, String topic, String tag) {
        super(group, consumeType, topic, tag);
    }

    public BatchConsumer(String group, String topic, String tag, MessageModel mode) {
        super(group, topic, tag, mode);
    }

    public BatchConsumer(String nameServer, String group, ConsumeFromWhere consumeType, String topic, String tag, MessageModel mode) {
        super(nameServer, group, consumeType, topic, tag, mode);
    }

    @Override
    public void receive() throws MQClientException {
        // 定义一个push的Consumer
        DefaultMQPushConsumer consumer = Consumer.getDefaultMQPushConsumer( this.nameServer, this.group, this.type, this.topic, this.tag, this.mode);
        // 每次可以消费的消息条数
        consumer.setConsumeMessageBatchMaxSize(10);
        // 每次可以拉取消息条数
        consumer.setPullBatchSize(40);
        // 注册监听器
        // MessageListenerConcurrently为监听订阅消息
        // 返回值为当前消费者状态
        consumer.registerMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
            for (MessageExt msg : list) {
                System.out.print(new SimpleDateFormat("mm:ss").format(new Date()));
                System.out.println("->BatchConsumer:" + msg);
            }
            // 返回状态为成功
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        // 开启消费者
        consumer.start();
        System.out.println("BatchConsumer等待消息:" + consumer);
    }

    public static void main(String[] args) throws MQClientException {
        new BatchConsumer("batchTopic").receive();
    }
}
