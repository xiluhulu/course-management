package com.cccc.service.impl;

import com.cccc.common.R;
import com.cccc.mapper.*;
import com.cccc.pojo.AddScoreRecord;
import com.cccc.pojo.Course;
import com.cccc.pojo.User;
import com.cccc.service.AddScoreRecordService;
import com.cccc.utils.Jwt_Utils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class AddScoreRecordServiceImol implements AddScoreRecordService {
    @Autowired(required=false)
    private AddScoreRecordMapper addScoreRecordMapper;

    @Autowired(required=false)
    private CourseMapper courseMapper;
    @Autowired(required=false)
    private CourseTotalMapper courseTotalMapper;

    @Autowired(required=false)
    private UserMapper userMapper;
    @Override
    public R applyForExtraPoints(HttpServletRequest request, AddScoreRecord addScoreRecord) {
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");
        addScoreRecord.setUserId(id);
        addScoreRecord.setApplicationDate(new Date());
//        System.out.println(addScoreRecord.toString());
        int i = addScoreRecordMapper.applyForExtraPoints(addScoreRecord);
        return R.ok(i);
    }
//      学生获取加分详情
    @Override
    public R getBonusPointsDetails(HttpServletRequest request, int courseId) {
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");
        AddScoreRecord addScoreRecord = new AddScoreRecord();
        addScoreRecord.setUserId(id);
        addScoreRecord.setCourseId(courseId);

        List<AddScoreRecord> bonusPointsDetails = addScoreRecordMapper.getBonusPointsDetails(addScoreRecord);
        return R.ok(bonusPointsDetails);
    }
//      学生获取曲线图数据
    @Override
    public R getGraphData(HttpServletRequest request, int courseId) {
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");
        AddScoreRecord addScoreRecord = new AddScoreRecord();
        addScoreRecord.setUserId(id);
        addScoreRecord.setCourseId(courseId);

        List<AddScoreRecord> bonusPointsDetails = addScoreRecordMapper.getGraphData(addScoreRecord);
        return R.ok(bonusPointsDetails);
    }

    @Override
    public R teaGetBonusPointsDetails(HttpServletRequest request) {
        //        获取所管理的课程idList
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");

        List<Course> courses = courseMapper.useUserIdGetCourse(id);
        ArrayList<Integer> list = new ArrayList<Integer>();

        for (int i=0;i<courses.size();i++){
            list.add(courses.get(i).getId());
        }
        //        获取含该课程的加分详情
        List<AddScoreRecord> addScoreRecords = addScoreRecordMapper.teaGetBonusPointsDetails(list);

        return R.ok(addScoreRecords);
    }
    //    教师获取未审批的数量
    @Override
    public R teaGetUnapproved(HttpServletRequest request) {
        //        获取所管理的课程idList
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");

        List<Course> courses = courseMapper.useUserIdGetCourse(id);
        ArrayList<Integer> list = new ArrayList<Integer>();

        for (int i=0;i<courses.size();i++){
            list.add(courses.get(i).getId());
        }

        Long aLong = addScoreRecordMapper.teaGetUnapproved(list);

        return R.ok(aLong);
    }

    @Override
    public R teaGetTotalApprovals(HttpServletRequest request) {
        //        获取所管理的课程idList
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");

        List<Course> courses = courseMapper.useUserIdGetCourse(id);
        ArrayList<Integer> list = new ArrayList<Integer>();

        for (int i=0;i<courses.size();i++){
            list.add(courses.get(i).getId());
        }

        Long aLong = addScoreRecordMapper.teaGetTotalApprovals(list);

        return R.ok(aLong);
    }
//   同意
    @Override
    public R consentApplication(HttpServletRequest request, AddScoreRecord addScoreRecord) {
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");
        User user = userMapper.useIdGetUser(id);
        addScoreRecord.setProcessDate(new Date());
        addScoreRecord.setProcessName(user.getUsername());
        addScoreRecord.setProcessState(2);
//        修改申请状态
        addScoreRecordMapper.consentApplication(addScoreRecord);

//        添加分数
        int i1 = courseTotalMapper.teaAddScoreAfterConsent(addScoreRecord);

        return R.ok(i1);
    }

    @Override
    public R disagreeWithTheApplication(HttpServletRequest request, AddScoreRecord addScoreRecord) {
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");
        User user = userMapper.useIdGetUser(id);
        addScoreRecord.setProcessDate(new Date());
        addScoreRecord.setProcessName(user.getUsername());
        addScoreRecord.setProcessState(1);
//        修改申请状态
        int i = addScoreRecordMapper.consentApplication(addScoreRecord);

        return R.ok(i);
    }

    @Override
    public R teaBonusPoints(HttpServletRequest request, AddScoreRecord addScoreRecord) {
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");
        User user = userMapper.useIdGetUser(id);
        addScoreRecord.setApplicationDate(new Date());
        addScoreRecord.setProcessState(2);
        addScoreRecord.setProcessName(user.getUsername());
        addScoreRecord.setProcessDate(new Date());
        addScoreRecord.setComeFrom(1);
        int i1 = addScoreRecordMapper.teaBonusPoints(addScoreRecord);
        int i = courseTotalMapper.teaAddScoreAfterConsent(addScoreRecord);

        return R.ok(i);
    }



}
