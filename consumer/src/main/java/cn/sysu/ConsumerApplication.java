package cn.sysu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*
    开启hystrix，本来是可以EnableHystrix，由于后续需要使用到服务熔断，所以使用EnableCircuitBreaker
    以下三个注解其实可以直接使用SpringCloudApplication
 */
/*@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication*/
@EnableFeignClients
@SpringCloudApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class,args);
    }
}
