package cn.sysu;

import cn.sysu.config.RabbitMQComfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
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
        rabbitTemplate.convertAndSend(RabbitMQComfig.EXCHANGE_NAME,"testReturn.ming","test-return");
    }


    //测试队列消息统一过期
    @Test
    void testTTL1() {
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                System.out.println("return方法执行了");
            }
        });
        rabbitTemplate.convertAndSend(RabbitMQComfig.EXCHANGE_NAME,"test3.ttl","test-ttl");
    }

    //测试消息单独过期
    @Test
    void testTTL2() {

        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置消息5秒后过期
                message.getMessageProperties().setExpiration("5000");
                return message;
            }
        };
        rabbitTemplate.convertAndSend(RabbitMQComfig.EXCHANGE_NAME,"test2.ttl2","test-ttl2",messagePostProcessor);
    }

}
