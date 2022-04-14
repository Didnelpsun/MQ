// AsyncFilterProducer.java
package org.didnelpsun.filter;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.didnelpsun.Producer;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

// 参数异步生产者
public class AsyncFilterProducer extends FilterProducer {
    // 异步等待确认的睡眠时间
    private int sleep;

    public AsyncFilterProducer() {
        super();
        this.sleep = 5;
    }

    public AsyncFilterProducer(Map<String, Object> properties) {
        super();
        this.properties = properties;
        this.sleep = 5;
    }

    public AsyncFilterProducer(String group) {
        super(group);
        this.sleep = 5;
    }

    public AsyncFilterProducer(String group, int sleep) {
        super(group);
        this.sleep = sleep;
    }

    public AsyncFilterProducer(String nameServer, String group) {
        super(nameServer, group);
        this.sleep = 5;
    }

    public AsyncFilterProducer(String nameServer, String group, int sleep) {
        super(nameServer, group);
        this.sleep = sleep;
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
        // 发送消息，需要传入一个异步回调函数
        final SendResult[] result = new SendResult[]{null};
        producer.send(this.putUserProperty(msg), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                result[0] = sendResult;
            }

            @Override
            public void onException(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        // 必须休眠等待发送结果，否则会直接关闭
        TimeUnit.SECONDS.sleep(this.sleep);
        producer.shutdown();
        System.out.println("AsyncFilterProducer发送完成");
        return result[0];
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> prop = new HashMap<>();
        prop.put("age", 15);
        System.out.println(new AsyncFilterProducer(prop).send("filterTopic", "async", "AsyncFilterProducer"));
    }
}
