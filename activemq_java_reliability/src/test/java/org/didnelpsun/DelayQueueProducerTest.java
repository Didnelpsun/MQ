// DelayQueueProducerTest.java
package org.didnelpsun;

import org.didnelpsun.entity.DelayQueueProducer;

public class DelayQueueProducerTest {
    public static final String activemq_url = "tcp://127.0.0.1:61616";
    public static final String queue_name = "queue_test";

    public static void main(String[] args) {
        if (new DelayQueueProducer(activemq_url, queue_name).send())
            System.out.println("消息发送完成");
        else
            System.out.println("消息发送失败");
    }
}
