package com.dgut.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "checks")
public class Checks {
    @TableId(value = "ch_id")
    @JsonProperty("chId")
    private int chId;
    @JsonProperty("chName")
    private String chName;
    @JsonProperty("chPrice")
    private Double chPrice;
}
