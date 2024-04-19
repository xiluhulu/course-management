package com.cccc.service;

import com.cccc.common.R;
import com.cccc.pojo.AllClass;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Repository
public interface AllClassService {
//    获取所有班级
    R getAllClass(String keyword);
//    获取管理的班级
    R getClassListTea(HttpServletRequest request);
//    教师添加班级
    R insertClass(HttpServletRequest request,AllClass allClass);
//     删除班级
    R deleteClass(AllClass allClass);
//    根据id查找班级
    R useIdGetClass(AllClass allClass);
//    根据班级id修改班级名称
    R useIdUpdeteClass(AllClass allClass);
//    使用课程id获取选了该课程的班级名称
    R useCourseIdGetClass(int id);
    //    获取可选的课程
    R useCourseIdGetOptionalClass(int id);

    R useClassIdLetCourseInsertStu(int classId,int courseId);
}
