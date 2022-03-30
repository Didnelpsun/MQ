package org.didnelpsun;

import org.didnelpsun.config.ActiveMQConfig;
import org.didnelpsun.org.didnelpsun.service.QueueProducer;
import org.didnelpsun.org.didnelpsun.service.TopicProducer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import javax.annotation.Resource;
import java.io.IOException;

// 测试类全套写法
// 测试哪些类
@SpringBootTest(classes = {ActiveMQConfig.class, QueueProducer.class, TopicProducer.class})
// 进行测试的工具
@RunWith(SpringJUnit4ClassRunner.class)
// 表明WebApplicationContext应该使用Web应用程序根路径的默认值为测试加载资源
@WebAppConfiguration
@EnableScheduling
class ActivemqIntegrationSpringbootApplicationTests {
    @Resource
    private QueueProducer queueProducer;
    @Resource
    private TopicProducer topicProducer;
    @Test
    public void testSend(){
        queueProducer.send();
    }
    @Test
    public void testSendTime() throws IOException {
        queueProducer.sendTime();
        System.in.read();
    }
    @Test
    public void testTopic() throws IOException {
        topicProducer.send();
        System.in.read();
    }
}
