package db.utils;

import db.annotation.AFCOLUMN;
import db.annotation.AFCOLUMNS;
import db.annotation.AFTABLE;
import db.mapping.Configuration;

import java.lang.reflect.Field;

public class AnnotationUtil
{

    public static void parse(Configuration object, Class clazz) throws Exception
    {
        object.name = clazz.getName();
        object.clazz = clazz;

        if( clazz.isAnnotationPresent(AFTABLE.class))
        {
            // 如果类中有AFTABLE, AFCOLUMNS, AFCOLUMN注解，则根据注解来解析
            parseWithAnnotation(object,clazz );
        }
        else
        {
            //没有AFTABLE注解，当只当作普通类操作，只得到getter和setter
            parseWithoutAnnotation(object,clazz);
//            throw new Exception("类" + clazz.getName() + "里没有AFTABLE注解!");
        }
    }

    private static void parseWithAnnotation(Configuration object, Class clazz)
    {
        // 提取 AFTABLE 注解，得到表名
        AFTABLE table = (AFTABLE)clazz.getAnnotation(AFTABLE.class);
        object.table = table.name();

        // 提取 AFCOLUMNS 注解，得到所有列名
        if(clazz.isAnnotationPresent(AFCOLUMNS.class))
        {
            AFCOLUMNS a = (AFCOLUMNS)clazz.getAnnotation(AFCOLUMNS.class);
            boolean auto = a.auto();
            String names = a.names();
            String generated = a.generated();
            if(auto)
            {
                Field[] fields = clazz.getDeclaredFields();
                for(Field field :fields)
                {
                    Configuration.Property p = new Configuration.Property();
                    object.properties.add( p );
                    p.name = field.getName();
//                    p.type = "varchar";
                    p.type=field.getType().getName();

                    if(p.name.equals(generated))
                        object.generatedKey = generated;

                    object.getters.put( p.name,  ReflectUtil.findGetter(clazz, p.name));
                    object.setters.put( p.name,  ReflectUtil.findSetter(clazz, p.name));
                }
            }
        }
        else
        {
            // 分别提取列名
            Field[] fields = clazz.getDeclaredFields();
            for(Field field :fields)
            {
                if(field.isAnnotationPresent(AFCOLUMN.class))
                {
                    AFCOLUMN a = (AFCOLUMN) field.getAnnotation(AFCOLUMN.class);

                    Configuration.Property p = new Configuration.Property();
                    object.properties.add( p );

                    p.name = a.name();
                    p.type = a.type();
                    if( a.generated() )
                    {
                        object.generatedKey = a.name();
                    }

                    object.getters.put( p.name,  ReflectUtil.findGetter(clazz, p.name));
                    object.setters.put( p.name,  ReflectUtil.findSetter(clazz, p.name));
                }
            }
        }
    }

    // 即使类里没有注解，也可以解析, 但无法得到表名
    private static void parseWithoutAnnotation(Configuration object, Class clazz)
    {
        // 列名 <-> 字段名 自动匹配
        Field[] fields = clazz.getDeclaredFields();
        for(Field field :fields)
        {
            Configuration.Property p = new Configuration.Property();
            object.properties.add( p );

            // 提取出每一列
            p.name = field.getName();
//          p.type="varchar";
            p.type =field.getType().getName();

            object.getters.put( p.name,  ReflectUtil.findGetter(clazz, p.name));
            object.setters.put( p.name,  ReflectUtil.findSetter(clazz, p.name));
        }
    }
}
