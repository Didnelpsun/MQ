// Property.java
package org.didnelpsun;

public class Property {
    public static final String host = "127.0.0.1";
    public static final int port = 5672;
    public static final String username = "guest";
    public static final String password = "guest";
    // 队列名称
    public static final String QUEUE_NAME = "hello-world";
    // 发信息总个数
    public static final int COUNT = 1000;
    // 批量消息数
    public static final int BATCH = 105;
    // 交换机名称
    public static final String EXCHANGE = "exchange";
}