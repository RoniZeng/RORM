package orm.utils;

import orm.bean.ColumnInfo;
import orm.bean.Configuration;
import orm.bean.JavaFeildInfo;
import orm.bean.TableInfo;
import orm.core.DBManager;
import orm.core.MysqlConvertorHandler;
import orm.core.TableContext;
import orm.core.TypeConvertorHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by 16114 on 2019/5/18.
 * 生成 Java 源的工具类
 */
public class JavaFileUtils {

    private static Configuration configuration = DBManager.getConfiguration();

    public static JavaFeildInfo createJavaFeild(ColumnInfo columnInfo, TypeConvertorHandler convertorHandler) {
        //将字段数据类型转换成java数据类型
        String javaType = convertorHandler.JdbcType2JavaType(columnInfo.getDataType());
        String columnName = columnInfo.getName().toLowerCase();
        JavaFeildInfo feildInfo = new JavaFeildInfo();

        //生成属性语句 private String name;
        feildInfo.setFeildInfo("\tprivate " + javaType + " " + StringUtils.trimUnderLine(columnName) + ";\n");

        //生成get方法 public String getUsername(){return username;}
        StringBuilder sb = new StringBuilder();
        sb.append("\tpublic " + javaType + " " + "get" + StringUtils.UpFirstString(columnName) + "() {\n");
        sb.append("\t\treturn " + columnName + ";\n");
        sb.append("\t}\n");
        feildInfo.setGetFeildInfo(sb.toString());

        //生成set方法 public void setUsername(){this.username = username;}
        StringBuilder sb1 = new StringBuilder();
        sb1.append("\tpublic void " + "set" + StringUtils.UpFirstString(columnName)
                + "(" + javaType + " " + columnName + ") {\n");
        sb1.append("\t\t this." + columnName + " = " + columnName + ";\n");
        sb1.append("\t}\n");
        feildInfo.setSetFeildInfo(sb1.toString());
        return feildInfo;
    }

    /**
     * 根据表信息生成java类的源码
     * @param tableInfo 表信息
     * @param typeConvertorHandler  数据类型转换器
     * 生成 java 类的源码  StringBuilder sb = new StringBuilder();
     */
    public static void createJavaFile(TableInfo tableInfo, TypeConvertorHandler typeConvertorHandler){
        //得到所有的列信息
        StringBuilder sb = new StringBuilder();
        Map<String, ColumnInfo> columns = tableInfo.getColumns();
        ArrayList<JavaFeildInfo> javaFeildInfos = new ArrayList<JavaFeildInfo>();
        Collection<ColumnInfo> values = columns.values();
        //生成所有的 java 属性信息和 get/set 方法
        for (ColumnInfo c : values) {
            JavaFeildInfo info = createJavaFeild(c,typeConvertorHandler);
            javaFeildInfos.add(info);
        }
        //生成 package 语句
        sb.append("package " + configuration.getPackageName()+";\n\n");
        //生成 import 语句
        sb.append("import java.sql.*;\n");
        sb.append("import java.util.*;\n\n");
        //生成类声明语句
        sb.append("public class " + StringUtils.UpFirstString(tableInfo.getName())+" {\n\n");
        //生成属性列表
        for (JavaFeildInfo javaFeildInfo: javaFeildInfos){
            sb.append(javaFeildInfo.getFeildInfo());
        }
        sb.append("\n\n");
        //生成 get 方法列表
        for (JavaFeildInfo javaFeildInfo: javaFeildInfos){
            sb.append(javaFeildInfo.getGetFeildInfo());
        }
        //生成 set 方法列表
        for (JavaFeildInfo javaFeildInfo: javaFeildInfos){
            sb.append(javaFeildInfo.getSetFeildInfo());
        }
        //生成类结束
        sb.append("}\n");
        //System.out.println(sb.toString());
        //return sb.toString();
        //把字符串写入流

        String classInfo = sb.toString();
        String filePathFromPackage = PathUtils.getFilePathFromPackage(configuration.getPackageName());

        File file = new File(filePathFromPackage, StringUtils.UpFirstString(tableInfo.getName()) + ".java");
        System.out.println(file); /**打印file：  E:\maven\MyORM\src\orm\pojo\Userinfo.java**/
        BufferedOutputStream bufferedOutputStream=null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bufferedOutputStream.write(classInfo.getBytes(),0,classInfo.getBytes().length);
            bufferedOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("表" + tableInfo.getName()+"对应的类" + StringUtils.UpFirstString(tableInfo.getName()) + "已自动生成..");
    }

/*
    public static void createJavaPOFile(TableInfo tableInfo, TypeConvertorHandler convertor){
        String src  = createJavaFile(tableInfo, convertor);
        BufferedWriter bw = null;
        bw = new BufferedWriter(new FileWriter(""));
        try {
            bw.write(src);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
*/

    public static void createJavaFileToPackage(){
        Map<String, TableInfo> tables = TableContext.tables;
        TypeConvertorHandler convertorHandler = null;
        if (configuration.getUseDB().equalsIgnoreCase("mysql")){
            convertorHandler = new MysqlConvertorHandler();
        }
        for (TableInfo tableInfo:tables.values()){
            createJavaFile((tableInfo),convertorHandler);
        }
    }



    public static void main(String[] args) {
       /*
        ColumnInfo c = new ColumnInfo("id", "int", 0);
        JavaFeildInfo f = createJavaFeild(c,new MysqlConvertorHandler());
        System.out.println(f);
        */
       /* Map<String, TableInfo> map = TableContext.tables;
        for (TableInfo t: map.values()) {
            createJavaFile(t, new MysqlConvertorHandler());
        }*/
        //TableInfo t = map.get("userinfo");

    }

}
