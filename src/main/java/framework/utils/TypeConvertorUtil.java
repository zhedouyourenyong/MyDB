package framework.utils;

import java.lang.reflect.Method;
import java.sql.Types;
import java.text.SimpleDateFormat;

public class TypeConvertorUtil
{
    public static void map (Object pojo, Method method, int columnType, String columnValue) throws Exception
    {
        SimpleDateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf_t = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf_dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println("列类型: " + columnType + ", 值: " + columnValue);
        if(method == null)
            return;
        if(columnValue == null)
            return;

        // 根据类型，传递合适的参数给setter
        Object arg0 = null;
        if(columnType == Types.CHAR || columnType == Types.VARCHAR)
        {
            arg0 = columnValue; // 给方法传一个String参数
        } else if(columnType == Types.LONGVARCHAR)  //text
        {
            arg0 = columnValue;
        } else if(columnType == Types.BIT) // tinyint(1)
        {
            arg0 = (columnValue.equals("1"));
        } else if(columnType == Types.DATE) // date
        {
            arg0 = sdf_d.parse(columnValue);
        } else if(columnType == Types.TIME) // time
        {
            arg0 = sdf_t.parse(columnValue);
        } else if(columnType == Types.TIMESTAMP) // datetime timestamp
        {
            arg0 = sdf_dt.parse(columnValue);
        } else if(columnType == Types.REAL) // SQLite里的REAL类型,对应double或float
        {
            Class[] parameterTypes = method.getParameterTypes();
            Class c = parameterTypes[0];
            if(c.equals(double.class) || c.equals(Double.class))
            {
                arg0 = Double.valueOf(columnValue);
            } else if(c.equals(float.class) || c.equals(Float.class))
            {
                arg0 = Float.valueOf(columnValue);
            }
        } else if(columnType == Types.TINYINT || columnType == Types.SMALLINT
                || columnType == Types.INTEGER || columnType == Types.BIGINT
                || columnType == Types.DOUBLE || columnType == Types.FLOAT)
        {
            // 整数类型的处理
            Class[] parameterTypes = method.getParameterTypes();
            Class c = parameterTypes[0];
            if(c.equals(int.class) || c.equals(Integer.class))
            {
                arg0 = Integer.valueOf(columnValue);
            } else if(c.equals(long.class) || c.equals(Long.class))
            {
                arg0 = Long.valueOf(columnValue);
            } else if(c.equals(short.class) || c.equals(Short.class))
            {
                arg0 = Short.valueOf(columnValue);
            } else if(c.equals(byte.class) || c.equals(Byte.class))
            {
                arg0 = Byte.valueOf(columnValue);
            } else if(c.equals(float.class) || c.equals(Float.class))
            {
                arg0 = Float.valueOf(columnValue);
            } else if(c.equals(double.class) || c.equals(Double.class))
            {
                arg0 = Double.valueOf(columnValue);
            }
        }

        // 调用setter方法
        Object args[] = {arg0};
        try
        {
            method.invoke(pojo, args);
        } catch (IllegalArgumentException e)
        {
            Class[] parameterTypes = method.getParameterTypes();
            Class c = parameterTypes[0];
            System.out.println("期望类型:" + c.getCanonicalName() + "，实际类型:" + arg0.getClass().getCanonicalName());
            e.printStackTrace();
        }
    }

    public static void setIntValue (Object pojo, Method setter, String value) throws Exception
    {
        Object arg0 = null;

        // 整数类型的处理
        Class[] parameterTypes = setter.getParameterTypes();
        Class c = parameterTypes[0];
        if(c.equals(int.class) || c.equals(Integer.class))
        {
            arg0 = Integer.valueOf(value);
        } else if(c.equals(long.class) || c.equals(Long.class))
        {
            arg0 = Long.valueOf(value);
        } else if(c.equals(short.class) || c.equals(Short.class))
        {
            arg0 = Short.valueOf(value);
        } else if(c.equals(byte.class) || c.equals(Byte.class))
        {
            arg0 = Byte.valueOf(value);
        }

        Object args[] = {arg0};
        try
        {
            if(arg0 != null) setter.invoke(pojo, args);
        } catch (IllegalArgumentException e)
        {
            //System.out.println("期望类型:" + c.getCanonicalName() + "，实际类型:" + arg0.getClass().getCanonicalName() );
            e.printStackTrace();
        }
    }

}
