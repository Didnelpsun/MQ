// QueueProducer.java
package org.didnelpsun.entity;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class QueueProducer extends Producer {
    public QueueProducer(String activemq_url, String queue_name) {
        super(activemq_url, queue_name);
    }

    public boolean send() {
        try{
            // 1.创建连接工厂，按照URL采用默认用户名和密码
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(activemq_url);
            // 2.连接工厂获得连接Connection，并启动访问
            Connection connection = activeMQConnectionFactory.createConnection();
            connection.start();
            // 3.创建会话，有两个参数，第一个是事务，第二个是签收
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 4.创建目的地，是队列还是主题
            Queue queue = session.createQueue(destination_name);
            // 5.创建消息的生产者
            MessageProducer messageProducer = session.createProducer(queue);
            // 6.使用MessageProducer生产消息发送到MQ的队列中
            for (int i = 1; i <= 3; i++) {
                // 7.创建消息，可以视为一个字符串
                TextMessage textMessage = session.createTextMessage("sendQueueMessage:"+i);
                // 8.通过MessageProducer发送消息给MQ
                messageProducer.send(textMessage);
            }
            // 9.关闭资源
            messageProducer.close();
            session.close();
            connection.close();
            return true;
        } catch (JMSException e){
            e.printStackTrace();
            return false;
        }
    }
}
