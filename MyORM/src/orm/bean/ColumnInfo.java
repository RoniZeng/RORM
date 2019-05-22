package orm.bean;

/**
 * Created by 16114 on 2019/5/18.
 * 用于封装数据表中的一个字段的信息
 *      字段名
 *      字段类型
 *      字段键类型 0 普通键 /1 主键 /2 外键
 */
public class ColumnInfo { //id int 主键
    /**
     * 字段名
     */
    private String name;
    /**
     * 字段数据类型
     */
    private String dataType;
    /**
     * 字段键类型
     */
    private int keyType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

    public ColumnInfo(String name, String dataType, int keyType) {
        this.name = name;
        this.dataType = dataType;
        this.keyType = keyType;
    }
}
