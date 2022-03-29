// PersistentTest.java
package org.didnelpsun;

import org.didnelpsun.entity.PersistentConsumer;

public class PersistentTest {
    // 设置ActiveMQ的连接地址，后端运行在61616
    public static final String activemq_url = "tcp://127.0.0.1:61616";
    // 设置目的地主题名称
    public static final String topic_name = "topic_test";
    public static void main(String[] args) {
        if (new PersistentConsumer(activemq_url, topic_name).receive())
            System.out.println("消息接收完成");
        else
            System.out.println("消息接收失败");
    }
}
