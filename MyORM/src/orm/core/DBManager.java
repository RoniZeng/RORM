package orm.core;

import orm.bean.Configuration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by 16114 on 2019/5/18.
 * 根据配置信息，维持连接对象的管理
 */
public class DBManager {

    private static Configuration configuration;
    private static DBPool pool;

    /**
     * 初始化configuration
     */
    static {
        Properties properties = new Properties();

        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));

            configuration = new Configuration();
            //根据properties构建configuration
            configuration.setDriver(properties.getProperty("driver"));
            configuration.setPackageName(properties.getProperty("packageName"));
            configuration.setPassword(properties.getProperty("password"));
            configuration.setUrl(properties.getProperty("url"));
            configuration.setUseDB(properties.getProperty("useDB"));
            configuration.setUsername(properties.getProperty("username"));
            configuration.setPoolMinSize(Integer.parseInt(properties.getProperty("poolMinSize")));
            configuration.setPoolMaxSize(Integer.parseInt(properties.getProperty("poolMaxSize")));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }

    }
    /**
     * 得到数据库链接
     * @param
     * @return java.sql.Connection
     */

    public static Connection getConnection(){
        if (pool == null){
            pool = new DBPool();
        }
        return pool.getConnection();
    }


    /**
     *创建数据库连接
     */
    public static Connection createConnection(){

        try {
            Class.forName(configuration.getDriver());

            return DriverManager.getConnection(configuration.getUrl(),
                    configuration.getUsername(),configuration.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 关闭数据库连接
     * @param connection
     * @param preparedStatement
     * @return void
     */
    public static void close(Connection connection, PreparedStatement preparedStatement){
        if (connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement!=null){
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取配置信息
     * @return configuration
     */
    public static Configuration getConfiguration() {

        return configuration;
    }

}

