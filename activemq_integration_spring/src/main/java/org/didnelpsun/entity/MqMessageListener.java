// MqMessageListener.java
package org.didnelpsun.entity;

import org.springframework.stereotype.Component;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component
public class MqMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        if(message instanceof TextMessage textMessage){
            try {
                System.out.println("receive:" + textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
