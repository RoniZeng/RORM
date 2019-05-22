package orm.core;

import java.util.List;

/**
 * 负责针对 MySQL 的查询
 * Created by 16114 on 2019/5/18.
 */
@SuppressWarnings("all")
public interface MysqlQuery{

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
    public Object queryValue(String sql, Object[] params);//传入sql，出来对象

    /**
     * 查询返回一个数字（count(*)）（一行一列），并将该值返回
     * @param sql   查询语句
     * @param params    sql的参数
     * @return  查询到的结果
     */
    public  Number queryNumber(String sql, Object[] params);//传入sql，出来对象

//15375-->1752
  /*  public static void main(String[] args){

            Product p = new Product();
            p.setId(2);
            p.setName("小米");
            p.setPrice(1255.26);
        new MysqlQuery().insert(p);
        /*
        long l1 = System.currentTimeMillis();
        for (int i=0;i<1000;i++) {
            String sql = "select name from product_ where id=?";
            Object o = new MysqlQuery().queryValue(sql,new Object[]{1});
            //Object o = QueryFactory.createQuery().queryValue(sql, new Object[]{1});
           // System.out.println(o);
        }
       long l = System.currentTimeMillis();
        System.out.println(l-l1);
         //JavaFileUtils.createJavaFileToPackage();

    }*/


    /*public static void main(String[] args) {
        Product product = new Product();
        new MysqlQuery().delete(Product.class,3);
/*
       Object o = new MysqlQuery().
                queryValue("select count(*) from product_ where price>?",
                        new Object[]{1});
        System.out.println(o);
        product.setName("思渺");
        product.setPrice(1054.00);
        product.setId(3);
        List<Product> pl = new MysqlQuery()
                .queryRows("select id,name,price from product_ where price > ?"
                        ,Product.class,
                        new Object[]{100});
        for (Product p : pl
             ) {
            System.out.println(p.getName());
        }
    }
*/
}
