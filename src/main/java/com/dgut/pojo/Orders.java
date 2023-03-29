package com.dgut.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("orders")
public class Orders {
    @TableId(value = "o_id")
    @JsonProperty("oId")
    private int oId;
    @JsonProperty("pId")
    private int pId;
    @JsonProperty("dId")
    private int dId;
    @JsonProperty("oRecord")
    private String oRecord;
    @JsonProperty("oStart")
    private String oStart;
    @JsonProperty("oEnd")
    private String oEnd;
    @JsonProperty("oState")
    private Integer oState;
    @JsonProperty("oDrug")
    private String oDrug;
    @JsonProperty("oCheck")
    private String oCheck;
    @JsonProperty("oTotalPrice")
    private Double oTotalPrice;
    @JsonProperty("oPriceState")
    private Integer oPriceState;
    @JsonProperty("countGender")
    @TableField(exist = false)
    private Integer countGender;
    @JsonProperty("oAdvice")
    private String oAdvice;
    //多表查询用
    @TableField(exist = false)//声明不是数据库里面的字段
    private Doctor doctor;
    //多表查询用
    @TableField(exist = false)//声明不是数据库里面的字段
    private Patient patient;
    @TableField(exist = false)
    private Integer countSection;
    @JsonProperty("dName")
    @TableField(exist = false)
    private String dName;
    @JsonProperty("pName")
    @TableField(exist = false)
    private String pName;


}
