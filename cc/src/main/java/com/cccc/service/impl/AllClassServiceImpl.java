package com.cccc.service.impl;

import com.cccc.common.R;
import com.cccc.mapper.AllClassMapper;
import com.cccc.mapper.CourseTotalMapper;
import com.cccc.mapper.UserMapper;
import com.cccc.pojo.AllClass;
import com.cccc.pojo.User;
import com.cccc.service.AllClassService;
import com.cccc.utils.Jwt_Utils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class AllClassServiceImpl implements AllClassService {
    @Autowired(required=false)
    private AllClassMapper allClassMapper;
    @Autowired(required=false)
    private UserMapper userMapper;
    @Autowired(required=false)
    private CourseTotalMapper courseTotalMapper;
    @Override
    public R getAllClass(String keyword) {
        List<AllClass> allClass = allClassMapper.getAllClass(keyword);
//        List<AllClass> classListTea = allClassMapper.getClassListTea(id);
        ArrayList<Long> longs = new ArrayList<>();
        for (int i=0;i<allClass.size();i++){
            Long aLong = userMapper.useClassIdGetUser(allClass.get(i));
            longs.add(aLong);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("c",allClass);
        map.put("sum",longs);
        return R.ok(map);
    }
//    获取管理的班级
    @Override
    public R getClassListTea(HttpServletRequest request) {
//        解析token获取id
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");
//        System.out.println("idididididiididididi"+id);
        List<AllClass> classListTea = allClassMapper.getClassListTea(id);
        ArrayList<Long> longs = new ArrayList<>();
        for (int i=0;i<classListTea.size();i++){
            Long aLong = userMapper.useClassIdGetUser(classListTea.get(i));
            longs.add(aLong);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("c",classListTea);
        map.put("sum",longs);
        return R.ok(map);
    }
//    添加班级
    @Override
    public R insertClass(HttpServletRequest request,AllClass allClass) {
//        解析token获取用户id
        String token = request.getHeader("token");
        Claims claims = Jwt_Utils.parseToken(token);
        int id = (int) claims.get("id");
        String role = (String) claims.get("role");
        System.out.println(id+":"+role);
        AllClass s = allClassMapper.ifExistClass(allClass);
        if(s!=null){
            return  R.error(505,"已存在该课程");
        }
//        如果是教师则需要设置班主任为自己，否则跳过
        if("1".equals(role)){
            allClass.setUserId(id);
        }



        return R.ok(allClassMapper.insertClass(allClass));
//        return null;
    }

//    删除班级
    @Override
    public R deleteClass(AllClass allClass) {
        int id = allClass.getId();
//        删除班级
        int i = allClassMapper.deleteClass(id);
//        删除班级的人员
        List<User> users = userMapper.useClassIdGetStu(id);
        for (int j=0;j<users.size();j++){
            courseTotalMapper.useUserIdDeleteCourseTotal(users.get(j).getId());
        }

        userMapper.useClassIdDeleteUser(id);
        return R.ok(i);
    }

    @Override
    public R useIdGetClass(AllClass allClass) {

        return R.ok(allClassMapper.useIdGetClass(allClass.getId()));
    }

//    修改班级
    @Override
    public R useIdUpdeteClass(AllClass allClass) {
        AllClass s = allClassMapper.ifExistClass(allClass);

        if(s!=null){
            return  R.error(505,"已存在该班级");
        }
        int i = allClassMapper.useIdUpdeteClass(allClass);
        return R.ok(i);
    }

    @Override
    public R useCourseIdGetClass(int id) {
        List<AllClass> allClasses = allClassMapper.useCourseIdGetClass(id);
        System.out.println("zxczxczxczxc");
        allClasses.forEach(System.out::println);

        if(allClasses.get(0)!=null && allClasses.size()>0){
            return R.ok(allClasses);
        }
        ArrayList<AllClass> allClasses1 = new ArrayList<>();
        AllClass allClass = new AllClass();
        allClass.setId(0);
        allClass.setClassname("暂无班级");
        allClasses1.add(allClass);
        return R.error(888,allClasses1,"无班级");

    }
    //    获取可选的班级
    @Override
    public R useCourseIdGetOptionalClass(int id) {
//        获取已选择的课程
        List<AllClass> allClasses = allClassMapper.useCourseIdGetClass(id);
//        System.out.println("aaaaaaaaaaaaaaaaaaaaa"+allClasses.size());
//        System.out.println(allClasses.get(0));

//        如果已选择的班级为空则返回所有班级
        if(allClasses.size()>0 && allClasses.get(0)!=null){
            ArrayList<Object> list = new ArrayList<>();
            for (int i=0;i<allClasses.size();i++){
                list.add(allClasses.get(i).getId());
            }
//            获取不包括已选班级的班级
            List<AllClass> allClasses1 = allClassMapper.useCourseIdGetOptionalClass(list);
            System.out.println("aaaaaaaaa");
            allClasses1.forEach(System.out::println);
            return R.ok(allClasses1);
        }
//        获取所有班级
        List<AllClass> allClass = allClassMapper.getAllClass(null);
        return R.ok(allClass);
    }

    //    插入选择班级的所有学生
    @Override
    public R useClassIdLetCourseInsertStu(int classId, int courseId) {
        List<User> users = userMapper.useClassIdGetStu(classId);
        if(users.size()>0 && users.get(0)!=null){
            ArrayList<Object> list = new ArrayList<>();
            for (int i=0;i<users.size();i++){
                list.add(users.get(i).getId());
            }
            HashMap<String, Object> map = new HashMap<>();
            map.put("list",list);
            map.put("courseId",courseId);
            int i = courseTotalMapper.useClassIdInsertUserIdAndTotla(map);
            return R.ok(i);
        }
        return R.error(888,"班级没有人员，请联系该班级的班主任！");
    }

}
