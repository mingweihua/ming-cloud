package cn.sysu.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ReturnPattern {
    private String msg;
    private List<ZK_collect> data;
}
