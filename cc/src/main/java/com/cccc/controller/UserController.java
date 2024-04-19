package com.cccc.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.cccc.common.R;
import com.cccc.pojo.Course;
import com.cccc.pojo.User;
import com.cccc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

//    获取所有人员
    @RequestMapping("/admin/getalllist")
    public R getAllList(){
        return R.ok(userService.findAll());
    }
//    插入学生
    @PostMapping("/insertstu")
    public R insertStu(@RequestBody User user){
        return userService.insertStu(user);
    }
//    获取所有学生
    @RequestMapping("/admin/getallstuinfo")
    public R getAllStuInfo(String keyword){

        return userService.getAllStuInfo(keyword);
    }
//    获取所有教师
    @RequestMapping("/admin/getallteacherinfo")
    public R getAllTeacherInfo(String keyword){
        System.out.println(keyword);
        return R.ok(userService.getAllTeacherInfo(keyword));
    }
    @RequestMapping("/getUserInfo")
    public R getUserInfo(HttpServletRequest request){
        return userService.getUserInfo(request);
    }
//    根据学号或者工号登录
    @PostMapping("/login")
    public R login(@RequestBody User user){
        log.info("登录：",user);
        System.out.println(user.toString());
        return userService.login(user);
    }
    @RequestMapping("/tea/useClassIdGetStu")
    public R useClassIdGetStu(User user){
        return userService.useClassIdGetStu(user);
    }
//    根据学号或工号删除用户
    @PostMapping("/tea/deleteUser")
    public R deleteUser(@RequestBody User user){
        return userService.deleteUser(user);
    }
    //    根据id查找用户
    @RequestMapping("/tea/useIdGetUser")
    public R useIdGetUser(User user){
        return userService.useIdGetUser(user);
    }
    //    根据id跟新用户
    @PostMapping("/tea/update")
    public R updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }

//    批量添加学生
    @PostMapping("/tea/teaBatchAddStu")
    public R teaBatchAddStu(@RequestBody HashMap<String, Object> map){
        int classId = Integer.parseInt(map.get("classId").toString()) ;
        String role = map.get("role").toString();
        List<HashMap<String,Object>> userList = (List<HashMap<String, Object>>) map.get("userList");
        ArrayList<User> users = new ArrayList<>();
        System.out.println(role);
        for (int i =0;i<userList.size();i++){
            User user = new User();
            user.setStuJobNum(userList.get(i).get("stuJobNum").toString());
            user.setClassId(classId);
            user.setNum(userList.get(i).get("num").toString());
            user.setUsername(userList.get(i).get("username").toString());
            user.setPassword(userList.get(i).get("password").toString());
            user.setSex(Integer.parseInt(userList.get(i).get("sex").toString()));
            user.setRole(role);
            users.add(user);

        }
        users.forEach(System.out::println);
        return userService.teaBatchAddStu(users);
//        return null;
    }
//    修改密码
    @PostMapping("/changePassword")
    public R  changePassword(@RequestBody HashMap<String, Object> map,HttpServletRequest request){
        String oldPassword = map.get("oldPassword").toString();
        String newPassword = map.get("newPassword").toString();
//        System.out.println(oldPassword);
//        System.out.println(newPassword);
        return userService.changePassword(request,oldPassword,newPassword);
    }

//    管理员获取教师列表用于添加课程
    @RequestMapping("/admin/adminGetTeaList")
    public R adminGetTeaList(){
        return userService.adminGetTeaList();
    }

}
