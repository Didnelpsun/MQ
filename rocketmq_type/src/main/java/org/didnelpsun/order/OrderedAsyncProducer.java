// OrderedSyncProducer.java
package org.didnelpsun.order;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.didnelpsun.Producer;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

// 异步顺序消息生产者
public class OrderedAsyncProducer extends Producer {
    // 异步等待确认的睡眠时间
    private int sleep;

    public OrderedAsyncProducer() {
        super("ordered");
        this.sleep = 5;
    }

    public OrderedAsyncProducer(String group) {
        super(group);
        this.sleep = 5;
    }

    public OrderedAsyncProducer(String nameServer, String group) {
        super(nameServer, group);
        this.sleep = 5;
    }

    public OrderedAsyncProducer(String nameServer, String group, int sleep) {
        super(nameServer, group);
        this.sleep = sleep;
    }

    public SendResult send(String topic, String tag, String message, long orderId) throws MQClientException, RemotingException, InterruptedException {
        return send(topic, tag, message, orderId, 2, 3000);
    }

    // 需要多传入一个选择id
    public SendResult send(String topic, String tag, String message, long orderId, int retryTimesWhenSendAsyncFailed, int sendMsgTimeout) throws MQClientException, RemotingException, InterruptedException {
        DefaultMQProducer producer = Producer.getDefaultMQProducer(this.nameServer, this.group, retryTimesWhenSendAsyncFailed, sendMsgTimeout);
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
        final SendResult[] result = new SendResult[]{null};
        producer.send(msg, (list, message1, o) -> {
            // 获取设置的消息key
            String id = msg.getKeys();
//            Long id = (Long) o;
            int index = Integer.parseInt(id) % list.size();
            return list.get(index);
        }, orderId, new SendCallback() {
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
        System.out.println("OrderedAsyncProducer发送完成");
        return result[0];
    }

    public static void main(String[] args) throws RemotingException, InterruptedException, MQClientException {
        System.out.println(new OrderedAsyncProducer().send("orderedTopic", "async", "OrderedAsyncProducer", 0));
    }
}
