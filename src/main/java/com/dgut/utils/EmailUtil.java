package com.dgut.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailUtil {
    @Autowired
    private JavaMailSender javaMailSender;//邮箱

    public void sendMail(String to, Integer code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1306931167@qq.com"); //发送者
        message.setTo(to);  //接受者
        //message.setCc("hinschan1998@163.com"); //抄送，填发送者的邮箱即可
        message.setSubject("大朗医院找回密码服务");	//主题
        message.setText("尊敬的用户！你的验证码为 "+ code +" ,有效期为2分钟！");	//内容
        try {
            this.javaMailSender.send(message);
            System.out.println("简单邮件已经发送");
        } catch (Exception e) {
            System.out.println("发送简单邮件时发生异常！" + e.toString());
        }
    }

}
