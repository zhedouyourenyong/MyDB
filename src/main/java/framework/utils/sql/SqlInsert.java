package framework.utils.sql;

import java.util.ArrayList;
import java.util.List;

public class SqlInsert
{
	String table = "";
	
	List<String> names = new ArrayList<>();
	List<String> values =  new ArrayList<>();
	
	public SqlInsert (String table)
	{
		this.table = table;
	}
	
	// 不提供列名，则SQL里只写值，不写列名
	public SqlInsert add(String value)
	{
		values.add(value);
		return this;
	}
	
	// 提供列名和值
	public SqlInsert add(String name, String value)
	{
		names.add(name);
		values.add(value);
		return this;
	}

	@Override
	public String toString()
	{
		String column = "";
		if(names.size() > 0)
		{
			if(names.size() != values.size())
				return "SQL拼写出错! 列和值个数不一致!";
			
			column = "(";
			for(int i=0; i<names.size(); i++)
			{
				String name = names.get(i);
				if(i > 0)
					column += ",";
				column += SqlUtil.name( name );
			}
			column += ")";
		}
		
		String value = "";
		if(values.size() > 0)
		{
			value = "(";
			for(int i=0; i<values.size(); i++)
			{
				String str = values.get(i);
				if(i > 0) value += ",";
				value += SqlUtil.value( str );
			}
			value += ")";
		}
		
		String sql = " INSERT INTO " + SqlUtil.name(table) + column + " VALUES " + value;
		return sql;
	}
}
