package com.cccc.mapper;

import com.cccc.pojo.AllClass;
import com.cccc.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.ArrayList;
import java.util.List;
@Mapper
public interface UserMapper {
//    查找所有用户
    List<User> findAll();
//    查找所有学生
    List<User> getAllStuInfo(String keyword);
//    查找所有教师
    List<User> getAllTeacherInfo(String keyword);
//    添加一个学生
    int insertUser(User user);
//    添加一个老师
    int insertTeacher(User user);
//    根据学号或工号查询用户名密码还有用户id
    User stuJobgetUserIdOrPassword(User user);
//    根据学号或工号查询数据库中是否有该用户
    User stuJobGetStuJob(User user);
//    根据班级id查找用户数量
    Long useClassIdGetUser(AllClass allClass);
//    根据班级id查找学生
    List<User> useClassIdGetStu(int classId);
//    删除用户
    int deleteUser(int id);
//    更新用户
    int updateUser(User user);
//    根据id查找用户
    User useIdGetUser(int id);
//    判断是否有学号相同且不是自己的用户
    User ifExistUser(User user);

    int teaBatchAddStu(@Param("userlist") ArrayList<User> userlist);

//    根据班级id删除所有人员
    int useClassIdDeleteUser(int id);
    //    管理员获取教师列表用于添加课程
    List<User> adminGetTeaList();
}
