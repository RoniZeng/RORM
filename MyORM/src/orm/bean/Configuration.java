package orm.bean;

/**
 * Created by 16114 on 2019/5/18.
 * 核心配置类，同于存储配置文件的信息
 */
public class Configuration {
    /**
     * 声明驱动
     */
    private String driver; // com.mysql.jdbc.Driver
    /**
     * 声明数据库用户名
     */
    private String username; //root
    /**
     * 密码
     */
    private String password; //123456
    /**
     * 正在使用的数据库
     */
    private String useDB; //mysql
    /**
     * 访问数据库
     */
    private String url; //jdbc:mysql://localhost:3306/test

    /**
     * 生成的pojo所在的包
     */
    private String packageName; // orm.pojo
    /**
     * 连接池最小链接数
     */
    private int poolMinSize;
    /**
     * 连接池最大链接数
     */
    private int poolMaxSize;

    public Configuration(String driver, String username, String password, String useDB, String url, String packageName) {
        this.driver = driver;
        this.username = username;
        this.password = password;
        this.useDB = useDB;
        this.url = url;
        this.packageName = packageName;
    }

    public Configuration() {
    }

    public Configuration(String driver, String username, String password, String useDB, String url, String packageName, int poolMinSize, int poolMaxSize) {
        this.driver = driver;
        this.username = username;
        this.password = password;
        this.useDB = useDB;
        this.url = url;
        this.packageName = packageName;
        this.poolMinSize = poolMinSize;
        this.poolMaxSize = poolMaxSize;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUseDB() {
        return useDB;
    }

    public void setUseDB(String useDB) {
        this.useDB = useDB;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getPoolMinSize() {
        return poolMinSize;
    }

    public void setPoolMinSize(int poolMinSize) {
        this.poolMinSize = poolMinSize;
    }

    public int getPoolMaxSize() {
        return poolMaxSize;
    }

    public void setPoolMaxSize(int poolMaxSize) {
        this.poolMaxSize = poolMaxSize;
    }
}
