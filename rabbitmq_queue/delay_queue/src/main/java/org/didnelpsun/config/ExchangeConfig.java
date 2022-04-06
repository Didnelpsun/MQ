// ExchangeConfig.java
package org.didnelpsun.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ExchangeConfig {

    @Resource
    private PropertyConfig config;

    public void setConfig(PropertyConfig config) {
        this.config = config;
    }

    // 声明延迟交换机
    // 由于这是个插件导入的，所以需要自定义交换机
    @Bean
    public CustomExchange delayExchange(){
        Map<String, Object> arguments = new HashMap<>();
        // 消息延迟发送的类型，为路由类型，直接根据RoutingKey发送消息
        arguments.put("x-delayed-type", "direct");
        // 参数分别为：名字、类型、是否持久化，是否自动删除，附加参数
        return new CustomExchange(config.getDelayExchange(), "x-delayed-message", false, false, arguments);
    }

    //  声明队列
    @Bean
    public Queue queue() {
        return QueueBuilder.durable(config.getQueue()).build();
    }

    @Bean
    // 绑定队列和交换机
    // 普通队列和延迟交换机
    public Binding delay(@Qualifier("queue") Queue queue, @Qualifier("delayExchange") CustomExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(config.getDelay()).noargs();
    }
}
