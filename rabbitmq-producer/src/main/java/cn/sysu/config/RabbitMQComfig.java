package cn.sysu.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQComfig {

    public static final String EXCHANGE_NAME = "ming-topic-exchange";
    public static final String QUEUE_NAME1 = "ming-queue1";
    public static final String QUEUE_NAME2 = "ming-queue2";

    //1.创建交换机
    @Bean("topic-exchange")
    public Exchange createExchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }

    //2.创建交队列
    @Bean("queue1")
    public Queue createQueue1(){
        return QueueBuilder.durable("QUEUE_NAME1").build();
    }
    @Bean("queue2")
    public Queue createQueue2(){
        return QueueBuilder.durable("QUEUE_NAME2").build();
    }

    //3.队列与交换机的关系 Binding


}
