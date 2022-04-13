// DelayConsumerRun.java
package org.didnelpsun.consumer;

import org.apache.rocketmq.client.exception.MQClientException;

public class DelayConsumerRun {
    public static void main(String[] args) throws MQClientException {
        new PushConsumer("delayTopic").receive();
    }
}
