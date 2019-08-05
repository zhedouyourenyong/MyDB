package framework.utils.sql;

import java.util.ArrayList;
import java.util.List;

public class SqlWhere
{
	ArrayList<String> conditions = new ArrayList<String>();
	

	
	// 加入单个查询条件
	public SqlWhere add(String condition)
	{
		condition = condition.trim();
		if( !condition.startsWith("("))
		{
			condition= "(" + condition + ")" ;
		}
		
		conditions.add(condition);
		return this;
	}

	// 加入有操作符的表达式
	public SqlWhere add(String name, String op, Object value)
	{
		 add( SqlUtil.name(name) + op + SqlUtil.value(value.toString()));
		 return this;
	}
	
	/////////////////////////////////
	// 字符串值 比较
	public SqlWhere add2(String name, String value)
	{
		add (name, "=", value);
		return this;
	}
	
	// 数值 比较
	public SqlWhere add2(String name, Long value)
	{
		add (name, "=", value);
		return this;
	}
	
	// 数值 比较
	public SqlWhere add2(String name, Integer value)
	{
		add (name, "=", value);
		return this;
	}
	
	// 数值 比较
	public SqlWhere add2(String name, Short value)
	{
		add (name, "=", value);
		return this;
	}
	
	public SqlWhere add2(String name, Boolean value)
	{
		add (name, "=", value ? "1" :"0");
		return this;
	}
	
	////////////////////////////////
	
	// LIKE 
	public SqlWhere addLike(String name, String value)
	{
		//conditions.add( colName + " LIKE '%" + colValue + "%'");
		// 把中间的空格替换成%通配符
		String filter = value.replace('　', '%').replace(' ', '%').replace('，', '%'); // 中文空格
		add (name, " LIKE ", filter);
		return this;
	}
	
	// IN	
	//  WHERE id IN ('07922860270B47FDB02ADD231F885DAB', '08C74DB5EBC64F439BB747EECBEC3862')
	public SqlWhere addIn(String name, List values)
	{
		if(values.size() == 0) return this;
		
		String sql =  SqlUtil.name(name) + " IN (";
		
		for(int i=0; i<values.size(); i++)
		{
			String s = SqlUtil.value( values.get(i).toString());
			sql += s;
			if(i != values.size() - 1) sql += ",";
		}
		
		sql += ") ";
		add(sql);
		return this;
	}
	
	public SqlWhere addIn(String name, Object[] values)
	{
		if(values.length == 0) return this;
		
		String sql = SqlUtil.name(name) + "IN (";
		for(int i=0; i<values.length; i++)
		{
			String a = values[i].toString().trim();
			if(a.length() == 0) continue;
			
			String s = SqlUtil.value(a);
			sql += s;
			if(i != values.length - 1) sql += ",";
		}
		
		sql += ")";
		add(sql);
		return this;
	}	
	
	@Override
	public String toString()
	{
		if(conditions.size() == 0) return " ";
		
		String where = " WHERE ";
		for(int i=0; i<conditions.size() ;i++)
		{
			if(i != 0) where += " AND ";
			where += conditions.get(i) ;
		}
		return where;
	}
	
}
