// ConsumerRun1.java
package org.didnelpsun;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.didnelpsun.Property.EXCHANGE;

public class ConsumerRun1 {
    public static void main(String[] args) throws IOException, TimeoutException {
        new Consumer(EXCHANGE, "1").receive();
    }
}
