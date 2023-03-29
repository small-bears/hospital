package com.dgut.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData implements Serializable {
    /**
     * 表示当前相应的状态是成功或者失败
     */
    private int status;
    /**
     * 表示当响应之后给前端的提示信息
     */
    private String msg;
    /**
     * 表示当响应成功之后返回给前端的数据
     */
    private Object data;


    public static ResponseData success(String msg, Object data) {
        return new ResponseData(200, msg, data);
    }

    public static ResponseData fail(String msg) {
        return new ResponseData(400, msg, null);
    }

    public static ResponseData success(String msg) {
        return new ResponseData(200, msg, null);

    }

}
