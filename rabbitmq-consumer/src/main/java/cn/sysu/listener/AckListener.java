package cn.sysu.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class AckListener {

    public static final String QUEUE_NAME1 = "ming-queue1";

    @RabbitListener(queues = QUEUE_NAME1)
    public void getMessageFromQueue1(Channel channel, Message message) throws IOException {
        String msg = new String(message.getBody());
        try{
            //如果消息处理失败
            //int num = 1/0;
            System.out.println("消息处理成功：" + msg);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息
            // 这里表示该消息已经被消费了 可以在队列删掉 这样以后就不会再发了
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e) {
            System.out.println("消息处理失败：" + msg);
            // deliveryTag:该消息的index
            // multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
            // requeue：被拒绝的是否重新入队列
            // 这里表示该消息没有被成功消费，并且将该消息重新入队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,true);
        }

    }

}
