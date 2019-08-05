package framework.utils.sql;

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
		sss.add( SqlUtil.name(name) + "=" + SqlUtil.value(value));
		return this;
	}
	
	@Override
	public String toString()
	{
		String sql = " UPDATE " + SqlUtil.name(table)
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
