package cn.sysu.controller;

import cn.sysu.pojo.ZK_collect;
import cn.sysu.service.ZKDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class ServiceController {

    @Autowired
    private ZKDataService zkDataService;

    @RequestMapping("getAllZKData")
    public List<ZK_collect> getAllZKData() {
        log.info("#日志# ======= 查找所有钻孔统计数据");
        return zkDataService.getAllCollectData();
    }
}
