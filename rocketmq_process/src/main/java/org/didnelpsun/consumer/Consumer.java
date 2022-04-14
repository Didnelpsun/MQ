// Consumer.java
package org.didnelpsun.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

// 消费者父类
public class Consumer {
    // NameServer地址，并指定默认值
    protected String nameServer = "127.0.0.1:9876";
    // 组名
    protected String group;
    // 消费方式，默认为从第一个开始消费
    protected ConsumeFromWhere type = ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET;
    // 主题
    protected String topic;
    // 标签，默认为*表示全部
    protected String tag = "*";
    // 消费模式，默认为集群模式
    protected MessageModel mode = MessageModel.CLUSTERING;

    public Consumer(String group, String topic) {
        this.group = group;
        this.topic = topic;
    }

    public Consumer(String group, String topic, String tag) {
        this.group = group;
        this.topic = topic;
        this.tag = tag;
    }

    public Consumer(String group, ConsumeFromWhere type, String topic) {
        this.group = group;
        this.type = type;
        this.topic = topic;
    }

    public Consumer(String nameServer, String group, String topic, String tag) {
        this.nameServer = nameServer;
        this.group = group;
        this.topic = topic;
        this.tag = tag;
    }

    public Consumer(String group, ConsumeFromWhere type, String topic, String tag) {
        this.group = group;
        this.type = type;
        this.topic = topic;
        this.tag = tag;
    }

    public Consumer(String group, String topic, String tag, MessageModel mode) {
        this.group = group;
        this.topic = topic;
        this.tag = tag;
        this.mode = mode;
    }

    public Consumer(String nameServer, String group, ConsumeFromWhere type, String topic, MessageModel mode) {
        this.nameServer = nameServer;
        this.group = group;
        this.type = type;
        this.topic = topic;
        this.mode = mode;
    }

    public Consumer(String nameServer, String group, ConsumeFromWhere type, String topic, String tag, MessageModel mode) {
        this.nameServer = nameServer;
        this.group = group;
        this.type = type;
        this.topic = topic;
        this.tag = tag;
        this.mode = mode;
    }

    // 获取推式消费者实例
    public static DefaultMQPushConsumer getDefaultMQPushConsumer(String nameServer, String group, ConsumeFromWhere type, String topic, String tag, MessageModel mode) throws MQClientException {
        // 定义一个push的Consumer
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group);
        consumer.setNamesrvAddr(nameServer);
        consumer.setConsumeFromWhere(type);
        consumer.subscribe(topic, tag);
        consumer.setMessageModel(mode);
        return consumer;
    }

    public void receive() throws Exception {
    }

    @Override
    public String toString() {
        return "Consumer{" + "nameServer='" + nameServer + '\'' + ", group='" + group + '\'' + ", type=" + type + ", topic='" + topic + '\'' + ", tag='" + tag + '\'' + ", mode=" + mode + '}';
    }
}
