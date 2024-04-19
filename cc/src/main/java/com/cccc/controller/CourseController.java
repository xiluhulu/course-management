package com.cccc.controller;

import com.cccc.common.R;
import com.cccc.pojo.Course;
import com.cccc.pojo.CourseTotal;
import com.cccc.pojo.User;
import com.cccc.service.CourseService;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;
//  管理员获取课程
    @RequestMapping("/admin/getAllCourse")
    public R getAllCourse(String keyword) {
        System.out.println(keyword);
        return courseService.getAllCourse(keyword);
    }

    //    根据教师id获取所管理的课程及课程的人数
    @RequestMapping("/tea/useUserIdGetCourse")
    public R useUserIdGetCourse(HttpServletRequest request) {
        return courseService.useUserIdGetCourse(request);
    }


    @RequestMapping("/tea/lookStuTotal")
    public R useUserIdGetStuTotal(User user) {
        return courseService.useUserIdGetStuTotal(user);
    }

    //    根据课程id获取上该课程的人
    @RequestMapping("/tea/useCourseIdGetCoursePersonnel")
    public R useCourseIdGetCoursePersonnel(@RequestParam("courseId") int courseId, @RequestParam("classId") int classId) {
        System.out.println("课程id：" + courseId);
        System.out.println("班级id：" + classId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("courseId", courseId);
        map.put("classId", classId);
        return courseService.useCourseIdGetCoursePersonnel(map);
    }

    ;

    //    删除指定课程id与指定班级id的所有学生总分
    @RequestMapping("/tea/useCourseIdAndClassIdDelStuTotal")
    public R useCourseIdAndClassIdDelStuTotal(int courseId, int classId) {
        return courseService.useCourseIdAndClassIdDelStuTotal(courseId, classId);
    }

    //    教师添加课程
    @PostMapping("/tea/insertCourse")
    public R insertCourse(@RequestBody Course course, HttpServletRequest request) {
        return courseService.insertCourse(request, course);
    }

    //  教师删除课程
    @RequestMapping("/tea/delCourse")
    public R delCourse(int id) {
        return courseService.delCourse(id);
    }

    //教师根据课程id获取一条课程用于修改
    @RequestMapping("/tea/useCourseIdGetCourse")
    public R useCourseIdGetCourse(int id) {
        return courseService.useCourseIdGetCourse(id);
    }

    //    修改课程名称,及图片url
    @PostMapping("/tea/updateCourseName")
    public R updateCourseName(@RequestBody Course course) {
//        System.out.println(course.toString());
        return courseService.updateCourseName(course);
    }

    //    学生获取课程
    @RequestMapping("/stu/getCourse")
    public R stuGetCourse(HttpServletRequest request) {
        return courseService.stuGetCourse(request);
    }

    //    学生获未选且公开的课程
    @RequestMapping("/stu/getNotSelectedAndPublicCourse")
    public R getNotSelectedAndPublicCourse(HttpServletRequest request) {
        return courseService.getNotSelectedAndPublicCourse(request);
    }

    //    学生获取课程分数,及排名
    @RequestMapping("/stu/stuGetTotlaAndRanking")
    public R stuGetTotlaAndRanking(HttpServletRequest request, int courseId) {
//        System.out.println("cccc:"+courseId);
        return courseService.stuGetTotlaAndRanking(request, courseId);
    }

    @PostMapping("tea/teaUpdataTotal")
    public R teaUpdataTotal(@RequestBody CourseTotal courseTotal, HttpServletRequest request) {
        System.out.println(courseTotal.toString());
        return courseService.teaUpdataTotal(courseTotal, request);
    }

    //    学生加入公开课
    @RequestMapping("stu/stuAddCourse")
    public R stuAddCourse(HttpServletRequest request, Course course) {
//        System.out.println(course.toString());
        return courseService.stuAddCourse(request, course);
    }

    //    学生退出公开课
    @RequestMapping("stu/escPublicCourse")
    public R escPublicCourse(HttpServletRequest request, Course course) {
        System.out.println(course.toString());
        return courseService.escPublicCourse(request, course);
    }

    //    管理员添加课程
    @PostMapping("/admin/adminInsertCourse")
    public R adminInsertCourse(@RequestBody Course course){
        System.out.println(course.toString());

        return courseService.adminInsertCourse(course);
    }
}
