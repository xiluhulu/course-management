package com.cccc.service;

import com.cccc.common.R;
import com.cccc.pojo.Course;
import com.cccc.pojo.CourseTotal;
import com.cccc.pojo.User;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Repository
public interface CourseService {
    R getAllCourse(String keyword);
    //    根据教师id获取所管理的课程及课程的人数
    R useUserIdGetCourse(HttpServletRequest request);

    R useUserIdGetStuTotal(User user);

    R useCourseIdGetCoursePersonnel(HashMap<String,Object> map);
//    删除指定课程id与指定班级id的所有学生总分
    R useCourseIdAndClassIdDelStuTotal(int courseId,int classId);
    //添加课程
    R insertCourse(HttpServletRequest request ,Course course);
    //删除课程及学生总分
    R delCourse(int id);
    //根据课程id获取课程
    R useCourseIdGetCourse( int id);
//      修改课程名称
    R updateCourseName( Course course);
//      学生获取需要上的课程
    R stuGetCourse(HttpServletRequest request);
//    学生获取未选择且公开的课程
    R getNotSelectedAndPublicCourse(HttpServletRequest request);

//    学生获取课程分数,及排名
    R stuGetTotlaAndRanking(HttpServletRequest request,int courseId);

    R teaUpdataTotal(CourseTotal courseTotal ,HttpServletRequest request);
//    学生加入公开课
    R stuAddCourse(HttpServletRequest request,Course course);
//    学生退出公开课
    R escPublicCourse(HttpServletRequest request,Course course);
//    管理员添加课程
    R adminInsertCourse( Course course);
}
