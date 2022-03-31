// DelayQueueConsumerTest.java
package org.didnelpsun;

import org.didnelpsun.entity.ListenerConsumer;

public class DelayQueueConsumerTest {
    public static final String activemq_url = "tcp://127.0.0.1:61616";
    public static final String queue_name = "queue_test";

    public static void main(String[] args) {
        if (new ListenerConsumer(activemq_url, queue_name).receive())
            System.out.println("消息接收完成");
        else
            System.out.println("消息接收失败");
    }
}
