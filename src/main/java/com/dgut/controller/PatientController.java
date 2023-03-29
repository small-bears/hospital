package com.dgut.controller;

import com.dgut.mapper.OrderMapper;
import com.dgut.pojo.Orders;
import com.dgut.pojo.Patient;
import com.dgut.service.DoctorService;
import com.dgut.service.OrderService;
import com.dgut.service.PatientService;
import com.dgut.utils.JwtUtil;
import com.dgut.utils.PdfUtil;
import com.dgut.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("patient")
public class PatientController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private JavaMailSender javaMailSender;//邮箱
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 登录数据验证
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData login(@RequestParam(value = "pId") int pId, @RequestParam(value = "pPassword") String pPassword) {
        Patient patient = this.patientService.login(pId, pPassword);
        if (patient != null) {
            Map<String,String> map = new HashMap<>();
            map.put("pName", patient.getPName());
            map.put("pId", String.valueOf(patient.getPId()));
            map.put("pCard", patient.getPCard());
            String token = JwtUtil.getToken(map);
            map.put("token", token);
            //response.setHeader("token", token);
            return ResponseData.success("登录成功", map);
        } else {
            return ResponseData.fail("登录失败，密码或账号错误");
        }
    }
    /**
     * 根据科室查询所有医生信息
     */
    @RequestMapping("findDoctorBySection")
    public ResponseData findDoctorBySection(@RequestParam(value = "dSection") String dSection){
        return ResponseData.success("根据科室查询所有医生信息成功", this.doctorService.findDoctorBySection(dSection));
    }
    /**
     * 增加挂号信息
     */
    @RequestMapping("addOrder")
    @ResponseBody
    public ResponseData addOrder(Orders order, String arId){
        System.out.println(arId);
        if (this.orderService.addOrder(order, arId))
        return ResponseData.success("插入挂号信息成功");
        return ResponseData.fail("插入挂号信息失败");
    }
    /**
     * 根据pId查询挂号
     */
    @RequestMapping("findOrderByPid")
    public ResponseData findOrderByPid(@RequestParam(value = "pId") int pId){
        return ResponseData.success("返回挂号信息成功", this.orderService.findOrderByPid(pId)) ;
    }

    /**
     * 发送邮件
     */
    @RequestMapping("sendEmail")
    public ResponseData sendEmail(String pEmail){
        if (this.patientService.sendEmail(pEmail))
            return ResponseData.success("邮件发送成功！");
        return ResponseData.fail("邮箱号未注册！");
    }
    /**
     * 找回密码服务邮件和验证码校验
     */
    @RequestMapping("findPassword")
    public ResponseData findPassword(String pEmail, String pPassword, String code){
        if(this.patientService.findPassword(pEmail, pPassword, code))
            return ResponseData.success("密码修改成功");
        return ResponseData.fail("密码修改失败!验证码不正确或者已过期");
    }
    /**
     * 增加患者信息
     */
    @RequestMapping("addPatient")
    @ResponseBody
    public ResponseData addPatient(Patient patient) {
        Boolean bo = this.patientService.addPatient(patient);
        if (bo) {
            return ResponseData.success("注册成功");
        }
        return ResponseData.fail("注册失败！账号或邮箱已被占用");
    }
    @GetMapping("/pdf")
    public void downloadPDF(HttpServletRequest request, HttpServletResponse response, int oId) throws Exception {
        Orders order = this.orderMapper.findOrderByOid(oId);
        PdfUtil.ExportPdf(request, response, order);
    }
    /**
     * 统计患者男女人数
     */
    @RequestMapping("patientAge")
    public ResponseData patientAge(){
        return  ResponseData.success("统计患者男女人数成功", this.patientService.patientAge());

    }
}
