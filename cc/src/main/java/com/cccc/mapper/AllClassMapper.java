package com.cccc.mapper;

import com.cccc.pojo.AllClass;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface AllClassMapper {
//    查看所有班级
    List<AllClass> getAllClass(String keyword);

    List<AllClass> getClassListTea(int id);
    // 查看学生所在的班级
    AllClass useClassIdGetClassName(int id);

//    判断有无相同的班级
    AllClass ifExistClass(AllClass allClass);
//    添加班级
    int insertClass(AllClass allClass);
//    删除班级
    int deleteClass(int id);
//    根据id查找班级
    AllClass useIdGetClass(int id);
//修改班级
    int useIdUpdeteClass(AllClass allClass);
//    使用课程id获取选了该课程的班级
    List<AllClass> useCourseIdGetClass(int id);
//    获取可选的班级
    List<AllClass> useCourseIdGetOptionalClass(List<Object> list);
}
