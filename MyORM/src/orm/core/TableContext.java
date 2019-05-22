package orm.core;

import orm.bean.ColumnInfo;
import orm.bean.TableInfo;
import orm.utils.JavaFileUtils;
import orm.utils.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 16114 on 2019/5/18.
 * 负责获取数据库的表结构和类结构的关系，并可以根据表结构生成类结构
 */
public class TableContext {
    /**
     * 表名为key，表信息对象为value
     */
    public static Map<String,TableInfo> tables = new HashMap<String, TableInfo>();
    /**
     * 将po的class对象和表信息对象关联起来 便于重用
     */
    public static Map<Class,TableInfo> poClassTableMap = new HashMap<Class, TableInfo>();
    private TableContext(){}

    static {
        try {
            //初始化获得表信息
            Connection connection = DBManager.createConnection();
            //元数据包含了表中所有信息
            DatabaseMetaData dbmd = connection.getMetaData();
            ResultSet tableRet = dbmd.getTables(null, "%", "%", new String[]{"TABLE"});
            while (tableRet.next()) {

                String tableName = (String) tableRet.getObject("TABLE_NAME");
                TableInfo tableInfo = new TableInfo(
                        tableName,  //表名
                        new HashMap<String, ColumnInfo>(), //字段名-字段信息
                        new ArrayList<ColumnInfo>()); //联合主键
                tables.put(tableName, tableInfo);
                ResultSet set = dbmd.getColumns(null, "%", tableName, "%"); //查询表中和的所有字段

                while (set.next()) {
                    ColumnInfo columnInfo = new ColumnInfo(
                            set.getString("COLUMN_NAME"),
                            set.getString("TYPE_NAME"),
                            0); //普通键
                    tableInfo.getColumns().put(set.getString("COLUMN_NAME"), columnInfo);
                }
                ResultSet set1 = dbmd.getPrimaryKeys(null, "%", tableName);

                while (set1.next()) {
                    ColumnInfo columnInfo1 = (ColumnInfo) tableInfo.getColumns().get(set1.getObject("COLUMN_NAME"));
                    columnInfo1.setKeyType(1); //设为主键类型
                    tableInfo.getPriKeys().add(columnInfo1);
                }
                //取唯一主键..方便使用,如果是联合主键则为空！
                if (tableInfo.getPriKeys().size() > 0) {
                    tableInfo.setOnlyPriKey(tableInfo.getPriKeys().get(0));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //更新类结构
         //updatePoFile();
        //加载PO包下所有类，便于重用，提高效率!!!
        loadTablePoToMap();
    }

    /**
     * 根据数据库表结构生成pojo类!!!
     * 项目启动时调用
     */
     public static void updatePoFile(){
         Map<String, TableInfo> map = TableContext.tables;
         for (TableInfo t: map.values()) {
             JavaFileUtils.createJavaFile(t, new MysqlConvertorHandler());
         }
    }

    /**
     * 加载完数据库中的表，生成pojo之后，把对应的类和对应的表关联起来
     * 比如：通过class对象找表结构TableInfo
     */
    public static void loadTablePoToMap(){
        for (TableInfo tableInfo: tables.values()) {
            Class<?> aClass = null;
            try {
                aClass = Class.forName(
                        DBManager.getConfiguration().getPackageName()
                +"."+ StringUtils.UpFirstString(tableInfo.getName()));
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
            poClassTableMap.put(aClass,tableInfo);
        }
    }

   /* public static void main(String[] args) {
        Map<String,TableInfo> tables = TableContext.tables;
        System.out.println(tables);
    }*/
}
