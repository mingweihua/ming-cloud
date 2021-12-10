package cn.sysu.controller;

import cn.sysu.pojo.ZK_collect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Slf4j
@RestController
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/test")
    public List<ZK_collect> test() {
        log.info("测试方法则行");
        String url = "http://localhost:8091/service/getAllZKData";
        List<ZK_collect> zk_collects = restTemplate.getForObject(url, List.class);
        return zk_collects;
    }


}
