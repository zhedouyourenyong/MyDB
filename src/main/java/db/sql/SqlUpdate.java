package db.sql;

import db.utils.AfSql;

import java.util.ArrayList;
import java.util.List;

public class SqlUpdate
{
	String table = "";
	
	List<String> sss = new ArrayList<>();
	
	public SqlUpdate (String table)
	{
		this.table = table;
	}
	
	public SqlUpdate add(String expr)
	{
		sss.add( expr );
		return this;
	}
	
	public SqlUpdate add(String name, String value)
	{
		sss.add( AfSql.name(name) + "=" + AfSql.value(value));
		return this;
	}
	
	////////////////////////////////
	public SqlUpdate add2(String name, String value)
	{
		add(name, value);
		return this;
	}	
	public SqlUpdate add2(String name, Integer value)
	{
		add(name, value.toString());
		return this;
	}
	public SqlUpdate add2(String name, Long value)
	{
		add(name, value.toString());
		return this;
	}
	public SqlUpdate add2(String name, Short value)
	{
		add(name, value.toString());
		return this;
	}
	public SqlUpdate add2(String name, Boolean value)
	{
		add(name, value?"1":"0");
		return this;
	}	
	
	@Override
	public String toString()
	{
		String sql = " UPDATE " + AfSql.name(table) 
			+ " SET ";

		for(int i=0; i<sss.size(); i++)
		{
			if(i > 0) sql += ",";
			sql += sss.get(i);
		}	
		sql += " ";
		
		return sql;
	}
}
