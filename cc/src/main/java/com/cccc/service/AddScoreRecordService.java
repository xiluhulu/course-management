package com.cccc.service;

import com.cccc.common.R;
import com.cccc.pojo.AddScoreRecord;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Repository
public interface AddScoreRecordService {
//    学生申请加分
    R applyForExtraPoints(HttpServletRequest request, @RequestBody AddScoreRecord addScoreRecord);
//    学生获取加分详情
    R getBonusPointsDetails(HttpServletRequest request,int courseId);
//    学生获取曲线图数据
    R getGraphData(HttpServletRequest request,int courseId);

    R teaGetBonusPointsDetails(HttpServletRequest request);

    //    教师获取总审批数量
    R teaGetUnapproved(HttpServletRequest request);

    R teaGetTotalApprovals(HttpServletRequest request);

    R consentApplication(HttpServletRequest request, AddScoreRecord addScoreRecord);

    R disagreeWithTheApplication(HttpServletRequest request,AddScoreRecord addScoreRecord);

    R teaBonusPoints(HttpServletRequest request,AddScoreRecord addScoreRecord);

}
