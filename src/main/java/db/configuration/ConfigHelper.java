package db.configuration;

import db.utils.PropsUtil;

import java.util.Properties;

public class ConfigHelper
{
    private static final Properties CONFIG_PROPS = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);

    public static String getValue(String key)
    {
       if(CONFIG_PROPS.contains(key))
       {
           return CONFIG_PROPS.getProperty(key);
       }
       return null;
    }
}
