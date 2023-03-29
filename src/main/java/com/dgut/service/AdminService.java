package com.dgut.service;

import com.dgut.pojo.Admin;

public interface AdminService {
    /**
     * 登录数据校验
     * */
    Admin login(int aId, String aPassword);
    /**
     * 找回密码服务
     */
    Boolean findPassword(String aEmail, String aPassword, String code);
    /**
     * 发送邮件
     */
    Boolean sendEmail(String aEmail);
}
