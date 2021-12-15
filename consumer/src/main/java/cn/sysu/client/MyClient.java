package cn.sysu.client;

import cn.sysu.pojo.ZK_collect;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("service-ming")
public interface MyClient {

    @RequestMapping("service/getAllZKData")
    List<ZK_collect> getAllZK_collect();
}
