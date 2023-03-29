package com.dgut.controller;

import com.dgut.pojo.Admin;
import com.dgut.pojo.Doctor;
import com.dgut.service.AdminService;
import com.dgut.service.DoctorService;
import com.dgut.service.OrderService;
import com.dgut.service.PatientService;
import com.dgut.utils.JwtUtil;
import com.dgut.utils.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private OrderService orderService;

    /**
     * 登录数据验证
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseData login(@RequestParam("aId") int aId, @RequestParam("aPassword") String aPassword) {
        Admin admin = this.adminService.login(aId, aPassword);
        if (admin != null) {
            Map<String,String> map = new HashMap<>();
            map.put("aName", admin.getAName());
            map.put("aId", String.valueOf(admin.getAId()));
            String token = JwtUtil.getToken(map);
            map.put("token", token);
            return ResponseData.success("登录成功", map);
        } else {
            return ResponseData.fail("登录失败，密码或账号错误");
        }
    }

    /**
     * 分页模糊查询所有医护人员信息
     */
    @RequestMapping("findAllDoctors")
    public ResponseData findAllDoctors(@RequestParam(value = "pageNumber") int pageNumber, @RequestParam(value = "size") int size, @RequestParam(value = "query") String query){
        return ResponseData.success("返回医护人员信息成功",  this.doctorService.findAllDoctors(pageNumber, size, query));
    }
    /**
     * 根据id查找医生
     */
    @RequestMapping("findDoctor")
    public ResponseData findDoctor(@RequestParam(value = "dId") int dId) {
        return ResponseData.success("查询医生成功", this.doctorService.findDoctor(dId));
    }
    /**
     * 增加医生信息
     */
    @RequestMapping("addDoctor")
    @ResponseBody
    public ResponseData addDoctor(Doctor doctor) {
        Boolean bo = this.doctorService.addDoctor(doctor);
        if (bo) {
            return ResponseData.success("增加医生信息成功");
        }
        return ResponseData.fail("增加医生信息失败！账号或已被占用");
    }
    /**
     * 删除医生信息
     */
    @RequestMapping("deleteDoctor")
    public ResponseData deleteDoctor(@RequestParam(value = "dId") int dId) {
        Boolean bo = this.doctorService.deleteDoctor(dId);
        if (bo){
            return ResponseData.success("删除医生信息成功");
        }
        return ResponseData.fail("删除医生信息失败");
    }
    /**
     * 修改医生信息
     * bug: dState会自动更新为0
     */
    @RequestMapping("modifyDoctor")
    @ResponseBody
    public ResponseData modifyDoctor(Doctor doctor) {
        this.doctorService.modifyDoctor(doctor);
        return ResponseData.success("修改医生信息成功");
    }
    /**
     * 分页模糊查询所有患者信息
     */
    @RequestMapping("findAllPatients")
    public ResponseData findAllPatients(@RequestParam(value = "pageNumber") int pageNumber, @RequestParam(value = "size") int size, @RequestParam(value = "query") String query){
        return ResponseData.success("返回患者信息成功",  this.patientService.findAllPatients(pageNumber, size, query));
    }
    /**
     * 删除患者信息
     */
    @RequestMapping("deletePatient")
    public ResponseData deletePatient(@RequestParam(value = "pId") int pId) {
        Boolean bo = this.patientService.deletePatient(pId);
        if (bo){
            return ResponseData.success("删除患者信息成功");
        }
        return ResponseData.fail("删除患者信息失败");
    }
    /**
     * 分页模糊查询所有挂号信息
     */
    @RequestMapping("findAllOrders")
    public ResponseData findAllOrders(@RequestParam(value = "pageNumber") int pageNumber, @RequestParam(value = "size") int size, @RequestParam(value = "query") String query){
        return ResponseData.success("返回挂号信息成功",  this.orderService.findAllOrders(pageNumber, size, query));
    }
    /**
     * 删除挂号信息
     */
    @RequestMapping("deleteOrder")
    public ResponseData deleteOrder(@RequestParam(value = "oId") int oId) {
        Boolean bo = this.orderService.deleteOrder(oId);
        if (bo){
            return ResponseData.success("删除挂号信息成功");
        }
        return ResponseData.fail("删除挂号信息失败");
    }
    /**
     * 发送邮件
     */
    @RequestMapping("sendEmail")
    public ResponseData sendEmail(String aEmail){
        if (this.adminService.sendEmail(aEmail))
            return ResponseData.success("邮件发送成功！");
        return ResponseData.fail("邮箱号未注册！");
    }
    /**
     * 找回密码服务邮件和验证码校验
     */
    @RequestMapping("findPassword")
    public ResponseData findPassword(String aEmail, String aPassword, String code){
        if(this.adminService.findPassword(aEmail, aPassword, code))
            return ResponseData.success("密码修改成功");
        return ResponseData.fail("密码修改失败!验证码不正确或者已过期");
    }


}
