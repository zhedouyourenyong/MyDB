package db.session;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import db.configuration.ConfigConstant;
import db.configuration.ConfigHelper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SqlSessionFactory
{
    private static ComboPooledDataSource c3p0Pool = new ComboPooledDataSource();


    public static SqlSession getConnection () throws Exception
    {
        return new SqlSession(c3p0Pool.getConnection());
    }

    public static void insert (Object pojo) throws Exception
    {
        SqlSession connection = getConnection();
        try
        {
            connection.insert(pojo);
        } finally
        {
            connection.close();
        }
    }

    public static void execute (String sql) throws Exception
    {
        SqlSession connection = getConnection();
        try
        {
            connection.execute(sql);
        } finally
        {
            connection.close();
        }
    }

    public static <T> T get (String sql, Class<T> clazz) throws Exception
    {
        SqlSession connection = getConnection();
        try
        {
            List<T> rows = executeQuery(sql, clazz);
            if(rows == null || rows.size() == 0)
            {
                return null;
            } else
            {
                return rows.get(0);
            }
        } finally
        {
            connection.close();
        }
    }

    public static JSONObject getAsJSON (String sql) throws Exception
    {
        SqlSession connection = getConnection();
        try
        {
            JSONArray rows = executeQuery2JSONArray(sql);
            if(rows == null || rows.size() == 0)
            {
                return null;
            } else
            {
                return rows.getJSONObject(0);
            }
        } finally
        {
            connection.close();
        }
    }

    public static Map getAsMap (String sql) throws Exception
    {
        SqlSession connection = getConnection();
        try
        {
            List rows = executeQuery2List(sql);
            if(rows == null || rows.size() == 0)
            {
                return null;
            } else
            {
                return (Map) rows.get(0);
            }
        } finally
        {
            connection.close();
        }
    }

    public static <T> List<T> executeQuery (String sql, Class<T> clazz) throws Exception
    {
        SqlSession connection = getConnection();
        try
        {
            return connection.executeQuery(sql, clazz);
        } finally
        {
            connection.close();
        }
    }

    public static JSONArray executeQuery2JSONArray (String sql) throws Exception
    {
        JSONArray result = new JSONArray();

        SqlSession connection = getConnection();
        try
        {
            ResultSet rs = connection.executeQuery(sql);

            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            int[] columnTypes = new int[numColumns];
            String[] columnLabels = new String[numColumns];
            for (int i = 0; i < numColumns; i++)
            {
                int columnIndex = i + 1;
                columnLabels[i] = rsmd.getColumnLabel(columnIndex);
                columnTypes[i] = rsmd.getColumnType(columnIndex);
            }

            while (rs.next())
            {
                JSONObject jrow = new JSONObject();
                result.add(jrow);

                for (int i = 0; i < numColumns; i++)
                {
                    String columnValue = rs.getString(i + 1);
                    if(columnValue == null) continue;

                    int type = columnTypes[i];
                    if(type == Types.TINYINT || type == Types.SMALLINT || type == Types.INTEGER || type == Types.BIGINT)
                    {
                        jrow.put(columnLabels[i], Long.valueOf(columnValue));
                    } else if(type == Types.DOUBLE || type == Types.FLOAT)
                    {
                        jrow.put(columnLabels[i], Double.valueOf(columnValue));
                    } else
                    {
                        jrow.put(columnLabels[i], columnValue);
                    }
                }
            }
            return result;
        } finally
        {
            connection.close();
        }
    }

    public static List executeQuery2List (String sql) throws Exception
    {
        List result = new ArrayList();

        SqlSession connection = getConnection();
        try
        {
            ResultSet rs = connection.executeQuery(sql);

            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            int[] columnTypes = new int[numColumns];
            String[] columnLabels = new String[numColumns];
            for (int i = 0; i < numColumns; i++)
            {
                int columnIndex = i + 1;
                columnLabels[i] = rsmd.getColumnLabel(columnIndex);
                columnTypes[i] = rsmd.getColumnType(columnIndex);
            }

            while (rs.next())
            {
                HashMap<String, Object> row = new HashMap<>();
                result.add(row);

                for (int i = 0; i < numColumns; i++)
                {
                    String columnValue = rs.getString(i + 1);
                    if(columnValue == null)
                    {
                        row.put(columnLabels[i], null);
                        continue;
                    }

                    int type = columnTypes[i];
                    Object value = columnValue;
                    if(type == Types.TINYINT)
                        value = Byte.valueOf(columnValue);
                    else if(type == Types.SMALLINT)
                        value = Short.valueOf(columnValue);
                    else if(type == Types.INTEGER)
                        value = Integer.valueOf(columnValue);
                    else if(type == Types.BIGINT)
                        value = Long.valueOf(columnValue);
                    else if(type == Types.DOUBLE)
                        value = Double.valueOf(columnValue);
                    else if(type == Types.FLOAT)
                        value = Float.valueOf(columnValue);

                    row.put(columnLabels[i], value);
                }
            }
            return result;
        } finally
        {
            connection.close();
        }
    }
}