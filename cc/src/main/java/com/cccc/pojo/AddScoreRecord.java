package com.cccc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddScoreRecord {
    private int id;//id
    private int userId;//申请的学生id
    private int courseId;//申请的课程id
    private String cause;//申请的原因
    private int addTheScores;//申请加分的分数
    private Date applicationDate;//申请的时间
    private int processState;//处理状态（0：处理中，1：不同意，2：同意）
    private Date processDate;//处理时间
    private String processName;//处理人名称
    private int comeFrom;//来自（0：学生，1：教师,2：教师直接修改分数）

    private User user;
    private Course course;
}
