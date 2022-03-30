// QueueConsumer
package org.didnelpsun.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    private JmsTemplate jmsTemplate;
    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        Consumer consumer = applicationContext.getBean("consumer", Consumer.class);
        // 接收消息
        String text = (String) consumer.jmsTemplate.receiveAndConvert();
        System.out.println("receive:" + text);
    }
}
