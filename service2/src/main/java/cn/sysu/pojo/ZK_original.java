package cn.sysu.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("zk_original")
@Data
public class ZK_original {

    private Integer id;
    private String holeCode;
    private Double x;
    private Double y;
    private Double height;
    private Double depth;
    private String waterDepth;
    private String age;
    private String level;
    private Double bottomHeight;
    private Double bottomDepth;
    private Double thickness;

}
