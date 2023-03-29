package com.dgut.controller;

import com.dgut.pojo.Doctor;
import com.dgut.service.DoctorService;
import com.dgut.service.OrderService;
import com.dgut.service.PatientService;
import com.dgut.utils.JwtUtil;
import com.dgut.utils.ResponseData;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PatientService patientService;
    /**
     * 登录数据验证
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData login(@RequestParam(value = "dId") int dId, @RequestParam(value = "dPassword") String dPassword) {
        Doctor doctor = this.doctorService.login(dId, dPassword);
        if (doctor != null) {
            Map<String,String> map = new HashMap<>();
            map.put("dName", doctor.getdName());
            map.put("dId", String.valueOf(doctor.getdId()));
            String token = JwtUtil.getToken(map);
            map.put("token", token);
            //response.setHeader("token", token);
            return ResponseData.success("登录成功", map);
        } else {
            return ResponseData.fail("登录失败，密码或账号错误");
        }
    }
    /**
     * 查看当天挂号列表
     */
    @RequestMapping("findOrderByNull")
    public ResponseData findOrderByNull(@Param(value = "dId") int dId, @RequestParam(value = "oStart") String oStart){
        System.out.println("账号时间为"+dId+oStart);
        return ResponseData.success("返回当天挂号信息成功", this.orderService.findOrderByNull(dId,oStart));

    }
    /**
     * 根据患者id查询患者信息
     */
    @RequestMapping("findPatientById")
    public ResponseData findPatientById(int pId){
        return ResponseData.success("返回患者信息成功！", this.patientService.findPatientById(pId));
    }
    /**
     * 发送邮件
     */
    @RequestMapping("sendEmail")
    public ResponseData sendEmail(String dEmail){
        if (this.doctorService.sendEmail(dEmail))
            return ResponseData.success("邮件发送成功！");
        return ResponseData.fail("邮箱号未注册！");
    }
    /**
     * 找回密码服务邮件和验证码校验
     */
    @RequestMapping("findPassword")
    public ResponseData findPassword(String dEmail, String dPassword, String code){
        if(this.doctorService.findPassword(dEmail, dPassword, code))
            return ResponseData.success("密码修改成功");
        return ResponseData.fail("密码修改失败!验证码不正确或者已过期");
    }
    /**
     * 分页根据科室查询所有医生信息
     */
    @RequestMapping("findDoctorBySectionPage")
    public ResponseData findDoctorBySectionPage(int pageNumber, int size, String query, String dSection){
        return ResponseData.success("分页根据科室查询所有医生信息成功", this.doctorService.findDoctorBySectionPage(pageNumber, size, query, dSection));
    }
    /**
     * 用户评价
     */
    @RequestMapping("updateStar")
    public ResponseData updateStar(int dId, Double dStar){
        if(this.doctorService.updateStar(dId, dStar))
            return ResponseData.success("评价成功");
        return ResponseData.fail("评价失败");
    }
    /**
     * 上传Excel导入数据
     */
    @RequestMapping(value = "uploadExcel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData uploadExcel(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        if (this.doctorService.uploadExcel(multipartFile))
        return ResponseData.success("上传Excel导入数据成功");
        return ResponseData.fail("上传Excel导入数据失败");

    }
    /**
     * Excel导出数据
     */
    @RequestMapping("downloadExcel")
    public ResponseData downloadExcel(HttpServletResponse response) throws IOException {
        if (this.doctorService.downloadExcel(response))
        return ResponseData.success("Excel导出数据成功");
        return ResponseData.fail("Excel导出数据失败");
    }
}
