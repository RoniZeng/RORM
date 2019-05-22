package orm.core;

import orm.bean.ColumnInfo;
import orm.bean.TableInfo;
import orm.utils.JdbcUtils;
import orm.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 16114 on 2019/5/18.
 * 负责查询，对外提供服务的核心类
 * 模板方法模式：采用模板方法模式将JDBC操作封装成模板，便于重用
 * callback回调
 */
//abstract
public class Query implements MysqlQuery{

    @Override
    public int executeDML(String sql, Object[] params) {
        Connection connection = DBManager.getConnection();
        int cnt = 0;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            //给sql设参
            JdbcUtils.handlerParams(ps,params);
            System.out.println(ps);
            cnt = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DBManager.close(connection,ps);
        }
        return cnt;
    }


    @Override
    public void insert(Object obj) {
        //obj-->表中 insert into 表名 (id,uname,pwd) values (?,?,?)
        Class c = obj.getClass();
        //存储sql参数对象
        List<Object> params = new ArrayList<Object>();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);

        StringBuilder sql = new StringBuilder("insert into " + tableInfo.getName() + " (");
        int countNotNullField = 0; //计算不为空的属性值
        Field[] fields = c.getDeclaredFields();//遍历所有属性
        for (Field f: fields) {
            String fieldName = f.getName(); //属性名
            Object fieldValue = ReflectUtils.invokeGet(obj,fieldName);
            if (fieldValue != null){
                countNotNullField++;
                sql.append(fieldName + ",");
                params.add(fieldValue);
            }
        }
        sql.setCharAt(sql.length()-1,')');
        sql.append(" values (");
        for (int i = 0; i < countNotNullField; i++) {
            sql.append("?,");
        }
        sql.setCharAt(sql.length()-1,')');
        System.out.println(sql.toString());
        executeDML(sql.toString(),params.toArray());
    }


    @Override
    public void delete(Class clazz, Object id) {
        //Emp.class,2-->delete from emp where id=2;
        //通过class对象找表结构TableInfo
        TableInfo tableInfo = TableContext.poClassTableMap.get(clazz);
        //获得主键
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
        //
        String sql = "delete from " + tableInfo.getName()
                + " where " +
                onlyPriKey.getName() + "=?";
        executeDML(sql,new Object[]{id});
    }

    @Override
    public void delete(Object obj) {
        //获得clazz
        Class c = obj.getClass();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        //获得主键
        ColumnInfo onlyPriKey = tableInfo.getOnlyPriKey();
        //通过反射机制调用对应的get方法或set方法 getUser()
        Object priKeyValue = ReflectUtils.invokeGet(obj,onlyPriKey.getName());
        delete(c,priKeyValue);
    }

    @Override
    public int update(Object obj, String[] fieldNames) {
        //obj{"uname","pwd"} --> update 表名 set uname=?,pwd=?  where id=?
        Class c = obj.getClass();
        //存储sql参数对象
        List<Object> params = new ArrayList<Object>();
        TableInfo tableInfo = TableContext.poClassTableMap.get(c);
        ColumnInfo priKey = tableInfo.getOnlyPriKey(); //获得唯一主键
        StringBuilder sql =
                new StringBuilder("update " + tableInfo.getName() + " set ");
        for (String fname:fieldNames) {
            Object fvalue = ReflectUtils.invokeGet(obj,fname);
            params.add(fvalue);
            sql.append(fname + "=?,");
        }
        sql.setCharAt(sql.length()-1,' ');
        sql.append("where ");
        sql.append(priKey.getName() + "=? ");

        params.add(ReflectUtils.invokeGet(obj,priKey.getName()));
        System.out.println(sql);
        return executeDML(sql.toString(),params.toArray());
    }

    @Override
    public List queryRows(String sql, Class clazz, Object[] params) {
        Connection connection = DBManager.getConnection();
        PreparedStatement ps = null;
        List<Object> rows = new ArrayList<Object>();
        ResultSet resultSet = null;
        try {
            ps = connection.prepareStatement(sql);
            JdbcUtils.handlerParams(ps, params);
            resultSet = ps.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                Object o = clazz.newInstance();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    //得到每一列的名称
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    Object columnValue = resultSet.getObject(i + 1);
                    ReflectUtils.invokeSet(o, columnLabel, columnValue);
                }
                rows.add(o);
            }
            return rows;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBManager.close(connection,ps);
        }
        return null;
    }

    @Override
    public Object queryUniqueRow(String sql, Class clazz, Object[] params) {
        List list = queryRows(sql,clazz,params);
        return (list == null && list.size() > 0) ? null : list.get(0);
    }

    @Override
    public Object queryValue(String sql, Object[] params) {
        //select count(*) from user...
        Connection connection = DBManager.getConnection();
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Object o =null;
        try {
            ps = connection.prepareStatement(sql);
            JdbcUtils.handlerParams(ps,params);
            resultSet = ps.executeQuery();
            while (resultSet.next()){
                o = resultSet.getObject(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            DBManager.close(connection,ps);
        }
        return o;
    }

    @Override
    public Number queryNumber(String sql, Object[] params) {
        return (Number) queryValue(sql,params);
    }

}
