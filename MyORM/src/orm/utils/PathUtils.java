package orm.utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by 16114 on 2019/5/18.
 * 根据包名生成文件夹
 */
public class PathUtils {
    /**
     * 根据包名，生成包目录
     * @date 2019/5/18
     * @param packageName 包名
     * @return java.lang.String  包所在目录
     */

    public static String getFilePathFromPackage(String packageName){
        String str = packageName; //"jk.zmn.auto.dfd";
        str = str.replace(".","\\");
        File file = new File("");
        String canonicalPath = null;
        try {
            canonicalPath = file.getCanonicalPath();
        }catch (IOException e){
            e.printStackTrace();
        }
        canonicalPath += "\\src\\" + str;
//        String resource = Thread.currentThread().getContextClassLoader().getResource("/").getPath();
//        resource = resource.substring(1);
//        System.out.println(resource);
        File packageFile = new File(canonicalPath);
        if (!packageFile.exists()){
            packageFile.mkdirs();
        }
        return canonicalPath;
    }

    public static void main(String[] args) {
        System.out.println(PathUtils.getFilePathFromPackage("orm.test"));
    }

}
