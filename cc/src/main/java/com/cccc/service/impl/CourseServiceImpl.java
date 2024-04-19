package com.cccc.service.impl;

import com.cccc.common.R;
import com.cccc.mapper.AddScoreRecordMapper;
import com.cccc.mapper.CourseMapper;
import com.cccc.mapper.CourseTotalMapper;
import com.cccc.mapper.UserMapper;
import com.cccc.pojo.AddScoreRecord;
import com.cccc.pojo.Course;
import com.cccc.pojo.CourseTotal;
import com.cccc.pojo.User;
import com.cccc.service.CourseService;
import com.cccc.utils.Jwt_Utils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired(required=false)
    private CourseMapper courseMapper;

    @Autowired(required=false)
    private CourseTotalMapper courseTotalMapper;

    @Autowired(required=false)
    private UserMapper userMapper;

    @Autowired(required=false)
    private AddScoreRecordMapper addScoreRecordMapper;
    @Override
    public R getAllCourse( String keyword) {

        List<Course> allCourse = courseMapper.getAllCourse( keyword);

        ArrayList<Object> sum = new ArrayList<>();
        for (int i = 0; i<allCourse.size();i++){
            Long aLong = courseTotalMapper.useCourseIdGetStuPeople(allCourse.get(i));
            sum.add(aLong);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("Courses",allCourse);
        map.put("sum",sum);

        return R.ok(map);
    }

    @Override//    根据教师id获取所管理的课程及课程的人数
    public R useUserIdGetCourse(HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");
        HashMap<String, Object> map = new HashMap<>();
//        使用id查找课程
        List<Course> courses = courseMapper.useUserIdGetCourse(id);
        map.put("courses",courses);
//        通过课程id查找人数
        ArrayList<Object> sum = new ArrayList<>();
        for (int i = 0;i<courses.size();i++){
            Long aLong = courseTotalMapper.useCourseIdGetStuPeople(courses.get(i));
            sum.add(aLong);
        }
        map.put("sum",sum);
        return R.ok(map);
    }

    @Override
    public R useUserIdGetStuTotal(User user) {
        System.out.println("000000000000000000000:"+user.toString());
        List<CourseTotal> courseTotals = courseTotalMapper.useUserIdGetStuTotal(user.getId());
        return R.ok(courseTotals);
    }

    @Override
    public R useCourseIdGetCoursePersonnel(HashMap<String, Object> map) {
        List<CourseTotal> courseTotals = courseTotalMapper.useCourseIdAndClassIdGetUserAndClassAndTotla(map);
        return R.ok(courseTotals);
    }

    @Override
    public R useCourseIdAndClassIdDelStuTotal(int courseId, int classId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("courseId",courseId);
        map.put("classId",classId);
        int i = courseTotalMapper.useCourseIdAndClassIdDelStuTotal(map);
        return R.ok(i);
    }
//教师添加课程
    @Override
    public R insertCourse(HttpServletRequest request,Course course) {
        System.out.println("aaaaaaaaaaaaaa:"+course);
        Course course1 = courseMapper.ifExistCourse(course.getCourse());
        if (course1!=null ){
           return R.error(888 ,"已有该课程，不能创建重复课程");
        }
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");

        course.setUserId(id);
        course.setCourse(course.getCourse());
        course.setImgUrl(course.getImgUrl());
        int i = courseMapper.insertCourse(course);
        return R.ok(i);
    }
// 教师删除课程
    @Override
    public R delCourse(int id) {
//        删除课程
        courseMapper.delCourse(id);
//        删除属于该课程的学生及分数
        courseTotalMapper.useCourseIdDelStuTotal(id);
        return R.ok("删除成功");
    }


//根据课程id获取课程
    @Override
    public R useCourseIdGetCourse(int id) {
        Course course = courseMapper.useCourseIdGetCourse(id);
        return R.ok(course);
    }

    @Override
    public R updateCourseName(Course course) {
        Course course1 = courseMapper.ifExistCourse(course.getCourse());
        if (course1!=null && course1.getId()!=course.getId()){
            return R.error(888 ,"已有同名课程");
        }
        int i = courseMapper.updateCourse(course);
        return R.ok(i);
    }
    //      学生获取需要上的课程
    @Override
    public R stuGetCourse(HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");
        List<Course> courses = courseTotalMapper.stuGetCourse(id);
        return R.ok(courses);
    }
    //    学生获未选且公开的课程
    @Override
    public R getNotSelectedAndPublicCourse(HttpServletRequest request) {
//        先获取已选择的课程
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");
        List<Course> courses = courseTotalMapper.stuGetCourse(id);
        ArrayList<Object> list = new ArrayList<>();
        for (int i=0;i<courses.size();i++){
            list.add(courses.get(i).getId());
        }

        List<Course> notSelectedAndPublicCourse = courseMapper.getNotSelectedAndPublicCourse(list);

        return R.ok(notSelectedAndPublicCourse);
    }

    //    学生获取课程分数,及排名
    @Override
    public R stuGetTotlaAndRanking(HttpServletRequest request, int courseId) {


        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int userId = (int) claims.get("id");
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("courseId",courseId);
        System.out.println(map.toString());
        CourseTotal courseTotal = courseTotalMapper.stuGetTotlaAndRanking(map);

        return R.ok(courseTotal);
    }

    @Override
    public R teaUpdataTotal(CourseTotal courseTotal,HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");
        User user = userMapper.useIdGetUser(id);

        int i = courseTotalMapper.teaUpdataTotal(courseTotal);
        AddScoreRecord addScoreRecord = new AddScoreRecord();
        addScoreRecord.setUserId(courseTotal.getUserId());
        addScoreRecord.setCourseId(courseTotal.getCourseId());
        addScoreRecord.setCause("0");
        addScoreRecord.setAddTheScores(courseTotal.getTotal());
        addScoreRecord.setApplicationDate(new Date());
        addScoreRecord.setProcessState(2);
        addScoreRecord.setProcessDate(new Date());
        addScoreRecord.setProcessName(user.getUsername());
        addScoreRecord.setComeFrom(2);
        int i1 = addScoreRecordMapper.insertAddRecord(addScoreRecord);

        return R.ok(i);
    }

    @Override
    public R stuAddCourse(HttpServletRequest request,Course course) {
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int userId = (int) claims.get("id");
        CourseTotal courseTotal = new CourseTotal();
        courseTotal.setUserId(userId);
        courseTotal.setCourseId(course.getId());
        courseTotalMapper.stuAddCourse(courseTotal);
        return R.ok("加入成功");
    }

    @Override
    public R escPublicCourse(HttpServletRequest request, Course course) {

        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int userId = (int) claims.get("id");
        CourseTotal courseTotal = new CourseTotal();
        courseTotal.setUserId(userId);
        courseTotal.setCourseId(course.getId());
        courseTotalMapper.escPublicCourse(courseTotal);
        return R.ok("已退出");
    }
//    管理员添加课程
    @Override
    public R adminInsertCourse(Course course) {
        Course course1 = courseMapper.ifExistCourse(course.getCourse());
        if (course1!=null ){
            return R.error(888 ,"已有该课程，不能创建重复课程");
        }
        int i = courseMapper.adminInsertCourse(course);
        return R.ok(i);
    }


}
