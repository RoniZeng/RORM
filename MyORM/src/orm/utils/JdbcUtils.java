package orm.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by 16114 on 2019/5/18.
 *  * 用于处理jdbc相关的操作
 */
public class JdbcUtils {

    /**
     * 给 sql 语句设值
     * @date 2019/05/18 14:50
     * @param ps
     * @param params
     * @return void
     */
    public static void handlerParams(PreparedStatement ps, Object params[]){
        if (params != null){
            for (int i=0; i<params.length; i++){
                try {
                    ps.setObject(1+i, params[i]);
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
