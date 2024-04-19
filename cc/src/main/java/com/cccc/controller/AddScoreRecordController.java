package com.cccc.controller;

import com.cccc.common.R;
import com.cccc.pojo.AddScoreRecord;
import com.cccc.service.AddScoreRecordService;
import com.cccc.service.AllClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/add_score_record")
public class AddScoreRecordController {
    @Autowired
    private AddScoreRecordService addScoreRecordService;

//    学生申请加分
    @PostMapping("/stu/applyForExtraPoints")
    public R applyForExtraPoints(HttpServletRequest request, @RequestBody AddScoreRecord addScoreRecord){
//        System.out.println(addScoreRecord.toString());

        return addScoreRecordService.applyForExtraPoints(request,addScoreRecord);
    }
//    学生获取加分详情
    @RequestMapping("/stu/getBonusPointsDetails")
    public R getBonusPointsDetails(HttpServletRequest request,int courseId){
//        System.out.println(courseId);
        return addScoreRecordService.getBonusPointsDetails(request,courseId);
    }
    //学生获取曲线图数据
    @RequestMapping("/stu/getGraphData")
    public R getGraphData(HttpServletRequest request,int courseId){

        return addScoreRecordService.getGraphData(request,courseId);
    }
//    教师获取所管理课程加分详情
    @RequestMapping("/tea/teaGetBonusPointsDetails")
    public R teaGetBonusPointsDetails(HttpServletRequest request){

        return addScoreRecordService.teaGetBonusPointsDetails(request);
    }


//    教师获取未审批的数量
    @RequestMapping("/tea/teaGetUnapproved")
    public R teaGetUnapproved(HttpServletRequest request){
        return addScoreRecordService.teaGetUnapproved(request);
    }
//    教师获取总审批数量
    @RequestMapping("/tea/teaGetTotalApprovals")
    public R teaGetTotalApprovals(HttpServletRequest request){
        return addScoreRecordService.teaGetTotalApprovals(request);
    }
//    教师同意加分申请
    @PostMapping("/tea/consentApplication")
    public R consentApplication(HttpServletRequest request,@RequestBody AddScoreRecord addScoreRecord){

        return addScoreRecordService.consentApplication(request,addScoreRecord);
    }
//    教师不同意申请
    @PostMapping("/tea/disagreeWithTheApplication")
    public R disagreeWithTheApplication(HttpServletRequest request,@RequestBody AddScoreRecord addScoreRecord){
        return addScoreRecordService.disagreeWithTheApplication(request,addScoreRecord);
    }

    //    教师加分
    @PostMapping("/tea/teaBonusPoints")
    public R teaBonusPoints(HttpServletRequest request,@RequestBody AddScoreRecord addScoreRecord){
        return addScoreRecordService.teaBonusPoints(request,addScoreRecord);
    }
}
