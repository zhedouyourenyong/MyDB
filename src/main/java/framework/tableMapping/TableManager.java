package framework.tableMapping;


import framework.config.ConfigConstant;
import framework.utils.AnnotationUtil;
import framework.utils.ReflectUtil;
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
public class TableManager
{
    private List<TableInfo> sqlPojoList = new ArrayList<>();
    private Map<String, TableInfo> nameMapping = new HashMap<>();  //根据类名寻找SqlPojo
    private Map<String, TableInfo> tableMapping = new HashMap<>(); //根据表名寻找SqlPojo

    private static volatile TableManager INSTANCE;

    private TableManager ()
    {
    }

    public static TableManager getInstance ()
    {
        if(INSTANCE == null)
        {
            synchronized (TableManager.class)
            {
                INSTANCE = new TableManager();
                try
                {
                    INSTANCE.load();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return INSTANCE;
    }

    // 加载sql-tableMapping.xml
    private void load () throws Exception
    {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(ConfigConstant.DEFAULT_CONFIG_FILR);

        if(in == null)
            return;

        SAXReader xmlReader = new SAXReader();
        Document x_doc = xmlReader.read(in);
        Element x_root = x_doc.getRootElement();
        in.close();

        List<Element> x_classes = x_root.elements("class");
        for (Element x_class : x_classes)
        {
            TableInfo asc = new TableInfo();
            asc.name = x_class.attributeValue("tableName");
            asc.table = x_class.attributeValue("table");
            asc.clazz = Class.forName(asc.name);

            List<Element> x_props = x_class.elements("property");
            for (Element x_prop : x_props)
            {
                TableInfo.Property prop = new TableInfo.Property();
                prop.name = x_prop.attributeValue("tableName");
                prop.type = x_prop.attributeValue("type");

                asc.properties.add(prop);
                asc.getters.put(prop.name, ReflectUtil.findGetter(asc.clazz, prop.name));
                asc.setters.put(prop.name, ReflectUtil.findSetter(asc.clazz, prop.name));
            }
            addClass(asc);
        }
    }

    private void addClass (TableInfo asc)
    {
        sqlPojoList.add(asc);
        nameMapping.put(asc.name, asc);
        tableMapping.put(asc.table, asc);
    }

    // 查找类描述
    public TableInfo findTableInfoByClassName (String className)
    {
        if(className != null)
        {
            TableInfo result = nameMapping.get(className);
            return result;
        }
        return null;
    }

    public TableInfo findTableInfoByTableName (String tableName)
    {
        if(tableName != null)
        {
            TableInfo result = tableMapping.get(tableName);
            return result;
        }
        return null;
    }

    public TableInfo findTableInfoByAnnotation (Class clazz) throws Exception
    {
        TableInfo sqlPojo = new TableInfo();
        AnnotationUtil.parse(sqlPojo, clazz);
        addClass(sqlPojo);
        return sqlPojo;
    }

}
