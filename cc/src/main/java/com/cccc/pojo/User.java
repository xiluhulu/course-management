package com.cccc.pojo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;


@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)

public class User {
    private int id;//id
    private String stuJobNum;//学号、工号
    private String password;//密码
    private String role;//角色：管理员=0、教师=1、学生=2
    private int classId;//班级id：教师与管理员为0
    private String username;//用户名
    private int sex;//性别
    private String num;//手机号码

    public User(HttpServletRequest request){
        this.username = request.getParameter("username");
        this.password = request.getParameter("password");
    }
}
