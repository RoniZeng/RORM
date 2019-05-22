package orm.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 16114 on 2019/5/18.
 *
 */
public class DBPool {
    /**
     * 连接池对象
     */
    private List<Connection> pool;

    /**
     * 最大连接数
     */
    private static final int POOL_MAX_SIZE = DBManager.getConfiguration().getPoolMaxSize();
    /**
     * 最小连接池
     */
    private static final int POOL_MIN_SIZE = DBManager.getConfiguration().getPoolMinSize();


    /**
     * 初始化连接池，使池中的连接数达到最小值
     */
    public void initPool() {
        if(pool==null){
            pool = new ArrayList<Connection>();
        }

        while(pool.size()<DBPool.POOL_MIN_SIZE){
            pool.add(DBManager.createConnection());
            System.out.println("初始化池，池中连接数："+pool.size());
        }
    }


    /**
     * 从连接池中取出一个连接
     * @return
     */
    public synchronized Connection getConnection() {
        int last_index = pool.size()-1;
        Connection conn = pool.get(last_index);
        pool.remove(last_index);

        return conn;
    }

    /**
     * 将连接放回池中
     * @param conn
     */
    public synchronized void close(Connection conn){

        if(pool.size()>=POOL_MAX_SIZE){
            try {
                if(conn!=null){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            pool.add(conn);
        }
    }

    public DBPool() {
        this.initPool();
    }
}
