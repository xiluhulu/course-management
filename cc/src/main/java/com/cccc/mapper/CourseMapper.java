package com.cccc.mapper;

import com.cccc.pojo.Course;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface CourseMapper {
    List<Course> getAllCourse(String keyword );


//    通过教师id查找课程
    List<Course> useUserIdGetCourse(int id);
//    用于判断有无该课程
    Course ifExistCourse(String course);
//    教师用于添加课程
    int insertCourse(Course course);

//    删除课程
    int delCourse(int id);
//    根据课程id获取课程
    Course useCourseIdGetCourse(int id);
//    根据课程id修改课程
    int updateCourse(Course course);
//    改url
    int updateUrl(Course course);
// 获取未选且公开的课程
    List<Course> getNotSelectedAndPublicCourse(ArrayList list);
//管理员添加课程
    int adminInsertCourse(Course course);
}

