package framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class ReflectUtil
{
    private static final Logger logger = LoggerFactory.getLogger(ReflectUtil.class);


    public static Method findSetter (Class cls, String fieldName)
    {
        String methodName = StringUtil.toSetter(fieldName);
        Method[] methods = cls.getMethods();
        for (Method m : methods)
        {
            if(m.getName().equals(methodName))
            {
                return m;
            }
        }
        return null;
    }

    public static Method[] findSetter (Class cls, String[] labels)
    {
        labels = StringUtil.toSetter(labels);
        Method[] result = findMethods(cls, labels);
        return result;
    }

    public static Method findGetter (Class cls, String fieldName)
    {
        String methodName = StringUtil.toGetter(fieldName);
        Method[] methods = cls.getMethods();
        for (Method m : methods)
        {
            if(m.getName().equals(methodName))
            {
                return m;
            }
        }
        return null;
    }

    public static Method[] findGetter (Class cls, String[] labels)
    {
        labels = StringUtil.toGetter(labels);
        Method[] result = findMethods(cls, labels);
        return result;
    }

    public static Map<String, Method> findMethodsMap (Class cls, String[] methodNames)
    {
        Method[] methods = cls.getMethods();
        Map<String, Method> map = new HashMap<>(methods.length);
        for (int i = 0; i < methods.length; i++)
        {
            map.put(methods[i].getName(), methods[i]);
        }
        return map;
    }

    public static Method[] findMethods (Class cls, String[] methodNames)
    {
        Method[] result = new Method[methodNames.length];
        Map<String, Method> map = findMethodsMap(cls, methodNames);
        for (int i = 0; i < methodNames.length; i++)
        {
            result[i] = map.get(methodNames[i]);
        }
        return result;
    }

    public static Object newInstance (Class<?> cls)
    {
        Object instance;
        try
        {
            instance = cls.newInstance();
        } catch (Exception e)
        {
            logger.error("new instance failure", e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    public static Object invokeMethod (Object obj, Method method, Object... args)
    {
        Object result;
        try
        {
            method.setAccessible(true);
            result = method.invoke(obj, args);
        } catch (Exception e)
        {
            logger.error("invoke method failure", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public static void setField (Object obj, Field field, Object value)
    {
        try
        {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e)
        {
            logger.error("set field failure", e);
            throw new RuntimeException(e);
        }
    }
}