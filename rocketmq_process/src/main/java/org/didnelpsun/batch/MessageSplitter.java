// MessageSplitter.java
package org.didnelpsun.batch;

import org.apache.rocketmq.common.message.Message;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

// 消息列表分割器
// 将所有要发送的数据传入消息分割器，如果其中有数据大小过大则分割这个数据
// 实现了迭代器
public class MessageSplitter implements Iterator<List<Message>> {
    // 极限大小值
    private int size = 4 * 1024 * 1024;
    // 消息列表
    private List<Message> messages;
    // 要进行批量发送消息的起始索引值
    private int index;

    public MessageSplitter(List<Message> messages) {
        this.messages = messages;
    }

    public MessageSplitter(List<Message> messages, int size) {
        this.messages = messages;
        this.size = size;
    }

    // 如何判断还有数据
    @Override
    public boolean hasNext() {
        return index < messages.size();
    }

    // 获取下一批数据进行遍历
    @Override
    public List<Message> next() {
        int nextIndex;
        // 计算当前要发送的一批数据的大小
        int totalSize = 0;
        for (nextIndex = index; nextIndex < messages.size(); nextIndex++) {
            // 获取到此轮的数据
            Message message = messages.get(nextIndex);
            // 获取主题数据的大小与消息主题的大小
            int tempSize = message.getTopic().length() + message.getBody().length;
            // 获取附加信息properties的大小
            // 将属性包装为一个Map类型
            for (Map.Entry<String, String> entry : message.getProperties().entrySet()) {
                tempSize += entry.getKey().length() + entry.getValue().length();
            }
            // 加上日志的20个字节
            tempSize += 20;
            // 假如当前消息本身就大于限制了，则只要包含这个消息就一定发不出去，则发送消息会被永远卡在这里
            if (tempSize > this.size) {
                // 如果前面还有消息则nextIndex - index != 0返回前面的消息跳到下面
                // 如果就只有这个消息，则直接返回这单个消息
                if (nextIndex - index == 0) {
                    System.out.println("消息[" + nextIndex + "]单个大于大小限制" + this.size + "B");
                    nextIndex++;
                }
                // 如果返回的单个消息超过最大限制，则需要其他函数对单个消息进行大小判断
            }
            // 计算当前数据大小加上之前的数据大小之和
            // 如果大于限制则表示不能再发送了，停止，否则添加并累计大小
            if (tempSize + totalSize > this.size) {
                break;
            } else {
                totalSize += tempSize;
            }
        }
        // 根据index截取list
        // 因为如果大于限制就退出循环，所以nextIndex的数据不应该被包含，并且下一次发送应该从nextIndex重新开始
        List<Message> list = messages.subList(index, nextIndex);
        this.index = nextIndex;
        return list;
    }

    @Override
    public void remove() {
        Iterator.super.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super List<Message>> action) {
        Iterator.super.forEachRemaining(action);
    }
}
