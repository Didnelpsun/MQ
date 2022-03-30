// QueueProducer.java
package org.didnelpsun.org.didnelpsun.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.jms.Queue;

@Service
public class QueueProducer {
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    public void setJmsMessagingTemplate(JmsMessagingTemplate jmsMessagingTemplate) {
        this.jmsMessagingTemplate = jmsMessagingTemplate;
    }
    private Queue queue;
    @Autowired
    public void setQueue(Queue queue) {
        this.queue = queue;
    }
    public void send(){
        jmsMessagingTemplate.convertAndSend(queue, "send");
    }
    // 定时发送
    @Scheduled(fixedDelay = 3000)
    public void sendTime(){
        send();
        System.out.println("定时发送");
    }
}
