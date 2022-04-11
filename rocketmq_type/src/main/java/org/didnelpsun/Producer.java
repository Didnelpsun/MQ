// Producer.java
package org.didnelpsun;

import org.apache.rocketmq.client.producer.SendResult;

// 生产者父类
public class Producer {
    // NameServer地址，并指定默认值
    protected String nameServer = "127.0.0.1:9876";
    // 组名
    protected String producerGroup;
    // 如果是一个参数就指定producerGroup
    public Producer(String producerGroup){
        this.producerGroup = producerGroup;
    }
    public Producer(String nameServer, String producerGroup){
        this.nameServer = nameServer;
        this.producerGroup = producerGroup;
    }

    public SendResult send(String topic, String tag, String message) throws Exception {
        return new SendResult();
    }
}
