package orm.utils;

import java.lang.reflect.Method;

/**
 * Created by 16114 on 2019/5/18.
 * 用于反射相关的操作
 */
public class ReflectUtils {
    /**
     * 通过反射调用某个实体类的属性的get方法
     * @date 2019/5/18
     * @param object    class对象
     * @param feildName 属性名称
     * @return java.lang.Object
     */
    public static Object invokeGet(Object object, String feildName){
        try {
            //Object 类中的 getClass( ) 方法将会返回一个 Class 类型的实例
            Class<?> aClass = object.getClass();
            //get方法（将字符串第一个字符大写，并去掉其中的下划线）
            Method method = aClass.getMethod("get" + StringUtils.UpFirstString(feildName),null);
            return method.invoke(object, null);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 通过反射调用属性的set方法设值
     * @date 2019/5/18
     * @param object
     * @param columnLabel
     * @param columnValue
     * @return java.lang.Object
     */
    public static Object invokeSet(Object object, String columnLabel, Object columnValue){
        try {
            if (columnValue != null){
                Class<?> clazz = object.getClass();
                //clazz.getSuperclass().getMethod("set" + StringUtils.UpFirstString(columnLabel), columnValue.getClass());
                Method method = clazz.getMethod("set" + StringUtils.UpFirstString(columnLabel),columnValue.getClass());
                return method.invoke(object,columnValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

