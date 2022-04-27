package cn.sysu;

import cn.sysu.config.RabbitMQComfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
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

    @Test
    void testConfirm() {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                System.out.println("消息投递后执行confirm方法");
                System.out.println(correlationData);
                System.out.println(b);
                System.out.println(s);
            }
        });
        rabbitTemplate.convertAndSend(RabbitMQComfig.EXCHANGE_NAME,"test1.hello","test-confirm");
    }

    @Test
    void testReturn() {
        //设置交换机处理失败消息的模式
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                System.out.println("return方法执行了");
            }
        });
        rabbitTemplate.convertAndSend(RabbitMQComfig.EXCHANGE_NAME,"test3.ming","test-return");
    }

}
