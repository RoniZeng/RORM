package orm.test;

import orm.core.QueryFactory;

/**
 * Created by 16114 on 2019/5/20.
 */
public class Test {
    public static void main(String[] args) {
        long l1 = System.currentTimeMillis();
        for (int i=0;i<1000;i++) {
            String sql = "SELECT name FROM product_ WHERE id=?";
            //Object o = new Query().queryValue(sql,new Object[]{1}); //9225
            Object o = QueryFactory.createQuery().queryValue(sql, new Object[]{1}); //5900
            //System.out.println(o);
        }
        long l2 = System.currentTimeMillis();
        System.out.println(l2-l1);
    }
}
