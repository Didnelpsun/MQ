// Producer.java
package org.didnelpsun;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static org.didnelpsun.Property.QUEUE_NAME;

public class Producer {
    // 批量发信息的个数
    public static final int COUNT = 100;

    public void send() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitUtil.getChannel();
        // 生成队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        // 获取开始时间
        long start = System.currentTimeMillis();
        // 批量发送消息
        for (int i = 0; i < COUNT; i++) {
            channel.basicPublish("", QUEUE_NAME, null, (String.valueOf(i)).getBytes(StandardCharsets.UTF_8));
            // 马上发布确认
            if (channel.waitForConfirms()) {
                System.out.println("消息" + i + "发送成功！");
            }
        }
        // 获取结束时间
        System.out.println("执行"+ COUNT +"条命令单独确认需要" + (System.currentTimeMillis() - start) + "毫秒");
    }

    // 单个确认
    public void sendSingle() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitUtil.getChannel();
        // 生成队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        // 获取开始时间
        long start = System.currentTimeMillis();
        // 批量发送消息
        for (int i = 0; i < COUNT; i++) {
            channel.basicPublish("", QUEUE_NAME, null, (String.valueOf(i)).getBytes(StandardCharsets.UTF_8));
            // 马上发布确认
            if (channel.waitForConfirms()) {
                System.out.println("消息" + i + "发送成功！");
            }
        }
        // 获取结束时间
        System.out.println("执行"+ COUNT +"条命令单独确认需要" + (System.currentTimeMillis() - start) + "毫秒");
    }

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        // 单个确认发布
        new Producer().sendSingle();
    }
}
