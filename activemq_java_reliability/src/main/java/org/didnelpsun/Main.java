// Main.java
package org.didnelpsun;

import org.didnelpsun.entity.ListenerConsumer;
import org.didnelpsun.entity.Producer;

public class Main {
    // 设置ActiveMQ的连接地址，后端运行在61616
    public static final String activemq_url = "tcp://127.0.0.1:61616";
    // 设置目的地队列名称
    public static final String queue_name = "queue_test";
    public static void main(String[] args) {
        if (new Producer(activemq_url, queue_name).send())
            System.out.println("消息发送完成");
        else
            System.out.println("消息发送失败");
        if (new ListenerConsumer(activemq_url,queue_name).receive())
            System.out.println("消息接收完成");
        else
            System.out.println("消息接收失败");
    }
}
