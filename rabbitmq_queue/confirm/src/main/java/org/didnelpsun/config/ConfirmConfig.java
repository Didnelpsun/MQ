// ConfirmConfig.java
package org.didnelpsun.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.didnelpsun.config.PropertyConfig.*;

@Configuration
// 发布确认配置
public class ConfirmConfig {
    // 交换机
    @Bean
    public DirectExchange confirmExchange() {
        // 转发给备份交换机
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE).build();
    }

    // 队列
    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.nonDurable(CONFIRM_QUEUE).build();
    }

    // 绑定队列与交换机
    @Bean
    public Binding confirmBind(@Qualifier("confirmQueue") Queue queue, @Qualifier("confirmExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(CONFIRM_ROUTING_KEY);
    }

    // 交换机
    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE);
    }

    // 队列
    @Bean
    public Queue backupQueue() {
        return QueueBuilder.nonDurable(BACKUP_QUEUE).build();
    }

    // 绑定队列与交换机
    @Bean
    public Binding backupBind(@Qualifier("backupQueue") Queue queue, @Qualifier("backupExchange") FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    // 队列
    @Bean
    public Queue warnQueue() {
        return QueueBuilder.nonDurable(WARN_QUEUE).build();
    }

    // 绑定队列与交换机
    @Bean
    public Binding warnBind(@Qualifier("warnQueue") Queue queue, @Qualifier("backupExchange") FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }
}
