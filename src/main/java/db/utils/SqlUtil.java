package db.utils;

public class SqlUtil
{
    // 名字加反引号
    public static String name(String str)
    {
        if(str.indexOf('.')>=0)
            return str; // "db.table.column"
        if(str.indexOf('`')>=0)
            return str;
        return "`" + str + "`";
    }

    // 值加单引号
    public static String value(String str)
    {
         str = escape(str); // 转义
        return "'" + str + "'";
    }

    // 转义 替换 \ 和 '
    public static String escape(String sql)
    {
        StringBuffer sb = new StringBuffer(sql.length() * 2);
        for(int i=0; i<sql.length(); i++)
        {
            char ch = sql.charAt(i);
            if(ch=='\'' || ch == '\\')
            {
                sb.append('\\')	;
            }

            sb.append(ch);
        }
        return sb.toString();
    }
}
