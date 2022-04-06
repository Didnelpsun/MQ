// Controller.java
package org.didnelpsun.controller;

import org.didnelpsun.config.PropertyConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
public class Controller {
    // 注入参数
    @Resource
    private PropertyConfig config;

    public void setConfig(PropertyConfig config) {
        this.config = config;
    }

    // RabbitMQ模板
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send/{msg}")
    public String send(@PathVariable String msg) {
        rabbitTemplate.convertAndSend(config.getNormalExchange(), config.getDelay3(), new Date() + "延迟三秒消息：" + msg);
        rabbitTemplate.convertAndSend(config.getNormalExchange(), config.getDelay10(), new Date() + "延迟十秒消息：" + msg);
        return "延迟发送成功";
    }

    @GetMapping("/send/{ttl}/{msg}")
    public String send(@PathVariable String ttl, @PathVariable String msg) {
        // 需要传入一个后置处理器对消息处理
        rabbitTemplate.convertAndSend(config.getNormalExchange(), config.getDelay(), new Date() + "延迟" + (Float.parseFloat(ttl) / 1000) + "秒消息：" + msg, message -> {
            // 设置存活时间
            message.getMessageProperties().setExpiration(ttl);
            return message;
        });
        return "延迟发送成功";
    }

    @GetMapping("/delay/{delay}/{msg}")
    public String send(@PathVariable Integer delay, @PathVariable String msg) {
        // 需要传入一个后置处理器对消息处理
        rabbitTemplate.convertAndSend(config.getDelayExchange(), config.getDelay(), new Date() + "延迟" + (delay / 1000) + "秒消息：" + msg, message -> {
            // 设置存活时间
            message.getMessageProperties().setDelay(delay);
            return message;
        });
        return "延迟发送成功";
    }
}
