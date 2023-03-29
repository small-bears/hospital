package com.dgut.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dgut.mapper.AdminMapper;
import com.dgut.pojo.Admin;
import com.dgut.service.AdminService;
import com.dgut.utils.Md5Util;
import com.dgut.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Service("AdminService")
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private JavaMailSender javaMailSender;//邮箱
    @Autowired
    private JedisPool jedisPool;//redis连接池
    /**
     * 登录数据校验
     * */
    @Override
    public Admin login(int aId, String aPassword){
        Admin admin = this.adminMapper.selectById(aId);
        if (admin == null) {
            return null;
        } else {
            if ((admin.getAPassword()).equals(aPassword)) {
                return admin;
            }
        }
        return null;
    }

    /**
     * 发送邮件
     */
    @Override
    public Boolean sendEmail(String aEmail){

        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("a_email", aEmail);
        Admin admin = this.adminMapper.selectOne(wrapper);
        if (admin == null){
            return false;
        }else {
            Integer code = RandomUtil.randomCode();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("1306931167@qq.com"); //发送者
            message.setTo(aEmail);  //接受者
            //message.setCc("hinschan1998@163.com"); //抄送，填发送者的邮箱即可
            message.setSubject("大朗医院找回密码服务");	//主题
            message.setText("尊敬的管理员！您的账号为：" + admin.getAId() + "  验证码为 "+ code +" ,有效期为2分钟！");	//内容
            try {
                javaMailSender.send(message);
                Jedis jedis = jedisPool.getResource();
                jedis.set(aEmail, String.valueOf(code));
                jedis.expire(aEmail, 120);
                System.out.println("简单邮件已经发送");
            } catch (Exception e) {
                System.out.println("发送简单邮件时发生异常！" + e.toString());
                return false;
            }
        }
        return true;
    }
    /**
     * 找回密码服务
     */
    @Override
    public Boolean findPassword(String aEmail, String aPassword, String code){
        UpdateWrapper<Admin> wrapper = new UpdateWrapper<>();
        String password = Md5Util.getMD5(aPassword);
        Jedis jedis = jedisPool.getResource();
        //System.out.println("============="+jedis.get(pEmail));
        if (jedis.get(aEmail) == null)
            return false;
        if (jedis.get(aEmail).equals(code)){
            wrapper.set("a_password", password).eq("a_email", aEmail);
            this.adminMapper.update(null, wrapper);
            return true;
        }
        return false;

    }



}
