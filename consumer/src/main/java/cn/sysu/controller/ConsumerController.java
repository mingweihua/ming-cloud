package cn.sysu.controller;

import cn.sysu.pojo.ZK_collect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Slf4j
@RestController
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    //用于动态拉取相应服务的地址
    @Autowired
    private DiscoveryClient discoveryClient;

    //添加@LoadBalanced，使用了ribbon负载均衡
    @Autowired
    private RestTemplate restTemplate2;

    @RequestMapping("/test")
    public List<ZK_collect> test() {
        log.info("测试方法则行");
        String url = "http://localhost:8021/service/getAllZKData";
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

    /*
        使用Ribbon负载均衡，前提是在configuration里面的restTemplate添加注解@LoadBalanced

        注意：使用该形式的负载均衡后，上面两个方法将无法执行，因为是根据application name进行查询服务的，因此要把两个restTemplate分开
        或者不要把注解放在restTemplate
     */
    @RequestMapping("/test3")
    public List<ZK_collect> test3() {
        log.info("使用负载均衡动态从Eureka服务器获取服务");
        String url = "http://service-ming/service/getAllZKData";
        log.info("此时IP和端口换成服务名/服务id");
        List<ZK_collect> zk_collects = restTemplate2.getForObject(url, List.class);
        return zk_collects;
    }
}
