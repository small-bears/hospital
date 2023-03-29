package com.dgut.service.serviceImpl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dgut.mapper.DoctorMapper;
import com.dgut.pojo.Doctor;
import com.dgut.service.DoctorService;
import com.dgut.utils.Md5Util;
import com.dgut.utils.RandomUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

@Service("DoctorService")
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private JavaMailSender javaMailSender;//邮箱
    @Autowired
    private JedisPool jedisPool;//redis连接池
    /**
     * 登录数据校验
     * */
    @Override
    public Doctor login(int dId, String dPassword){
        Doctor doctor = this.doctorMapper.selectById(dId);
        String password = Md5Util.getMD5(dPassword);
        if (doctor == null) {
            return null;
        } else {
            if ((doctor.getdPassword()).equals(password)) {
                return doctor;
            }
        }
        return null;
    }
    /**
     * 分页模糊查询所有医护人员信息
     */
    @Override
    public HashMap<String, Object> findAllDoctors(int pageNumber, int size, String query) {
        Page<Doctor> page = new Page<>(pageNumber, size);
        QueryWrapper<Doctor> wrapper = new QueryWrapper<>();
        wrapper.like("d_name", query).orderByDesc("d_state");
        IPage<Doctor> iPage = this.doctorMapper.selectPage(page, wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //总条数
        hashMap.put("pages", iPage.getPages());       //总页数
        hashMap.put("pageNumber", iPage.getCurrent());//当前页
        hashMap.put("doctors", iPage.getRecords()); //查询到的记录
        return hashMap;
    }

    /**
     * 根据id查找医生
     */
    @Override
    public Doctor findDoctor(int dId) {
        return this.doctorMapper.selectById(dId);
    }

    /**
     * 增加医生信息
     */
    @Override
    public Boolean addDoctor(Doctor doctor) {
        //如果账号已存在则返回false
        List<Doctor> doctors = this.doctorMapper.selectList(null);
        for (Doctor doctor1 : doctors) {
            if (doctor.getdId() == doctor1.getdId()) {
                return false;
            }
        }
        //密码加密
        String password = Md5Util.getMD5(doctor.getdPassword());
        doctor.setdPassword(password);
        doctor.setdState(1);
        doctor.setdStar(0.00);
        doctor.setdPeople(0);
        this.doctorMapper.insert(doctor);
        return true;
    }

    /**
     * 删除医生信息
     */
    @Override
    public Boolean deleteDoctor(int dId) {
        Doctor doctor = new Doctor();
        doctor.setdId(dId);
        doctor.setdState(0);
        this.doctorMapper.updateById(doctor);
        return true;
    }

    /**
     * 修改医生信息
     */
    @Override
    public Boolean modifyDoctor(Doctor doctor) {
//        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("d_id", doctor.getDId());
//        this.doctorMapper.update(doctor, queryWrapper);
        int i = this.doctorMapper.updateById(doctor);
        System.out.println("影响行数："+i);
        return true;
    }
    /**
     * 根据科室查询所有医生信息
     */
    @Override
    public HashMap<String, Object> findDoctorBySection(String dSection){
//        HashMap<String, Object> hashMap = new HashMap<>();
//        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("d_section", dSection).eq("d_state", 1);
//        List<Doctor> doctors = this.doctorMapper.selectList(queryWrapper);
        HashMap<String, Object> map = new HashMap<>();
        map.put("doctors", this.doctorMapper.findDoctorBySection(dSection));
        return map;

    }
    /**
     * 分页根据科室查询所有医生信息
     */
    @Override
    public HashMap<String, Object> findDoctorBySectionPage(int pageNumber, int size, String query, String dSection) {
        Page<Doctor> page = new Page<>(pageNumber, size);
        QueryWrapper<Doctor> wrapper = new QueryWrapper<>();
        wrapper.select("d_id", "d_name", "d_gender", "d_post", "d_section").like("d_name", query).eq("d_section", dSection).orderByDesc("d_state");
        IPage<Doctor> iPage = this.doctorMapper.selectPage(page, wrapper);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", iPage.getTotal());       //总条数
        hashMap.put("pages", iPage.getPages());       //总页数
        hashMap.put("pageNumber", iPage.getCurrent());//当前页
        hashMap.put("doctors", iPage.getRecords()); //查询到的记录
        return hashMap;
    }


    /**
     * 发送邮件
     */
    @Override
    public Boolean sendEmail(String dEmail){

        QueryWrapper<Doctor> wrapper = new QueryWrapper<>();
        wrapper.eq("d_email", dEmail);
        Doctor doctor = this.doctorMapper.selectOne(wrapper);
        if (doctor == null){
            return false;
        }else {
            Integer code = RandomUtil.randomCode();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("1306931167@qq.com"); //发送者
            message.setTo(dEmail);  //接受者
            //message.setCc("hinschan1998@163.com"); //抄送，填发送者的邮箱即可
            message.setSubject("大朗医院找回密码服务");	//主题
            message.setText("尊敬的医生！您的账号为：" + doctor.getdId() + "  验证码为 "+ code +" ,有效期为2分钟！");	//内容
            try {
                javaMailSender.send(message);
                Jedis jedis = jedisPool.getResource();
                jedis.set(dEmail, String.valueOf(code));
                jedis.expire(dEmail, 120);
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
    public Boolean findPassword(String dEmail, String dPassword, String code){
        UpdateWrapper<Doctor> wrapper = new UpdateWrapper<>();
        String password = Md5Util.getMD5(dPassword);
        Jedis jedis = jedisPool.getResource();
        //System.out.println("============="+jedis.get(pEmail));
        if (jedis.get(dEmail) == null)
            return false;
        if (jedis.get(dEmail).equals(code)){
            wrapper.set("d_password", password).eq("d_email", dEmail);
            this.doctorMapper.update(null, wrapper);
            return true;
        }
        return false;
    }
    /**
     * 用户评价
     */
    @Override
    public Boolean updateStar(int dId, Double dStar){

        if(this.doctorMapper.updateStar(dId, dStar))
            return true;
        return false;
    }
    /**
     * 上传Excel导入数据
     */
    @Override
    public Boolean uploadExcel(MultipartFile multipartFile) throws Exception {
        ImportParams params = new ImportParams();
        params.setHeadRows(1);
       List<Doctor> doctors = ExcelImportUtil.importExcel(multipartFile.getInputStream(), Doctor.class, params);
        for (Doctor doctor: doctors){
            doctor.setdPassword(Md5Util.getMD5(doctor.getdPassword()));
            this.addDoctor(doctor);
        }
        return true;
    }
    /**
     * Excel导出数据
     */
    @Override
    public Boolean downloadExcel(HttpServletResponse response) throws IOException {
        List<Doctor> doctors = this.findAll();
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), Doctor.class, doctors);
        ServletOutputStream stream = response.getOutputStream();
        response.setHeader("content-disposition", "attachment;fileName="+ URLEncoder.encode("医院医生信息.xlsx", "UTF-8"));
        workbook.write(stream);
        stream.close();
        workbook.close();
        return true;
    }
    /**
     * 查询所有医生不分页
     */
    @Override
    public List<Doctor> findAll(){
        return this.doctorMapper.selectList(null);
    }
}
