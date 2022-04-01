// Work3Run.java
package org.didnelpsun.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Work3Run {

    public static void main(String[] args) throws IOException, TimeoutException {
        new Worker(3).receive();
    }
}
