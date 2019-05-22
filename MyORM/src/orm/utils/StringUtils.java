package orm.utils;

/**
 * Created by 16114 on 2019/5/18.
 * 用来字符串相关操作
 */
public class StringUtils {

    /**
     * abcd——>ABCD——>A——>Abcd
     *  将字符串第一个字符大写，并去掉其中的下划线
     * @date 2019/5/18
     * @param s  待处理的字符串
     * @return java.lang.String 操作之后的
     */
    public static String UpFirstString(String s){
        s = s.replace("_","");
        s = s.substring(0,1).toUpperCase()+s.substring(1);
        return s;
    }

    /**
     * 去掉字符串中的下划线
     * @date 2019/5/18
     * @param s  待处理的字符串
     * @return java.lang.String 操作之后的
     */
    public static String trimUnderLine(String s){
        return s.replace("_","");
    }

}
