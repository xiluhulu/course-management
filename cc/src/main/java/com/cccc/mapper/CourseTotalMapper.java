package com.cccc.mapper;

import com.cccc.pojo.AddScoreRecord;
import com.cccc.pojo.Course;
import com.cccc.pojo.CourseTotal;
import com.cccc.pojo.User;

import java.util.HashMap;
import java.util.List;

public interface CourseTotalMapper {

    //    通过课程id查找人数
    Long useCourseIdGetStuPeople(Course course);

    //    根据学生id查找学生各个课程的分数情况
    List<CourseTotal> useUserIdGetStuTotal(int id);


    //    根据课程id与班级id查找教师管理的班级
    List<CourseTotal> useCourseIdAndClassIdGetUserAndClassAndTotla(HashMap<String, Object> map);

    //    根据班级id查找的用户id进行对同一课程id进行添加
    int useClassIdInsertUserIdAndTotla(HashMap<String, Object> map);


    //    删除指定课程id与指定班级id的所有学生总分
    int useCourseIdAndClassIdDelStuTotal(HashMap<String, Object> map);

    //  用于删除指定课程的学生分数详情
    int useCourseIdDelStuTotal(int id);

    //    学生获取需要上的课程
    List<Course> stuGetCourse(int id);

    //    学生获取课程分数,及排名
    CourseTotal stuGetTotlaAndRanking(HashMap<String, Object> map);

    //    教师同意后加分
    int teaAddScoreAfterConsent(AddScoreRecord addScoreRecord);

    //      教师修改分数
    int teaUpdataTotal(CourseTotal courseTotal);

    //      根据用户id删除用户分数
    int useUserIdDeleteCourseTotal(int id);

    //      学生加入公开课
    int stuAddCourse(CourseTotal courseTotal);

    //      学生退出公开课
    int escPublicCourse(CourseTotal courseTotal);
}
