package cn.sysu.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("zk_collect")
@Data
public class ZK_collect {

    private Integer id;
    private String holeCode;
    private Double x;
    private Double y;
    private Double height;
    private Double depth;
    private String waterDepth;
}
