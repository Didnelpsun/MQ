// DeadConsumerRun.java
package org.didnelpsun;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.didnelpsun.Property.DEAD_QUEUE;

public class DeadConsumerRun {
    public static void main(String[] args) throws IOException, TimeoutException {
        new Consumer(DEAD_QUEUE).receive((consumerTag, message) -> true);
    }
}
