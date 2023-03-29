package com.dgut.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dgut.mapper.PatientMapper;
import com.dgut.pojo.Patient;
import com.dgut.service.PatientService;
import com.dgut.utils.Md5Util;
import com.dgut.utils.RandomUtil;
import com.dgut.utils.TodayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("PatientService")
@Slf4j
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private JavaMailSender javaMailSender;//邮箱
    @Autowired
    private JedisPool jedisPool;//redis连接池


    /**
     * 登录数据校验
     * */
    @Override
    public Patient login(int pId, String pPassword){
        Patient patient = this.patientMapper.selectById(pId);
        String password = Md5Util.getMD5(pPassword);
        if (patient == null) {
            return null;
        } else {
            if ((patient.getPPassword()).equals(password)) {
                return patient;
            }
        }
        return null;
    }
    /**
     * 分页模糊查询所有患者信息
     */
    @Override
    public HashMap<String, Object> findAllPatients(int pageNumber, int size, String query) {
        Page<Patient> page = new Page<>(pageNumber, size);
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.like("p_name", query).orderByDesc("p_state");
        IPage<Patient> iPage = this.patientMapper.selectPage(page, wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //总条数
        hashMap.put("pages", iPage.getPages());       //总页数
        hashMap.put("pageNumber", iPage.getCurrent());//当前页
        hashMap.put("patients", iPage.getRecords()); //查询到的记录
        return hashMap;
    }

    /**
     * 删除患者信息
     */
    @Override
    public Boolean deletePatient(int pId) {
        Patient patient = new Patient();
        patient.setPId(pId);
        patient.setPState(0);
        this.patientMapper.updateById(patient);
        return true;
    }
    /**
     * 根据患者id查询患者信息
     */
    @Override
    public Patient findPatientById(int pId){
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq("p_id", pId);
        return this.patientMapper.selectOne(wrapper);
    }

    /**
     * 发送邮件
     */
    @Override
    public Boolean sendEmail(String pEmail){

        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq("p_email", pEmail);
        Patient patient = this.patientMapper.selectOne(wrapper);
        if (patient == null){
            return false;
        }else {
            Integer code = RandomUtil.randomCode();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("1306931167@qq.com"); //发送者
            message.setTo(pEmail);  //接受者
            //message.setCc("hinschan1998@163.com"); //抄送，填发送者的邮箱即可
            message.setSubject("大朗医院找回密码服务");	//主题
            message.setText("尊敬的用户！您的账号为：" + patient.getPId() + "  验证码为 "+ code +" ,有效期为2分钟！");	//内容
            try {
                javaMailSender.send(message);
                Jedis jedis = jedisPool.getResource();
                jedis.set(pEmail, String.valueOf(code));
                jedis.expire(pEmail, 120);
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
    public Boolean findPassword(String pEmail, String pPassword, String code){
        UpdateWrapper<Patient> wrapper = new UpdateWrapper<>();
        String password = Md5Util.getMD5(pPassword);
        Jedis jedis = jedisPool.getResource();
        //System.out.println("============="+jedis.get(pEmail));
        if (jedis.get(pEmail) == null)
            return false;
        if (jedis.get(pEmail).equals(code)){
            wrapper.set("p_password", password).eq("p_email", pEmail);
            this.patientMapper.update(null, wrapper);
            return true;
        }
        return false;

    }
    /**
     * 增加患者信息
     */
    @Override
    public Boolean addPatient(Patient patient) {
        //如果账号已存在则返回false
        List<Patient> patients = this.patientMapper.selectList(null);
        for (Patient patient1 : patients) {
            if (patient.getPId() == patient1.getPId()) {
                return false;
            }
            if ((patient.getPEmail()).equals(patient1.getPEmail()) ){
                return false;
            }
        }
        int yourYear = Integer.parseInt(patient.getPBirthday().substring(0, 4));
        int todayYear = Integer.parseInt(TodayUtil.getTodayYmd().substring(0,4));
        //密码md5加密
        String password = Md5Util.getMD5(patient.getPPassword());
        patient.setPPassword(password);
        patient.setPAge(todayYear-yourYear);
        patient.setPState(1);
        this.patientMapper.insert(patient);
        return true;
    }
    /**
     * 统计患者男女人数
     */
    public List<Integer> patientAge(){
        List<Integer> ageList = new ArrayList<>();
        Integer age1 = this.patientMapper.patientAge(0, 9);
        Integer age2 = this.patientMapper.patientAge(10, 19);
        Integer age3 = this.patientMapper.patientAge(20, 29);
        Integer age4 = this.patientMapper.patientAge(30, 39);
        Integer age5 = this.patientMapper.patientAge(40, 49);
        Integer age6 = this.patientMapper.patientAge(50, 59);
        Integer age7 = this.patientMapper.patientAge(60, 69);
        Integer age8 = this.patientMapper.patientAge(70, 79);
        Integer age9 = this.patientMapper.patientAge(80, 89);
        Integer age10 = this.patientMapper.patientAge(90, 99);
        ageList.add(age1);
        ageList.add(age2);
        ageList.add(age3);
        ageList.add(age4);
        ageList.add(age5);
        ageList.add(age6);
        ageList.add(age7);
        ageList.add(age8);
        ageList.add(age9);
        ageList.add(age10);
        return ageList;

    }


    }


