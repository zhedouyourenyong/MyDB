package test;


import db.session.SqlSessionFactory;

import java.util.Map;

public class test
{
    public static void main (String[] args) throws Exception
    {
        Student stu=new Student();
        stu.setName("jjjj");
//        SqlSessionFactory.insert(stu);
        String sql="select * from student";
//        JSONArray array=SqlSessionFactory.executeQuery2JSONArray(sql);
//        System.out.println(array);
        Map<String,Object> res= SqlSessionFactory.getAsMap(sql);
        System.out.println(res);

    }
}
