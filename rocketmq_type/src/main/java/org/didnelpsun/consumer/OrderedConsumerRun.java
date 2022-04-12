// OrderedConsumerRun.java
package org.didnelpsun.consumer;

import org.apache.rocketmq.client.exception.MQClientException;

public class OrderedConsumerRun {
    public static void main(String[] args) throws MQClientException {
        new PushConsumer("orderedTopic").receive();
    }
}