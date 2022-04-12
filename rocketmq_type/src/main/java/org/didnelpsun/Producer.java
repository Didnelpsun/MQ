// Producer.java
package org.didnelpsun;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;

// 生产者父类
public class Producer {
    // NameServer地址，并指定默认值
    protected String nameServer = "127.0.0.1:9876";
    // 组名
    protected String group;

    // 如果是一个参数就指定group
    public Producer(String group) {
        this.group = group;
    }

    public Producer(String nameServer, String group) {
        this.nameServer = nameServer;
        this.group = group;
    }

    // 直接获取MQProducer静态实例
    public static DefaultMQProducer getDefaultMQProducer(String nameServer, String group) {
        DefaultMQProducer producer = new DefaultMQProducer(group);
        producer.setNamesrvAddr(nameServer);
        return producer;
    }

    // 由于同步发送和异步发送不兼容，且两个重复发送次数都是int类型，所以一起设置，只有对应的类型对应的参数才会有用
    public static DefaultMQProducer getDefaultMQProducer(String nameServer, String group, int retryTimesWhenFailed, int sendMsgTimeout) {
        DefaultMQProducer producer = getDefaultMQProducer(nameServer, group);
        // 设置当发送失败是重试发送的次数，默认为为2次
        producer.setRetryTimesWhenSendFailed(retryTimesWhenFailed);
        producer.setRetryTimesWhenSendAsyncFailed(retryTimesWhenFailed);
        // 设置发送超时时限，默认为3s
        producer.setSendMsgTimeout(sendMsgTimeout);
        return producer;
    }

    public SendResult send(String topic, String tag, String message) throws Exception {
        return new SendResult();
    }
}
