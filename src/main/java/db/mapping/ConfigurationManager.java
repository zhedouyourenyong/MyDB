package db.mapping;


import db.utils.AnnotationUtil;
import db.utils.ReflectUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 存储映射关系
 * */
public class ConfigurationManager
{
    private List<Configuration> sqlPojoList = new ArrayList<>();
    private Map<String, Configuration> nameMapping = new HashMap<>();  //根据类名寻找SqlPojo
    private Map<String, Configuration> tableMapping = new HashMap<>(); //根据表名寻找SqlPojo

    public static ConfigurationManager i;

    private ConfigurationManager ()
    {
    }

    static
    {
        i = new ConfigurationManager();
        try
        {
            i.load();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // 加载sql-mapping.xml
    private void load() throws Exception
    {
        InputStream in=Thread.currentThread().getContextClassLoader().getResourceAsStream("sql-mapping.xml");
        if(in==null)
            return ;

        SAXReader xmlReader=new SAXReader();
        Document x_doc = xmlReader.read(in);
        Element x_root = x_doc.getRootElement();
        in.close();

        List<Element> x_classes = x_root.elements("class");
        for(Element x_class : x_classes)
        {
            Configuration asc = new Configuration();
            asc.name = x_class.attributeValue("name");
            asc.table = x_class.attributeValue("table");
            asc.clazz = Class.forName(asc.name);

            List<Element> x_props = x_class.elements("property");
            for(Element x_prop : x_props)
            {
                Configuration.Property prop = new Configuration.Property();
                prop.name = x_prop.attributeValue("name");
                prop.type = x_prop.attributeValue("type");

                asc.properties.add(prop);
                asc.getters.put( prop.name,  ReflectUtil.findGetter(asc.clazz, prop.name));
                asc.setters.put( prop.name,  ReflectUtil.findSetter(asc.clazz, prop.name));
            }
            addClass(asc);
        }
    }

    private void addClass(Configuration asc)
    {
        sqlPojoList.add(asc);
        nameMapping.put(asc.name,asc);
        tableMapping.put(asc.table,asc);
    }

    // 查找类描述
    public Configuration findClassByClassName(String className)
    {
        if(className != null)
        {
            Configuration result = nameMapping.get(className);
            if(result != null)
                return result;
        }
        return null;
    }

    public Configuration findClassByTableName(String tableName)
    {
        if(tableName != null)
        {
            Configuration result = tableMapping.get(tableName);
            if(result != null)
                return result;
        }
        return null;
    }

    public Configuration findClassByAnnotation(Class clazz) throws Exception
    {
        Configuration sqlPojo=new Configuration();
        AnnotationUtil.parse(sqlPojo,clazz);
        addClass(sqlPojo);
        return sqlPojo;
    }

}
