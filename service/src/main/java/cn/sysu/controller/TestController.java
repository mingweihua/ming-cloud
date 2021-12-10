package cn.sysu.controller;

import cn.sysu.pojo.Product;
import cn.sysu.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @Autowired
    private ProductService productService;

    @RequestMapping("hello")
    public String test() {

        log.info("#日志# ======= 测试拦截器---log");
        return "Hello, ming";
    }

    @RequestMapping("product/{id}")
    public Product product(@PathVariable("id") int id) {
        log.info("#日志# ======= 根据id查找product，id为：" + id);
        return productService.findById(id);
    }
}
