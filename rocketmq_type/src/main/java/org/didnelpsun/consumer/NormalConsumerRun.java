// NormalConsumerRun.java
package org.didnelpsun.consumer;

import org.apache.rocketmq.client.exception.MQClientException;

public class NormalConsumerRun {
    public static void main(String[] args) throws MQClientException {
        new PushConsumer("normalTopic").receive();
    }
}