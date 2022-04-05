// NormalConsumerRun.java
package org.didnelpsun;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static org.didnelpsun.Property.DEAD_QUEUE;

public class NormalConsumerRun {
    public static void main(String[] args) throws IOException, TimeoutException {
        new Consumer(DEAD_QUEUE).receive((consumerTag, message) -> new String(message.getBody(), StandardCharsets.UTF_8).length() <= 10);
    }
}
