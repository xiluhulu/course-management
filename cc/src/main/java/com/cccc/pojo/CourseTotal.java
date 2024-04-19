package com.cccc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseTotal {
    private int userId;//学生id
    private int courseId;//课程id
    private int total;//总分数.
    private int ranking;//排名


    private Course course;

    private User user;

    private AllClass allClass;

}
