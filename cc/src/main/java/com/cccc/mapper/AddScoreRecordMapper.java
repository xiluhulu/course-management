package com.cccc.mapper;

import com.cccc.pojo.AddScoreRecord;
import org.apache.ibatis.annotations.Mapper;


import java.util.ArrayList;
import java.util.List;

@Mapper
public interface AddScoreRecordMapper {
    int applyForExtraPoints(AddScoreRecord addScoreRecord);
//  学生获取加分详情
    List<AddScoreRecord> getBonusPointsDetails(AddScoreRecord addScoreRecord);
//    学生获取曲线图数据
    List<AddScoreRecord> getGraphData(AddScoreRecord addScoreRecord);
    List<AddScoreRecord> teaGetBonusPointsDetails(ArrayList<Integer> list);

    Long teaGetUnapproved(ArrayList<Integer> list);

    Long teaGetTotalApprovals(ArrayList<Integer> list);

    int consentApplication(AddScoreRecord addScoreRecord);

    int teaBonusPoints(AddScoreRecord addScoreRecord);

    int insertAddRecord(AddScoreRecord addScoreRecord);
}
