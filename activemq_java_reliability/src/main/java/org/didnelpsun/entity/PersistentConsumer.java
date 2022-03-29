// PersistentConsumer.java
package org.didnelpsun.entity;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class PersistentConsumer extends Consumer {
    protected String clientId;

    public PersistentConsumer(String activemq_url, String topic_name) {
        super(activemq_url, topic_name);
        this.clientId = "clientId";
    }

    public PersistentConsumer(String activemq_url, String topic_name, String clientId) {
        super(activemq_url, topic_name);
        this.clientId = clientId;
    }

    @Override
    public boolean receive() {
        try {
            // 1.创建连接工厂，按照URL采用默认用户名和密码
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(activemq_url);
            // 2.连接工厂获得连接Connection，并启动访问
            Connection connection = activeMQConnectionFactory.createConnection();
            // 设置订阅者ID
            connection.setClientID(this.clientId);
            // 3.创建会话，有两个参数，第一个是事务，第二个是签收
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 4.创建目的地，是队列还是主题
            Topic topic = session.createTopic(destination_name);
            // 5.创建消息的消费者
            // 参数topic为发送端的主题，参数name为这个订阅的名字
            TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, this.clientId + "subscribe");
            // 启动服务
            connection.start();
            // 6.进行接收
            Message message = topicSubscriber.receive();
            while (message != null){
                TextMessage textMessage = (TextMessage) message;
                System.out.println("receivePersistentTopicMessage:" + textMessage.getText());
                message = topicSubscriber.receive(3000);
            }
            // 7.关闭资源
            topicSubscriber.close();
            session.close();
            connection.close();
            return true;
        } catch (JMSException e) {
            e.printStackTrace();
            return false;
        }
    }
}
