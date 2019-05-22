package orm.core;

/**
 * Created by 16114 on 2019/5/18.
 * 负责java数据类型和数据库数据类型的互相转化
 */
public interface TypeConvertorHandler {
    /**
     *   java数据类型转换成数据库数据类型
     * @date 2018/6/15 17:10
     * @param javaTypeData  java数据类型
     * @return java.lang.String   数据库数据类型
     */
    public String JavaType2JdbcType(String javaTypeData);

    /**
     *  数据库数据类型转换成java数据类型
     * @date 2018/6/15 17:09
     * @param jdbcTypeData  数据库类型
     * @return java.lang.String   java类型
     */
    public String JdbcType2JavaType(String jdbcTypeData);

}
