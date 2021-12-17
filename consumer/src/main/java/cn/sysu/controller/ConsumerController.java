package cn.sysu.controller;

import cn.sysu.client.MyClient;
import cn.sysu.pojo.ReturnPattern;
import cn.sysu.pojo.ZK_collect;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
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

    @Autowired(required = false)
    private MyClient myClient;

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


    /*
    当程序启动时，会进行包扫描，扫描所有 @FeignClient 的注解的类，并将这些信息注入 Spring IOC 容器中。
    当定义的 Fegin 接口中的方法被调用时，通过 JDK代理 的方式，来生成具体的 RequestTemplate。
    当生成代理时，Fegin 会为每个接口方法创建一个 RequestTemplate对象，该对象封装了HTTP请求需要的全部信息。
然后有 RequestTemplate 生成 Request，然后把 Request 交给 Client 去处理，这里指的 Client 可以是 JDK 原生的 URLConnection、
Apache 的 Http Client 也可以是 OKhttp。 最后 Client 被封装到 LoadBalanceclient 类，这各类结合 Ribbon 负载均衡发起服务之间的调用

     */

    /*
      注意：Feign的结合hystrix熔断不像以前那样，默认不开启，先配置文件中开启，后续再接口上配置一个类，该类必须实现该接口

        写起来还是比较麻烦
     */

    @RequestMapping("/test6")
    public ReturnPattern test6() {
        log.info("使用Fegin");

        List<ZK_collect> zk_collects = myClient.getAllZK_collect();
        ReturnPattern returnPattern = new ReturnPattern();
        returnPattern.setMsg("success");
        returnPattern.setData(zk_collects);
        return returnPattern;
    }
}
