// AckQueueProducer.java
package org.didnelpsun.entity;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.AsyncCallback;

import javax.jms.*;
import java.util.UUID;

public class AckQueueProducer extends Producer {
    public AckQueueProducer(String activemq_url, String queue_name) {
        super(activemq_url, queue_name);
    }

    public boolean send() {
        try {
            // 1.创建连接工厂，按照URL采用默认用户名和密码
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(activemq_url);
            // 设置同步
            activeMQConnectionFactory.setUseAsyncSend(true);
            // 2.连接工厂获得连接Connection，并启动访问
            Connection connection = activeMQConnectionFactory.createConnection();
            connection.start();
            // 3.创建会话，有两个参数，第一个是事务，第二个是签收
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 4.创建目的地，是队列还是主题
            Queue queue = session.createQueue(destination_name);
            // 5.创建消息的生产者
            // 使用需要确认的消息生产者
            ActiveMQMessageProducer activeMQMessageProducer = (ActiveMQMessageProducer) session.createProducer(queue);
            // 6.使用MessageProducer生产消息发送到MQ的队列中
            for (int i = 1; i <= 3; i++) {
                // 生产一个随机ID
                String id = UUID.randomUUID().toString();
                // 7.创建消息，可以视为一个字符串
                TextMessage textMessage = session.createTextMessage("sendAckQueueMessage:" + i + ",id:" + id);
                // 设置消息ID
                textMessage.setJMSMessageID(id);
                // 8.通过MessageProducer发送消息给MQ，并获取回调函数
                activeMQMessageProducer.send(textMessage, new AsyncCallback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("id:" + id + "消息发送成功");
                    }

                    @Override
                    public void onException(JMSException e) {
                        e.printStackTrace();
                        System.out.println("id:" + id + "消息发送失败");
                    }
                });
            }
            // 9.关闭资源
            activeMQMessageProducer.close();
            session.close();
            connection.close();
            return true;
        } catch (JMSException e) {
            e.printStackTrace();
            return false;
        }
    }
}
