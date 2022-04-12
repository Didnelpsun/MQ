// AsyncProducer.java
package org.didnelpsun.normal;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.didnelpsun.Producer;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

// 异步生产者
public class AsyncProducer extends Producer {
    // 异步等待确认的睡眠时间
    private int sleep;

    public AsyncProducer() {
        super("normal");
        this.sleep = 5;
    }

    public AsyncProducer(String nameServer, String group) {
        super(nameServer, group);
        this.sleep = 5;
    }

    public AsyncProducer(String nameServer, String group, int sleep) {
        super(nameServer, group);
        this.sleep = sleep;
    }

    public SendResult send(String topic, String message) throws MQClientException, RemotingException, InterruptedException {
        return send(topic, "", message);
    }

    public SendResult send(String topic, String tag, String message) throws RemotingException, InterruptedException, MQClientException {
        // 默认不异步重发
        return send(topic, tag, message, 2, 3000);
    }

    public SendResult send(String topic, String tag, String message, int retryTimesWhenSendAsyncFailed, int sendMsgTimeout) throws MQClientException, RemotingException, InterruptedException {
        DefaultMQProducer producer = Producer.getDefaultMQProducer(this.nameServer, this.group, retryTimesWhenSendAsyncFailed, sendMsgTimeout);
        // 开启生产者
        producer.start();
        // 生产消息
        Message msg = new Message(topic, tag, message.getBytes(StandardCharsets.UTF_8));
        // 发送消息，需要传入一个异步回调函数
        final SendResult[] result = new SendResult[]{null};
        producer.send(msg, new SendCallback() {
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
        System.out.println("AsyncProducer发送完成");
        return result[0];
    }

    public static void main(String[] args) throws RemotingException, InterruptedException, MQClientException {
        System.out.println(new AsyncProducer().send("normalTopic", "async", "AsyncProducer"));
    }
}
