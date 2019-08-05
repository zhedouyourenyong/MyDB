package framework.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface COLUMNS
{
	boolean auto () default false; // 是否自动从field中提取
	String generated () default ""; // 自增主键的列名
}
