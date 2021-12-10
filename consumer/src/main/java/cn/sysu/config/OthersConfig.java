package cn.sysu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OthersConfig {

    @Bean
    //负载均衡方式2
    //@LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
