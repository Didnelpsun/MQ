// QueueProducer
package org.didnelpsun.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {
    private JmsTemplate jmsTemplate;
    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        Producer producer = applicationContext.getBean("producer", Producer.class);
        // Lambda表达式直接发送信息
        producer.jmsTemplate.send(session -> session.createTextMessage("send"));
        System.out.println("发送成功");
    }
}
