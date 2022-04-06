// MqConfig.java
package org.didnelpsun.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
public class MqConfig {

    @Resource
    private PropertyConfig config;

    public void setConfig(PropertyConfig config) {
        this.config = config;
    }

    // 声明普通交换机
    @Bean
    public DirectExchange normalExchange() {
        return new DirectExchange(config.getNormalExchange());
    }

    // 声明死信交换机
    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(config.getDelayExchange());
    }

    //  声明队列
    // 普通队列
    public Queue normalQueue(int ttl, String queueName) {
        // 设置参数
        Map<String, Object> arguments = new HashMap<>();
        // 绑定普通队列和死信交换机
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", config.getDelayExchange());
        // 设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", config.getSend());
        // 设置TTL
        // 如果大于0就绑定TTL
        if (ttl > 0)
            arguments.put("x-message-ttl", ttl);
        // nonDurable为非持久化，传入队列名称
        return QueueBuilder.nonDurable(queueName).withArguments(arguments).build();
    }

    @Bean
    public Queue queue3() {
        return normalQueue(config.getTtl3(), config.getQueue3());
    }

    @Bean
    public Queue queue10() {
        return normalQueue(config.getTtl10(), config.getQueue10());
    }

    // 非固定TTL队列
    @Bean
    public Queue queueFree() {
        return normalQueue(0, config.getQueueFree());
    }

    // 死信队列
    @Bean
    public Queue queue() {
        return QueueBuilder.durable(config.getQueue()).build();
    }

    // 绑定队列和交换机
    // 普通队列和普通交换机
    public Binding bind(Queue queue, DirectExchange exchange, String key) {
        return BindingBuilder.bind(queue).to(exchange).with(key);
    }

    @Bean
    public Binding delay3(@Qualifier("queue3") Queue queue, @Qualifier("normalExchange") DirectExchange exchange) {
        return bind(queue, exchange, config.getDelay3());
    }

    @Bean
    public Binding delay10(@Qualifier("queue10") Queue queue, @Qualifier("normalExchange") DirectExchange exchange) {
        return bind(queue, exchange, config.getDelay10());

    }

    @Bean
    public Binding delay(@Qualifier("queueFree") Queue queue, @Qualifier("normalExchange") DirectExchange exchange) {
        return bind(queue, exchange, config.getDelay());

    }

    // 死信交换机与死信队列
    @Bean
    public Binding send(@Qualifier("queue") Queue queue, @Qualifier("delayExchange") DirectExchange exchange) {
        return bind(queue, exchange, config.getSend());
    }
}
