package cn.sysu.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/*
    0   用于描述地表高度， 厚度为0，
    1	Q4ml	第四系全新统人工堆积层
    2	Q4h	第四纪人工堆积
    3	Q4dl	坡积层
    4	Qel	残积层
    5	J1	早侏罗纪，下侏罗统
    6	J2-1	早侏罗纪，下侏罗统
    7	J2-2	早侏罗纪，下侏罗统
    8	J3	早侏罗纪，下侏罗统
    9	J4	早侏罗纪，下侏罗统


    最后使用bottomHeight。层底高度进行克里金插值方法进行处理
 */


@TableName("zk_change")
@Data
public class ZK_change {

    private Integer id;
    private String holeCode;
    private Double x;
    private Double y;
    private Double height;
    private String waterHeight;
    private Integer levelNum;
    private Double bottomHeight;
    private Double thickness;

}
