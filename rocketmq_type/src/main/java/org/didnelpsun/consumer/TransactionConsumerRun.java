// TransactionConsumerRun.java
package org.didnelpsun.consumer;

import org.apache.rocketmq.client.exception.MQClientException;

public class TransactionConsumerRun {
    public static void main(String[] args) throws MQClientException {
        new PushConsumer("transactionTopic").receive();
    }
}