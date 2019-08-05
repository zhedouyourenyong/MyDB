package framework.utils;

import framework.annotation.COLUMN;
import framework.annotation.COLUMNS;
import framework.annotation.TABLE;
import framework.tableMapping.TableInfo;

import java.lang.reflect.Field;

public class AnnotationUtil
{

    public static void parse (TableInfo tableInfo, Class clazz) throws Exception
    {
        tableInfo.name = clazz.getName();
        tableInfo.clazz = clazz;

        if(clazz.isAnnotationPresent(TABLE.class))
        {
            // 如果类中有TABLE, COLUMNS, COLUMN注解，则根据注解来解析
            parseWithAnnotation(tableInfo, clazz);
        } else
        {
            //没有TABLE注解，当只当作普通类操作，只得到getter和setter
            parseWithoutAnnotation(tableInfo, clazz);
        }
    }

    private static void parseWithAnnotation (TableInfo tableInfo, Class clazz)
    {
        TABLE table = (TABLE) clazz.getAnnotation(TABLE.class);
        tableInfo.table = table.tableName();

        if(clazz.isAnnotationPresent(COLUMNS.class))
        {
            COLUMNS columns = (COLUMNS) clazz.getAnnotation(COLUMNS.class);
            boolean auto = columns.auto();
            String generated = columns.generated();
            if(auto)
            {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields)
                {
                    TableInfo.Property p = new TableInfo.Property();
                    tableInfo.properties.add(p);
                    p.name = field.getName();
                    p.type = field.getType().getName();

                    if(p.name.equals(generated))
                        tableInfo.generatedKey = generated;

                    tableInfo.getters.put(p.name, ReflectUtil.findGetter(clazz, p.name));
                    tableInfo.setters.put(p.name, ReflectUtil.findSetter(clazz, p.name));
                }
            }
        } else
        {
            // 分别提取列名
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields)
            {
                if(field.isAnnotationPresent(COLUMN.class))
                {
                    COLUMN a = (COLUMN) field.getAnnotation(COLUMN.class);

                    TableInfo.Property p = new TableInfo.Property();
                    tableInfo.properties.add(p);

                    p.name = a.name();
                    p.type = a.type();
                    if(a.generated())
                    {
                        tableInfo.generatedKey = a.name();
                    }

                    tableInfo.getters.put(p.name, ReflectUtil.findGetter(clazz, p.name));
                    tableInfo.setters.put(p.name, ReflectUtil.findSetter(clazz, p.name));
                }
            }
        }
    }

    // 即使类里没有注解，也可以解析, 但无法得到表名
    private static void parseWithoutAnnotation (TableInfo tableInfo, Class clazz)
    {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields)
        {
            TableInfo.Property p = new TableInfo.Property();
            tableInfo.properties.add(p);
            p.name = field.getName();
            p.type = field.getType().getName();

            tableInfo.getters.put(p.name, ReflectUtil.findGetter(clazz, p.name));
            tableInfo.setters.put(p.name, ReflectUtil.findSetter(clazz, p.name));
        }
    }
}
