package com.cccc.service;

import com.cccc.common.R;
import com.cccc.pojo.User;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public interface UserService {
    List<User> findAll();
    R getAllStuInfo(String keyword);
    List<User> getAllTeacherInfo(String keyword);
    R insertStu(User user);
    R login(User user);
    R getUserInfo(HttpServletRequest request);
    R deleteUser(User user);
    R useClassIdGetStu(User user);
    R useIdGetUser(User user);
    R updateUser( User user);

    R teaBatchAddStu(ArrayList<User> users);

    R  changePassword(HttpServletRequest request,String oldPassword,String newPassword);

//    管理员获取教师列表用于添加课程
    R adminGetTeaList();
}
