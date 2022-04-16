package com.example.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.config.Result;
import com.example.entity.Library;
import com.example.entity.User;
import com.example.service.LibraryService;
import com.example.service.MailService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

//    @Autowired
//    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private LibraryService libraryService;

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        String username = user.getUserName();
        String password = user.getUserPassword();
        if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            return new Result(false,user);
        }
        return userService.login(user);
    }

    @PostMapping
    public boolean save(@RequestBody User user){
        return userService.saveUser(user);
    }

//    @PostMapping("/ser")
//    public Integer insert(@RequestBody User user){
//        return userMapper.insert(user);
//    }

    @GetMapping
    public List<User> index(){
        return userService.list();
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
        return userService.removeById(id);
    }


    //分页mybatispuls
    @GetMapping("/page")
    public IPage<User> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String username){
        IPage<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_name",username);
        return userService.page(page,queryWrapper);
    }

    @PostMapping("/sendMail")
    public boolean sendMail(@RequestBody String email, HttpSession session){
        boolean text = mailService.sendMimeMail(email,session);
        if( text){
            Library temp = new Library((String) session.getAttribute("code"));
            libraryService.saveOrUpdate(temp);

            return true;
        }else{
            return false;
        }

    }

    @PostMapping("/register")
    public boolean register(@RequestBody Library library){
//        String username = user.getUserName();
//        String password = user.getUserPassword();
        String codevalue = library.getCodeValue();
        if(StringUtils.isBlank(codevalue)){
            return false;
        }
        return libraryService.ycode(library);
    }

    @RequestMapping("/q")
    public Object test(){
        return "";
    }


    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<User> list = userService.list();

        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.addHeaderAlias("userId","用户编号");
        writer.addHeaderAlias("userName","用户名");
        writer.addHeaderAlias("userPassword","用户密码");
        writer.addHeaderAlias("email","邮箱");

        writer.write(list,true);
        //设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();



    }

    @PostMapping("/import")
    public String importExcel(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<User> list = reader.readAll(User.class);
        userService.saveBatch(list);
        return "true";

    }

//    @GetMapping("/eqlCode")
//    public boolean eCode( String code, HttpSession httpSession){
//        return mailService.equCode(code,httpSession);
//    }
//
//
//    @PostMapping("/saveCode")
//    public boolean saveCode(@RequestBody Library library){
//        return libraryService.saveOrUpdate(library);
//    }





    //分页查询
//    @GetMapping("/page")
//    public Map<String,Object> findPage(@RequestParam Integer pageNum,
//                                       @RequestParam Integer pageSize,
//                                       @RequestParam String username){
//        pageNum = (pageNum - 1) * pageSize;
//        Integer total = userMapper.selectTotal(username);
//        List<User> data= userMapper.selectPage(pageNum,pageSize,username);
//        Map<String,Object> res = new HashMap<>();
//        res.put("data",data);
//        res.put("total",total);
//        return res;
//    }
}
