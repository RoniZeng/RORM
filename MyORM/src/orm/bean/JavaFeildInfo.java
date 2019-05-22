package orm.bean;

/**
 * Created by 16114 on 2019/5/18.
 * 封装 java 类单一的属性，get set 方法
 */
public class JavaFeildInfo {
    /**
     * 属性的源码信息 private String feild
     * private int userId;
     */
    private String feildInfo;
    /**
     * 封装get方法,如 public int getUserId(){}
     */
    private String getFeildInfo;
    /**
     * 封装set方法,如 public int setUserId(){this.id=id}
     */
    private String setFeildInfo;

    @Override
    public String toString() {
        System.out.println(feildInfo);
        System.out.println(getFeildInfo);
        System.out.println(setFeildInfo);
        return super.toString();
    }

    public String getFeildInfo() {
        return feildInfo;
    }

    public void setFeildInfo(String feildInfo) {
        this.feildInfo = feildInfo;
    }

    public String getGetFeildInfo() {
        return getFeildInfo;
    }

    public void setGetFeildInfo(String getFeildInfo) {
        this.getFeildInfo = getFeildInfo;
    }

    public String getSetFeildInfo() {
        return setFeildInfo;
    }

    public void setSetFeildInfo(String setFeildInfo) {
        this.setFeildInfo = setFeildInfo;
    }

    public JavaFeildInfo() {
    }

    public JavaFeildInfo(String feildInfo, String getFeildInfo, String setFeildInfo) {
        this.feildInfo = feildInfo;
        this.getFeildInfo = getFeildInfo;
        this.setFeildInfo = setFeildInfo;
    }


}
