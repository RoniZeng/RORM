package orm.core;

/**
 * Created by 16114 on 2019/5/18.
 * 负责根据配置文件 管理query对象
 */
public class QueryFactory {


    private static QueryFactory queryFactory = new QueryFactory(); //单例模式
    private QueryFactory(){} //私有构造器
    private static Class c;

    static {
        if (DBManager.getConfiguration().getUseDB().equalsIgnoreCase("mysql")){
            try{
                c = Class.forName("orm.core.Query"); //加载指定的query类
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        //将对应的类和表封装起来
        TableContext.loadTablePoToMap();
    }

    public static Query createQuery(){
        try{
            return (Query) c.newInstance();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
