package orm.core;

/**
 * Created by 16114 on 2019/5/18.
 */
public class MysqlConvertorHandler implements TypeConvertorHandler{

    //varchar——>String
    @Override
    public String JdbcType2JavaType(String javaTypeData) {
        if ("varchar".equalsIgnoreCase(javaTypeData) || "char".equalsIgnoreCase(javaTypeData)){
            return "String";
        }
        else if ("int".equalsIgnoreCase(javaTypeData)
            ||"tinyint".equalsIgnoreCase(javaTypeData)
            ||"smallint".equalsIgnoreCase(javaTypeData)
            ||"integer".equalsIgnoreCase(javaTypeData)
        ) {
            return "Integer";
        }
        else if ("bigint".equalsIgnoreCase(javaTypeData)) {
            return "Long";
        }
        else if ("double".equalsIgnoreCase(javaTypeData) || "float".equalsIgnoreCase(javaTypeData)) {
            return "Double";
        }
        else if ("clob".equalsIgnoreCase(javaTypeData)){
            return "CLob";
        }
        else if ("blob".equalsIgnoreCase(javaTypeData)){
            return "BLob";
        }
        else if ("date".equalsIgnoreCase(javaTypeData)){
            return "Date";
        }
        else if ("time".equalsIgnoreCase(javaTypeData)){
            return "Time";
        }
        else if ("timestamp".equalsIgnoreCase(javaTypeData)){
            return "Timestamp";
        }
        return null;
    }

    @Override
    public String JavaType2JdbcType(String jdbcTypeData) {
        return null;
    }
}
