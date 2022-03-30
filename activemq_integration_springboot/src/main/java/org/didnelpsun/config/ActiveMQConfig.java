// ActiveMQConfig.java
package org.didnelpsun.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;

@Configuration
// 开启JMS
@EnableJms
public class ActiveMQConfig {
    @Value("${queueName}")
    private String queue;
    @Value("${topicName}")
    private String topic;
    @Bean
    public Queue queue(){
        return new ActiveMQQueue(queue);
    }
    @Bean
    public Topic topic(){
        return new ActiveMQTopic(topic);
    }
    @Bean
    ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory();
    }
    @Bean
    JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setPriority(999);
        return jmsTemplate;
    }
    @Bean(value="jmsMessagingTemplate")
    JmsMessagingTemplate jmsMessagingTemplate(JmsTemplate jmsTemplate) {
        return new JmsMessagingTemplate(jmsTemplate);
    }
}
