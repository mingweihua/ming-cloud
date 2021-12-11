package cn.sysu.controller;

import cn.sysu.pojo.ZK_collect;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Slf4j
@RestController
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/test")
    public List<ZK_collect> test() {
        log.info("测试方法则行");
        String url = "http://localhost:8092/service/getAllZKData";
        List<ZK_collect> zk_collects = restTemplate.getForObject(url, List.class);
        return zk_collects;
    }

    /*
        使用DiscoveryClient，动态从Eureka服务器根据application id/name获取服务
     */

    @RequestMapping("/test2")
    public List<ZK_collect> test2() {
        log.info("动态从Eureka服务器根据application id/name获取服务");
        List<ServiceInstance> instances = discoveryClient.getInstances("service-ming");
        ServiceInstance serviceInstance = instances.get(0);
        String url = serviceInstance.getUri() + "/service/getAllZKData";
        log.info(url);
        List<ZK_collect> zk_collects = restTemplate.getForObject(url, List.class);
        return zk_collects;
    }
}
