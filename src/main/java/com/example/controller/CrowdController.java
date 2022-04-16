package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

import cn.hutool.poi.excel.ExcelWriter;
import com.example.entity.Crowd;
import com.example.service.CrowdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/crowd")
public class CrowdController {

    @Autowired
    private CrowdService crowdService;

    @PostMapping("/import")
    public String importExcel(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
//        List<User> list = reader.readAll(User.class);

        // 方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
//        Iterator<List<Object>> iter = list.iterator();
//        while (iter.hasNext()) {
//            Object s = (Object) iter.next();
//            System.out.println();
//        }
//        System.out.println(list.get(9).get(5) != null);
        List<Crowd> crowds = CollUtil.newArrayList();
        for (List<Object> row : list) {
            Crowd crowd = new Crowd();
            if(row.get(0) != null){
                crowd.setPlace(row.get(0).toString());
            }
            if(row.get(1) != null){
                crowd.setRoomNumber(row.get(1).toString());
            }
            if(row.get(2) != null){
                crowd.setName(row.get(2).toString());
            }
            if(row.get(3) != null){
                crowd.setSex(row.get(3).toString());
            }
            if(row.get(4) != null){
                crowd.setPersonnelType(row.get(4).toString());
            }
            if(row.get(5) != null){
                crowd.setHealthCodeStatus(row.get(5).toString());
            }
            if(row.get(6) != null){
                crowd.setContact(row.get(6).toString());
            }
            if(row.get(7) != null){
                crowd.setDiseaseStatus(row.get(7).toString());
            }
            if(row.get(8) != null){
                crowd.setNucleicAcidTestResults(row.get(8).toString());
            }
            if(row.get(9) != null){
                crowd.setAge(row.get(9).toString());
            }

            crowds.add(crowd);


//            crowd.setRoomNumber(row.get(1).toString());
//
//            crowd.setName(row.get(2).toString());
//
//            crowd.setSex(row.get(3).toString());
//
//            crowd.setPersonnelType(row.get(4).toString());
//
//            crowd.setHealthCodeStatus(row.get(5).toString());
//
//            crowd.setContact(row.get(6).toString());
//
//            crowd.setDiseaseStatus(row.get(7).toString());
//
//            crowd.setNucleicAcidTestResults(row.get(8).toString());
//
//            crowd.setAge(row.get(9).toString());


    }





//        for (List<Object> row : list) {
//
//                System.out.println(row.get(0).toString());
//
//                System.out.println(row.get(1).toString());
//
//                System.out.println(row.get(2).toString());
//
//                System.out.println(row.get(3).toString());
//
//                System.out.println(row.get(4).toString());
//
//                System.out.println(row.get(5).toString());
//
//                System.out.println(row.get(6).toString());
//
//                System.out.println(row.get(7).toString());
//
//                System.out.println(row.get(8).toString());
//
//                System.out.println(row.get(9).toString());
//
//
//        }

        crowdService.saveBatch(crowds);
        return "true";
    }

    @GetMapping("/test")
    public List<Crowd> index(){
        return crowdService.list();
    }

    @GetMapping("/export")
    public void push(HttpServletResponse response) throws IOException{
        List<Crowd> list = crowdService.getList();

        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.addHeaderAlias("place","隔离场所");
        writer.addHeaderAlias("roomNumber","房间号（床号）");
        writer.addHeaderAlias("name","姓名");
        writer.addHeaderAlias("sex","性别");
        writer.addHeaderAlias("personnelType","人员类型");
        writer.addHeaderAlias("healthCodeStatus","健康状态");
        writer.addHeaderAlias("contact","联系方式");
        writer.addHeaderAlias("diseaseStatus","基础疾病");
        writer.addHeaderAlias("nucleicAcidTestResults","核算检测结果");
        writer.addHeaderAlias("age","年龄");


        writer.write(list,true);
        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }
}
