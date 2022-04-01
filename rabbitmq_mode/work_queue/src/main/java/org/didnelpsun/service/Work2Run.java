// Work2Run.java
package org.didnelpsun.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Work2Run {

    public static void main(String[] args) throws IOException, TimeoutException {
        new Worker(2).receive();
    }
}
