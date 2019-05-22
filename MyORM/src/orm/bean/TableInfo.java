package orm.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by 16114 on 2019/5/18.
 *  封装表信息
 *      表名
 *      字段信息
 *      主键
 */
public class TableInfo {
    private String name; //表名
    private Map<String,ColumnInfo> columns; //字段信息，字段名-字段信息
    private ColumnInfo onlyPriKey; //主键信息
    private List<ColumnInfo> priKeys; //联合主键


    public TableInfo(String name, Map<String, ColumnInfo> columns, List<ColumnInfo> priKeys) {
        this.name = name;
        this.columns = columns;
        this.priKeys = priKeys;
    }

    public List<ColumnInfo> getPriKeys() {
        return priKeys;
    }

    public void setPriKeys(List<ColumnInfo> priKeys) {
        this.priKeys = priKeys;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, ColumnInfo> columns) {
        this.columns = columns;
    }

    public ColumnInfo getOnlyPriKey() {
        return onlyPriKey;
    }

    public void setOnlyPriKey(ColumnInfo onlyPriKey) {
        this.onlyPriKey = onlyPriKey;
    }

    public TableInfo() {
    }

    public TableInfo(String name, Map<String, ColumnInfo> columns, ColumnInfo onlyPriKey) {
        this.name = name;
        this.columns = columns;
        this.onlyPriKey = onlyPriKey;
    }
}
