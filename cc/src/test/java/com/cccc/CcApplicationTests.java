package com.cccc;

import com.cccc.mapper.AllClassMapper;
import com.cccc.mapper.CourseTotalMapper;
import com.cccc.mapper.UserMapper;
import com.cccc.pojo.AllClass;
import com.cccc.pojo.CourseTotal;
import com.cccc.pojo.User;
import com.cccc.utils.Jwt_Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;


@SpringBootTest
class CcApplicationTests {
    @Autowired(required=false)
    private UserMapper userMapper;

    @Autowired(required=false)
    private AllClassMapper allClassMapper;

    @Autowired(required=false)
    private CourseTotalMapper courseTotalMapper;
    @Test
    void contextLoads() {
//        List<AllClass> allClasses = allClassMapper.useCourseIdGetClass(1);
//        allClasses.forEach(System.out::println);
        HashMap<String, Object> map = new HashMap<>();
        map.put("courseId",1);
        map.put("classId",1);

        List<CourseTotal> courseTotals = courseTotalMapper.useCourseIdAndClassIdGetUserAndClassAndTotla(map);
        courseTotals.forEach(System.out::println);


    }

}
