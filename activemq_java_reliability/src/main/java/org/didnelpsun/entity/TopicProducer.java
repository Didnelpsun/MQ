// TopicProducer.java
package org.didnelpsun.entity;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class TopicProducer extends Producer {
    public TopicProducer(String activemq_url, String topic_name) {
        super(activemq_url, topic_name);
    }

    public boolean send() {
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(activemq_url);
            Connection connection = activeMQConnectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(destination_name);
            MessageProducer messageProducer = session.createProducer(topic);
            for (int i = 1; i <= 3; i++) {
                TextMessage textMessage = session.createTextMessage("sendTopicMessage:" + i);
                messageProducer.send(textMessage);
            }
            messageProducer.close();
            session.close();
            connection.close();
            return true;
        } catch (JMSException e) {
            e.printStackTrace();
            return false;
        }
    }
}
