package framework.utils;

public class StringUtil
{
    public static String toGetter (String fieldName)
    {
        // "tableName" -> "getName()"
        char firstChar = Character.toUpperCase(fieldName.charAt(0));
        StringBuffer strbuf = new StringBuffer("get" + fieldName);
        strbuf.setCharAt(3, firstChar);
        return strbuf.toString();
    }

    public static String[] toGetter (String[] fieldName)
    {
        String[] result = new String[fieldName.length];
        for (int i = 0; i < fieldName.length; i++)
        {
            result[i] = toGetter(fieldName[i]);
        }
        return result;
    }

    public static String toSetter (String fieldName)
    {
        // "tableName" -> "setName()"
        char firstChar = Character.toUpperCase(fieldName.charAt(0));
        StringBuffer strbuf = new StringBuffer("set" + fieldName);
        strbuf.setCharAt(3, firstChar);
        return strbuf.toString();
    }

    public static String[] toSetter (String[] fieldName)
    {
        String[] result = new String[fieldName.length];
        for (int i = 0; i < fieldName.length; i++)
        {
            result[i] = toSetter(fieldName[i]);
        }
        return result;
    }
}
