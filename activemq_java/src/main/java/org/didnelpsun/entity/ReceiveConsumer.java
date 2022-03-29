// ReceiveConsumer.java
package org.didnelpsun.entity;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ReceiveConsumer extends Consumer {
    public ReceiveConsumer(String activemq_url, String queue_name) {
        super(activemq_url, queue_name);
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
            Queue queue = session.createQueue(destination_name);
            // 5.创建消息的消费者
            MessageConsumer messageConsumer = session.createConsumer(queue);
            while (true) {
                // 消费者存在时间为3秒，并获取消费者回复消息
                TextMessage textMessage = (TextMessage) messageConsumer.receive(3000);
                // 如果消费者收到消息并回复了，就打印
                if (textMessage != null)
                    System.out.println("receiveQueueMessage:" + textMessage.getText());
                else
                    break;
            }
            // 6.关闭资源
            messageConsumer.close();
            session.close();
            connection.close();
            return true;
        } catch (JMSException e) {
            e.printStackTrace();
            return false;
        }
    }
}
