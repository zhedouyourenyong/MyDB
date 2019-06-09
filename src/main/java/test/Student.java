package test;


import db.annotation.AFCOLUMNS;
import db.annotation.AFTABLE;

@AFTABLE(name="student")
@AFCOLUMNS(auto=true, generated="id")
public class Student
{
	int id;
	String name;
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	
}
