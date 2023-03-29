package com.dgut.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("bed")
public class Bed {
    @TableId(value = "b_id")
    @JsonProperty("bId")
    private int bId;
    @JsonProperty("pId")
    private int pId;
    @JsonProperty("dId")
    private int dId;
    @JsonProperty("bState")
    private Integer bState;
    @JsonProperty("bStart")
    private String bStart;
    @JsonProperty("bReason")
    private String bReason;
    @Version
    private Integer version;


}
