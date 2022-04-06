// PropertyConfig.java
package org.didnelpsun.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "property")
public class PropertyConfig {
    private String normalExchange;
    private String delayExchange;
    private String queue3;
    private String queue10;
    private String queueFree;
    private String queue;
    private String delay3;
    private String delay10;
    private String delay;
    private String send;
    private int ttl3;
    private int ttl10;

    public String getNormalExchange() {
        return normalExchange;
    }

    public void setNormalExchange(String normalExchange) {
        this.normalExchange = normalExchange;
    }

    public String getDelayExchange() {
        return delayExchange;
    }

    public void setDelayExchange(String delayExchange) {
        this.delayExchange = delayExchange;
    }

    public String getQueue3() {
        return queue3;
    }

    public void setQueue3(String queue3) {
        this.queue3 = queue3;
    }

    public String getQueue10() {
        return queue10;
    }

    public void setQueue10(String queue10) {
        this.queue10 = queue10;
    }

    public String getQueueFree() {
        return queueFree;
    }

    public void setQueueFree(String queueFree) {
        this.queueFree = queueFree;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getDelay3() {
        return delay3;
    }

    public void setDelay3(String delay3) {
        this.delay3 = delay3;
    }

    public String getDelay10() {
        return delay10;
    }

    public void setDelay10(String delay10) {
        this.delay10 = delay10;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public int getTtl3() {
        return ttl3;
    }

    public void setTtl3(int ttl3) {
        this.ttl3 = ttl3;
    }

    public int getTtl10() {
        return ttl10;
    }

    public void setTtl10(int ttl10) {
        this.ttl10 = ttl10;
    }
}
