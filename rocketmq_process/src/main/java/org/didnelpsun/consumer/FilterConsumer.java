// FilterConsumer.java
package org.didnelpsun.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FilterConsumer extends Consumer {
    // SQL语句，默认为空
    // 如果sql为空，则默认tag为*进行查询，而父类默认tag就是*，所以此时不用再重新设置
    protected String sql = "";

    public FilterConsumer(String topic) {
        super("filter", topic);
    }

    public FilterConsumer(String topic, String sql) {
        super("filter", topic);
        this.sql = sql;
    }

    public FilterConsumer(String group, String topic, String sql) {
        super(group, topic);
        this.sql = sql;
    }

    public FilterConsumer(String group, ConsumeFromWhere type, String topic) {
        super(group, type, topic);
    }

    public FilterConsumer(String nameServer, String group, String topic, String sql) {
        super(nameServer, group, topic, sql);
        this.sql = sql;
    }

    public FilterConsumer(String group, ConsumeFromWhere type, String topic, String sql) {
        super(group, type, topic);
        this.sql = sql;
    }

    public FilterConsumer(String group, String topic, String tag, MessageModel mode) {
        super(group, topic, tag, mode);
    }

    public FilterConsumer(String nameServer, String group, ConsumeFromWhere type, String topic, String sql, MessageModel mode) {
        super(nameServer, group, type, topic, mode);
        this.sql = sql;
    }

    // 获取过滤消费者实例
    public static DefaultMQPushConsumer getFilterMQPushConsumer(String nameServer, String group, ConsumeFromWhere type, String topic, String sql, MessageModel mode) throws MQClientException {
        // 定义一个filter的Consumer
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group);
        consumer.setNamesrvAddr(nameServer);
        consumer.setConsumeFromWhere(type);
        consumer.subscribe(topic, MessageSelector.bySql(sql));
        consumer.setMessageModel(mode);
        return consumer;
    }

    @Override
    public void receive() throws MQClientException {
        DefaultMQPushConsumer consumer;
        if (this.sql.length() == 0)
            consumer = getDefaultMQPushConsumer(this.nameServer, this.group, this.type, this.topic, this.tag, this.mode);

        else
            // 定义一个filter的Consumer
            consumer = getFilterMQPushConsumer(this.nameServer, this.group, this.type, this.topic, this.sql, this.mode);
        // 注册监听器
        // MessageListenerConcurrently为监听订阅消息
        // 返回值为当前消费者状态
        consumer.registerMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
            for (MessageExt msg : list) {
                System.out.print(new SimpleDateFormat("mm:ss").format(new Date()));
                System.out.println("->FilterConsumer:" + msg);
            }
            // 返回状态为成功
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        // 开启消费者
        consumer.start();
        System.out.println("FilterConsumer等待消息:" + consumer);
    }

    @Override
    public String toString() {
        return "FilterConsumer{" + "sql='" + sql + '\'' + ", nameServer='" + nameServer + '\'' + ", group='" + group + '\'' + ", type=" + type + ", topic='" + topic + '\'' + ", tag='" + tag + '\'' + ", mode=" + mode + '}';
    }

    public static void main(String[] args) throws MQClientException {
        new FilterConsumer("filterTopic", "age > 10").receive();
    }
}
