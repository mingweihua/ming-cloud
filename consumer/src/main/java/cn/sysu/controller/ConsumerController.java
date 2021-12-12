package cn.sysu.controller;

import cn.sysu.pojo.ReturnPattern;
import cn.sysu.pojo.ZK_collect;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
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
@DefaultProperties(defaultFallback = "defaultFallbackMethod")
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

    /*
        使用hystrix来提供线程隔离、服务降级和服务熔断等之类的

        注意：hystrix跳转的方法，必须有相同的返回类型，相同的输入参数类型
        但若是使用DefaultProperties，也就是所有出问题函数都走，因此输入参数就不需要一样了

        默认时间是1s，因此可以让线程睡眠2000ms来测试
     */
    @RequestMapping("/test4")
    //@HystrixCommand(fallbackMethod = "hystrixFallbackMethod")
    @HystrixCommand
    public ReturnPattern test4() {
        log.info("hystrix测试方法进入");

        String url = "http://service-ming/service/getAllZKData";
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<ZK_collect> zk_collects = restTemplate2.getForObject(url, List.class);
        ReturnPattern returnPattern = new ReturnPattern();
        returnPattern.setMsg("success");
        returnPattern.setData(zk_collects);
        return returnPattern;
    }

    /*
        hystrix  配置  HystrixCommandProperties.java
            找到要配置的属性，ctrl+alt+F7,查看相应的name

        hystrix超时时间配置

     */
    @RequestMapping("/test5")
    @HystrixCommand(fallbackMethod = "hystrixFallbackMethod",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "2500")
    })
    public ReturnPattern test5() {
        log.info("hystrix测试方法进入");

        String url = "http://service-ming/service/getAllZKData";
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<ZK_collect> zk_collects = restTemplate2.getForObject(url, List.class);
        ReturnPattern returnPattern = new ReturnPattern();
        returnPattern.setMsg("success");
        returnPattern.setData(zk_collects);
        return returnPattern;
    }


    public ReturnPattern hystrixFallbackMethod() {
        log.info("hystrixFallbackMethod 方法进入");
        ReturnPattern returnPattern = new ReturnPattern();
        returnPattern.setMsg("error：hystrixFallbackMethod");
        return returnPattern;
    }

    public ReturnPattern defaultFallbackMethod() {
        log.info("defaultFallbackMethod 方法进入");
        ReturnPattern returnPattern = new ReturnPattern();
        returnPattern.setMsg("error：defaultFallbackMethod");
        return returnPattern;
    }
}
