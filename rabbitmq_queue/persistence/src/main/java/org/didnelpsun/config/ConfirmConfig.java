// ConfirmConfig.java
package org.didnelpsun.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import static org.didnelpsun.config.PropertyConfig.*;

// 发布确认配置
public class ConfirmConfig {
    // 交换机
    @Bean
    public DirectExchange confirmExchange(){
        return new DirectExchange(CONFIRM_EXCHANGE);
    }
    // 队列
    @Bean
    public Queue confirmQueue(){
        return QueueBuilder.nonDurable(CONFIRM_QUEUE).build();
    }
    // 绑定队列与交换机
    @Bean
    public Binding confirmBind(@Qualifier("confirmQueue") Queue confirmQueue, @Qualifier("confirmExchange") DirectExchange confirmExchange){
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }
}
