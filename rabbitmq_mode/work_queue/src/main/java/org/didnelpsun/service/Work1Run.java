// Work1Run.java
package org.didnelpsun.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Work1Run {

    public static void main(String[] args) throws IOException, TimeoutException {
        new Worker(1).receive(1);
    }
}
