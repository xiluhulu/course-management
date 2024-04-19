package com.cccc.controller;

import com.cccc.common.R;
import com.cccc.pojo.AllClass;
import com.cccc.service.AllClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/all_lass")
public class AllClassControLLer {
    @Autowired
    private AllClassService allClassService;
//    获取所有班级
    @RequestMapping("/admin/get_all_class")
    public R getAllClass(String keyword){
        return allClassService.getAllClass(keyword);
    }
//    获取管理的班级
    @RequestMapping("/tea/getClassList")
    public R getClassListTea(HttpServletRequest request){
        return allClassService.getClassListTea(request);
    }
//    教师添加班级
    @PostMapping("/tea/insertClass")
    public R insertClass(HttpServletRequest request,@RequestBody AllClass allClass){
        return allClassService.insertClass(request ,allClass);
    }

//    根据班级id删除班级
    @PostMapping("/tea/deleteClass")
    public R deleteClass(@RequestBody AllClass allClass){
        return allClassService.deleteClass(allClass);
    }
//    根据班级id获取班级
    @RequestMapping("/tea/useIdGetClass")
    public R useIdGetClass(AllClass allClass){
        return allClassService.useIdGetClass(allClass);
    }
//    修改班级名称
    @PostMapping("/tea/useIdUpdeteClass")
    public R useIdUpdeteClass(@RequestBody AllClass allClass){
        return allClassService.useIdUpdeteClass(allClass);
    }
//    使用课程id获取选了该课程的班级名称
    @RequestMapping("/tea/useCourseIdGetClass")
    public  R useCourseIdGetClass(int id){
        return allClassService.useCourseIdGetClass(id);
    }
//    获取可选的班级
    @RequestMapping("/tea/useCourseIdGetOptionalClass")
    public R useCourseIdGetOptionalClass(int id){
                return allClassService.useCourseIdGetOptionalClass(id);
    }
//    插入选择班级的所有学生
    @RequestMapping("/tea/useClassIdLetCourseInsertStu")
    public R useClassIdLetCourseInsertStu(int classId,int courseId){

        return allClassService.useClassIdLetCourseInsertStu(classId,courseId);
    }
}
