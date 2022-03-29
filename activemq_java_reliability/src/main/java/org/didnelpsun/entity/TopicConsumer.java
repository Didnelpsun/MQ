// TopicConsumer.java
package org.didnelpsun.entity;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class TopicConsumer extends Consumer {
    public TopicConsumer(String activemq_url, String topic_name) {
        super(activemq_url, topic_name);
    }
    @Override
    public boolean receive() {
        try {
            // 1.创建连接工厂，按照URL采用默认用户名和密码
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(activemq_url);
            // 2.连接工厂获得连接Connection，并启动访问
            Connection connection = activeMQConnectionFactory.createConnection();
            connection.start();
            // 3.创建会话，有两个参数，第一个是事务，第二个是签收
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 4.创建目的地，是队列还是主题
            Topic topic = session.createTopic(destination_name);
            // 5.创建消息的消费者
            MessageConsumer messageConsumer = session.createConsumer(topic);
            // 6.添加监听器
            messageConsumer.setMessageListener((message) -> {
                if (message instanceof TextMessage textMessage) {
                    try {
                        System.out.println("receiveTopicMessage:" + textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
            // 保证消费者进程存在
            System.in.read();
            messageConsumer.close();
            session.close();
            connection.close();
            return true;
        } catch (JMSException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
