// Producer.java
package org.didnelpsun.entity;

public class Producer {
    protected String activemq_url;
    protected String destination_name;
    public Producer(String activemq_url, String destination_name){
        this.activemq_url = activemq_url;
        this.destination_name = destination_name;
    }
    public boolean send() {
        return true;
    }
}
