# 思想介绍 & 架构介绍

我们希望设计一个可以实现对象和 SQL 自动映射的框架，但是整体用法和设计比 Hibernate 简单，砍掉不必要的功能。

会穿插使用设计模式。

增删改：对象——>sql

查询：	sql——>对象

* 增加：将对象对应成 sql 语句，执行 sql，插入数据库中
* 删除：根据对象主键的值，生成 sql，执行，从库中删除

* 修改：根据对象需要修改属性的值，生成 sql，执行
* 查询：
  * 根据结果分类
    * 多行多列：List<JavaBean>
    * 一行多列：JavaBean
    * 一行一列：
      * 普通对象：Object
      * 数字：Number
* 核心架构
  * Query 接口：负责查询（对外提供服务的核心类）
  * QueryFacory 类：负责根据配置信息创建 query 对象
  * TypeConvertor 接口：负责类型转换
  * TableContext 类：负责获取管理数据库所有表结构和类接管的关系，并可以根据表结构生成类结构
  * DBManager 类：根据配置信息，维持连接对象的管理（增加连接池功能）
* 工具类
  * JDBCUtils 封装常用 JDBC 操作
  * StringUtils 封装常用字符串操作
  * JavaFileUtils 封装 Java 文件操作
  * ReflectUtils 封装常用反射操作
* 核心 Bean，封装相关数据
  * ColumnInfo：封装表中一个字段的信息（字段类型、字段名、键类型）
  * Configuration：封装配置文件信息
  * TableInfo：封装一张表的信息
* 针对 ORM 框架的说明
  * 配置文件：目前使用资源文件，后期复杂使用可增加 xml文件配置和注解
  * 类名由表名生成，只有首字母大写有区别，其他无区别eg：user——>User
  * Java 对象的属性由表中字段生成，完全对应
  * 目前，只支持表中只有一个主键，联合主键不支持

1. 在 src 下建立 db.properties。

2. 每张表只能有一个主键。不能处理多个主键的情况。
3. po 尽量使用包装类，不要使用基本数据类型。



```java
 /**
     * 直接执行一个DML语句
     * @param sql   sql语句
     * @param params    参数
     * @return int  执行sql语句后影响记录的行数
     */
    public int executeDML(String sql, Object[] params);

    /**
     * 将一个对象存储到数据库中
     * @param obj
     */
    public void insert(Object obj);

    /**
     * 删除clazz表示类对应的表中的记录（指定主键值id的记录）
     * @param clazz 和表对应的类的Class对象
     * @param id    主键值
     */
    public void delete(Class clazz, Object id); //delete from User where id=2;

    /**
     * 删除对象在数据库中对应的记录（对象所在的类对应到表，对象的主键值对应到记录）
     * @param obj
     */
    public void delete(Object obj);

    /**
     * 更新对象对应的记录，并且只更新指定字段值
     * @param obj   所要更新的对象
     * @param fieldNames    更新的属性列表
     * @return  执行sql语句后影响记录的行数
     */
    public int update(Object obj, String[] fieldNames);
    //update User set uname=?,pwd=?

    /**
     * 查询返回多行记录，并将每行记录封装到clazz指定的类对象中
     * @param sql   查询语句
     * @param clazz 封装数据的JavaBean类的Class对象
     * @param params    sql的参数
     * @return  查询到的结果
     */
    public List queryRows(String sql, Class clazz, Object[] params);//传入sql，出来对象


    /**
     * 查询返回一行记录，并将该记录封装到clazz指定的类对象中
     * @param sql   查询语句
     * @param clazz 封装数据的JavaBean类的Class对象
     * @param params    sql的参数
     * @return  查询到的结果
     */
    public Object queryUniqueRow(String sql, Class clazz, Object[] params);//传入sql，出来对象


    /**
     * 查询返回一个值（一行一列），并将该值返回
     * @param sql   查询语句
     * @param params    sql的参数
     * @return  查询到的结果
     */
    public  Object queryValue(String sql, Object[] params);//传入sql，出来对象

    /**
     * 查询返回一个数字（count(*)）（一行一列），并将该值返回
     * @param sql   查询语句
     * @param params    sql的参数
     * @return  查询到的结果
     */
    public  Number queryNumber(String sql, Object[] params);//传入sql，出来对象

```

核心增删改查

```java
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
```

连接池（Connection Pool）

* 就是将 Connection 对象放入 List 中，反复重用
* 连接池的初始化
  * 实现放入多个连接对象
* 从连接池中取连接对象
  * 如果池中有可用连接，则将池中最后一个返回。同时，将该连接从池中 remove，表示正在使用。
  * 如果池中无可用连接，则创建一个新的。
* 关闭连接
  * 不是真正的关闭连接，而是将用完的连接放入池中。
* 市面上的连接池产品
  * DBCP
  * c3p0
  * proxool

