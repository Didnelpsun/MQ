// Producer.java
package org.didnelpsun;

import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

import static org.didnelpsun.Property.*;

public class Producer {
    // 定义一个处理未确认消息的容器
    // 必须适用于高并发且线程安全有序
    public ConcurrentSkipListMap<Long, String> confirm;

    // 传入处理方法
    public void send(int count, IConfirm iConfirm) throws IOException, TimeoutException {
        Channel channel = RabbitUtil.getChannel();
        // 生成队列
        channel.queueDeclare(String.valueOf(count), true, false, false, null);
        // 开启发布确认
        channel.confirmSelect();
        // 获取开始时间
        long start = System.currentTimeMillis();
        // 批量发送消息方法，参数为信道以及发送信息条数
        // 返回值为布尔值，true为发送成功，false为发送失败
        if (!iConfirm.confirm(channel)) {
            System.out.println("发送消息失败");
        }
        // 获取结束时间
        System.out.println("执行" + count + "条命令确认需要" + (System.currentTimeMillis() - start) + "毫秒");
    }

    // 单个确认
    public void sendSingle(int count) throws IOException, TimeoutException {
        this.send(count, (channel) -> {
            try {
                // 批量发送消息
                for (int i = 1; i <= count; i++) {
                    channel.basicPublish("", QUEUE_NAME, null, (String.valueOf(i)).getBytes(StandardCharsets.UTF_8));
                    // 马上发布确认
                    if (channel.waitForConfirms()) {
//                        System.out.println("消息" + i + "发送成功！");
                    } else {
//                        System.out.println("消息" + i + "发送失败！");
                        return false;
                    }
                }
                return true;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    // 批量确认
    // 两个参数，第一个参数是发送消息的条数，第二个参数为批量确认中每批的条数
    public void sendBatch(int count, int batch) throws IOException, TimeoutException {
        this.send(count, (channel) -> {
            try {
                // 批量发送消息
                for (int i = 1; i <= count; i++) {
                    channel.basicPublish("", QUEUE_NAME, null, (String.valueOf(i)).getBytes(StandardCharsets.UTF_8));
                    // 如果到每一批再确认，即当前次数模批容量为0
                    if (i % batch == 0) {
                        if (channel.waitForConfirms()) {
//                            System.out.println("消息第" + i/batch + "批（" + (i-batch) + "-" + i + "）发送成功！");
                        } else {
//                            System.out.println("消息第" + i/batch + "批（" + (i-batch) + "-" + i + "）发送失败！");
                            return false;
                        }
                    }
                    // 如果总数不能被批数整除，则最后几个不能被确认
                    // 需要判断count能否被batch整除，如果不能则最后一个还要继续确认一次
                    if (!(count % batch == 0) && i == count) {
                        if (channel.waitForConfirms()) {
//                            System.out.println("消息第" + (i/batch+1) + "批（" + (i-i%batch) + "-" + i + "）发送成功！");
                        } else {
//                            System.out.println("消息第" + (i/batch+1) + "批（" + (i-i%batch) + "-" + i + "）发送失败！");
                            return false;
                        }
                    }
                }
                return true;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    // 异步确认
    public void sendAsync(int count) throws IOException, TimeoutException {
        this.send(count, (channel) -> {
            try {
                // 准备消息监听器，监听消息成功或失败
                // 第一个参数是对确认的回调处理，第二个参数是对未确认的回调处理
                channel.addConfirmListener(
                        (deliveryTag, multiple) -> {
                            if (multiple) {
                                // 如果是批处理则获取一批
                                // headMap返回这个标记之前的所有
                                ConcurrentNavigableMap<Long, String> confirmed = confirm.headMap(deliveryTag);
                                // 清除
                                confirmed.clear();
                            } else {
                                // 如果是非批处理，则删除已经确认的单条消息
                                confirm.remove(deliveryTag);
                            }
//                            System.out.println("消息" + deliveryTag + "发送成功！");
                        }, (deliveryTag, multiple) -> {
//                            System.out.println("消息" + deliveryTag + "发送失败！");
                            System.out.println("消息" + confirm.get(deliveryTag) + "发送失败");
                        }
                );
                // 批量发送消息，只负责发消息
                for (int i = 1; i <= count; i++) {
                    // 把消息放入容器
                    // key为getNextPublishSeqNo获取下一个发布的序号，value即message
                    // 因为现在还没有发布信息所以下一个发布的序号就是当前循环的序号
                    confirm.put(channel.getNextPublishSeqNo(), String.valueOf(i));
                    channel.basicPublish("", QUEUE_NAME, null, (String.valueOf(i)).getBytes(StandardCharsets.UTF_8));
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        // 单个确认发布
        new Producer().sendSingle(COUNT);
        // 批量确认发布
        new Producer().sendBatch(COUNT, BATCH);
        // 异步确认发布
        new Producer().sendAsync(COUNT);
    }
}
