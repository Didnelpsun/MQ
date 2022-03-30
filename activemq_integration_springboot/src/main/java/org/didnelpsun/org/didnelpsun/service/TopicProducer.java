// TopicProducer.java
package org.didnelpsun.org.didnelpsun.service;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Topic;

@Service
public class TopicProducer {
    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Resource
    private Topic topic;
    @Scheduled(fixedDelay = 3000)
    public void send(){
        jmsMessagingTemplate.convertAndSend(topic, "send");
        System.out.println("定时发送");
    }
}
