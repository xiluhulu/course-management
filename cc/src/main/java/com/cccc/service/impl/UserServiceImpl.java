package com.cccc.service.impl;

import com.cccc.common.R;
import com.cccc.mapper.AllClassMapper;
import com.cccc.mapper.UserMapper;
import com.cccc.pojo.AllClass;
import com.cccc.pojo.User;
import com.cccc.service.UserService;
import com.cccc.utils.Jwt_Utils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired(required=false)
    private UserMapper userMapper;
    @Autowired(required=false)
    private AllClassMapper allClassMapper;
    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public R getAllStuInfo(String keyword) {
        List<User> stuInfo = userMapper.getAllStuInfo(keyword);
        ArrayList<String> list = new ArrayList<>();
        for (int i=0;i<stuInfo.size();i++){
            AllClass allClass = allClassMapper.useClassIdGetClassName(stuInfo.get(i).getClassId());
            list.add(allClass.getClassname());
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("stuInfo",stuInfo);
        map.put("className",list);
        System.out.println("######################aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa###################");
        System.out.println("######################aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa###################");
        return R.ok(map);
    }

    @Override
    public List<User> getAllTeacherInfo(String keyword) {
        return userMapper.getAllTeacherInfo(keyword);
    }
//  添加学生
    @Override
    public R insertStu(User user) {

        User s = userMapper.stuJobGetStuJob(user);
        if (s!=null){
            return R.error(508,"已有相同学号的同学了");
        }
        int i = userMapper.insertUser(user);
        return R.ok(i);
    }
//    登录
    @Override
    public R login(User user) {
        User s = userMapper.stuJobGetStuJob(user);
        //用于判断用户是否存在
        if (s==null){
            return R.error(501,"找不到用户");
        }
        User info = userMapper.stuJobgetUserIdOrPassword(user);
        System.out.println("=====:"+info.toString());
//        用于判断密码是否错误
        if(!user.getPassword().equals(info.getPassword())){
            return R.error(502,"密码错误");
        }
//        成功后创建token，并返回token与角色
        String token = Jwt_Utils.createToken(info.getId(), info.getRole());
        HashMap<String, Object> t = new HashMap<>();
        t.put("token",token);
        t.put("role",info.getRole());
        t.put("user",s);
        return R.ok(t);
    }

    @Override
    public R getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");
        User user = userMapper.useIdGetUser(id);

        return R.ok(user);
    }

    //    删除用户
    @Override
    public R deleteUser(User user) {
        int i = userMapper.deleteUser(user.getId());
        return R.ok(i);
    }

    //    根据班级id获取学生
    @Override
    public R useClassIdGetStu(User user) {
        List<User> users = userMapper.useClassIdGetStu(user.getClassId());
        return R.ok(users);
    }
//    根据id获取用户
    @Override
    public R useIdGetUser(User user) {
        User user1 = userMapper.useIdGetUser(user.getId());
        return R.ok(user1);
    }
//修改用于信息
    public R updateUser (User user){
        User user1 = null;
        try {
            user1 = userMapper.ifExistUser(user);
            if (user1.getStuJobNum()!=null){
                return R.error(508,"已有相同学号的同学了");
            }
        } catch (Exception e) {
            int i = userMapper.updateUser(user);
            return R.ok(i);
        }
        return R.ok("1111111");

    }
//    批量添加学生
    @Override
    public R teaBatchAddStu(ArrayList<User> users) {

        ArrayList<Object> list = new ArrayList<>();

        for (int i=0;i<users.size();i++){
            User s = userMapper.stuJobGetStuJob(users.get(i));
            if(s!=null){
                list.add(users.get(i));
            }
        }
        if(list.size()>0){
            return R.error(654,list,"学号重复");
        }
        int u = userMapper.teaBatchAddStu(users);
        return R.ok(u);
    }
//    修改密码
    @Override
    public R changePassword(HttpServletRequest request, String oldPassword, String newPassword) {
//        判断旧密码是否正确
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");
        User user = userMapper.useIdGetUser(id);
        //        正确修改密码
        if(user.getPassword().equals(oldPassword)){
            user.setPassword(newPassword);
            int i = userMapper.updateUser(user);
            return R.ok("修改成功");
        }
//        不正确返回
        return R.error(654,"旧密码不正确");
    }
    //    管理员获取教师列表用于添加课程
    @Override
    public R adminGetTeaList() {
        List<User> users = userMapper.adminGetTeaList();
        return R.ok(users);
    }

}
