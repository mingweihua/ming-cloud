package cn.sysu;

import cn.sysu.config.RabbitMQComfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProducerApplicationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
        rabbitTemplate.convertAndSend(RabbitMQComfig.EXCHANGE_NAME,"test1.hello","test-message1");
        rabbitTemplate.convertAndSend(RabbitMQComfig.EXCHANGE_NAME,"test2.word","test-message2");
    }

}
