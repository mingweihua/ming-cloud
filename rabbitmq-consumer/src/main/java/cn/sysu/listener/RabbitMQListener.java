package cn.sysu.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    public static final String QUEUE_NAME1 = "ming-queue1";
    public static final String QUEUE_NAME2 = "ming-queue2";

    @RabbitListener(queues = QUEUE_NAME1)
    public void getMessageFromQueue1(Message message){
        System.out.println(message);
        System.out.println(new String(message.getBody()));
    }

    @RabbitListener(queues = QUEUE_NAME2)
    public void getMessageFromQueue2(Message message){
        System.out.println(message);
        System.out.println(new String(message.getBody()));
    }
}
