// TransactionProducer.java
package org.didnelpsun.transaction;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.didnelpsun.Producer;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.*;

// 事务生产者
public class TransactionProducer extends Producer {
    private int corePoolSize = 10;
    private int maximumPoolSize = 100;
    private int keepAliveTime = 5;
    private TimeUnit unit = TimeUnit.SECONDS;
    private int capacity = 1000;

    public TransactionProducer() {
        super("transaction");
    }

    public TransactionProducer(String nameServer, String group) {
        super(nameServer, group);
    }

    public TransactionProducer(String nameServer, String group, int corePoolSize, int maximumPoolSize) {
        super(nameServer, group);
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
    }

    public TransactionProducer(String nameServer, String group, int corePoolSize, int maximumPoolSize, int capacity) {
        super(nameServer, group);
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.capacity = capacity;
    }

    public TransactionProducer(String nameServer, String group, int corePoolSize, int maximumPoolSize, int keepAliveTime, int capacity) {
        super(nameServer, group);
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.capacity = capacity;
    }

    public TransactionProducer(String nameServer, String group, int corePoolSize, int maximumPoolSize, int keepAliveTime, TimeUnit unit, int capacity) {
        super(nameServer, group);
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        this.capacity = capacity;
    }


    // corePoolSize指在池中保持的核心线程数
    // maximumPoolSize指在池中保持的最大线程数
    // keepAliveTime指线程池中线程数量大于核心线程数量时，多余空闲线程的存活时间
    // unit指时间单位
    // workQueue指当请求的线程多于maximumPoolSize时消息请求存放的临时工作队列
    // capacity指可容纳的待处理消息数量
    public ThreadPoolExecutor getThreadPoolExecutor() {
        return new ThreadPoolExecutor(this.corePoolSize, this.maximumPoolSize, this.keepAliveTime, this.unit, new ArrayBlockingQueue<>(this.capacity), r -> {
            Thread thread = new Thread(r);
            thread.setName("transaction-" + new SimpleDateFormat("mm:ss:S").format(new Date()));
            return thread;
        });
    }

    public SendResult send(String topic, String message) throws Exception {
        return send(topic, "", message);
    }

    public SendResult send(String topic, String tag, String message) throws MQClientException {
        return send(topic, tag, message, 2, 3000);
    }

    public SendResult send(String topic, String tag, String message, int retryTimesWhenSendFailed, int sendMsgTimeout) throws MQClientException {
        TransactionMQProducer producer = Producer.getTransactionMQProducer(this.nameServer, this.group, retryTimesWhenSendFailed, sendMsgTimeout);
        // 生产者指定线程池
        producer.setExecutorService(getThreadPoolExecutor());
        // 生产者添加事务监听器
        producer.setTransactionListener(new DefaultTransactionListener());
        // 开启生产者
        producer.start();
        // 生产消息
        Message msg = new Message(topic, tag, message.getBytes(StandardCharsets.UTF_8));
        // 发送事务消息
        // 第二个参数用于指定在执行本地事务需要的一些业务参数
        SendResult result = producer.sendMessageInTransaction(msg, null);
        // 获取回执
        if (result.getSendStatus() == SendStatus.SEND_OK)
            System.out.println("TransactionProducer发送完成");
        else
            System.out.println("TransactionProducer发送失败");
        producer.shutdown();
        return result;
    }

    public ArrayList<SendResult> send(String topic, ArrayList<String> tags, ArrayList<String> messages) throws MQClientException {
        return send(topic, tags, messages, 2, 3000);
    }

    public ArrayList<SendResult> send(String topic, ArrayList<String> tags, ArrayList<String> messages, int retryTimesWhenSendFailed, int sendMsgTimeout) throws MQClientException {
        if (tags.size() == 0 || messages.size() == 0) {
            System.out.println("tags和messages不应为空");
            return null;
        }
        TransactionMQProducer producer = Producer.getTransactionMQProducer(this.nameServer, this.group, retryTimesWhenSendFailed, sendMsgTimeout);
        // 生产者指定线程池
        producer.setExecutorService(getThreadPoolExecutor());
        // 生产者添加事务监听器
        producer.setTransactionListener(new DefaultTransactionListener());
        // 开启生产者
        producer.start();
        // 定义一个返回数组
        ArrayList<SendResult> results = new ArrayList<>();
        // 循环，针对message大小，如果tag大小不一致则不一一对应，如果tag数量小于message数量则取模运算
        for (int i = 0; i < messages.size(); i++) {
            // 生产消息
            Message msg = new Message(topic, tags.get(i % tags.size()), messages.get(i).getBytes(StandardCharsets.UTF_8));
            // 发送事务消息
            // 第二个参数用于指定在执行本地事务需要的一些业务参数
            SendResult result = producer.sendMessageInTransaction(msg, null);
            // 获取回执
            if (result.getSendStatus() == SendStatus.SEND_OK)
                System.out.println("TransactionProducer发送完成");
            else
                System.out.println("TransactionProducer发送失败");
            results.add(result);
        }
        producer.shutdown();
        return results;
    }

    public static void main(String[] args) throws Exception {
        ArrayList<String> tags = new ArrayList<>();
        tags.add("commit");
        tags.add("rollback");
        tags.add("unknow");
        ArrayList<String> messages = new ArrayList<>();
        messages.add("TransactionProducer-commit");
        messages.add("TransactionProducer-rollback");
        messages.add("TransactionProducer-unknow");
        System.out.println(new TransactionProducer().send("transactionTopic", tags, messages));
    }
}
