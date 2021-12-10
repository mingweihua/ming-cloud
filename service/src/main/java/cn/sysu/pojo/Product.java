package cn.sysu.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;

@Data
//@Table(name = "product")
public class Product {

    @Id
    @KeySql(useGeneratedKeys = true)
    //@Column(name = "id")
    private Integer id;

    private String name;

    //@Column(name = "price")
    private Integer price;
}
