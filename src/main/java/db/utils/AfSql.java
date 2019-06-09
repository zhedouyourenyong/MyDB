package db.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/* 通用工具类
 * 
 */
public class AfSql
{
	public static boolean autoEscape = true; // 默认对sql里的 value自动转义
	public static boolean enableLog = false; // 是否显示控制台打印辅助调试
	
	
	public static void log(String message)
	{
		if(enableLog)
			System.out.println(message);
	}
	
	public static Date now()
	{
		return new Date();
	}
	
	// 名字加反引号
	public static String name(String str)
	{
		if(str.indexOf('.')>=0) return str; // "db.table.column"
		if(str.indexOf('`')>=0) return str;
		return "`" + str + "`";		
	}
	
	// 值加单引号
	public static String value(String str)
	{
		if(autoEscape) str = escape(str); // 转义
		//if(str.startsWith("\'")) return str;
		return "'" + str + "'";
	}
	
	// SimpleDateFormat不支持线程重入, 所以临时创建
	public static String date(Date d)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(d);
	}
	// SimpleDateFormat不支持线程重入, 所以临时创建
	public static String datetime(Date d)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}
	public static String date()
	{
		return date(new Date());
	}
	public static String datetime()
	{
		return datetime(new Date());
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
	
	public static String className4Table(String table)
	{
		String[] sss = table.split("_");
		String className = "";
		for(String s: sss )
		{
			StringBuffer sb = new StringBuffer(s);
			char ch = sb.charAt(0);
			sb.setCharAt(0, Character.toUpperCase(ch));
			
			className += sb.toString();
		}
		return className;
	}
}
