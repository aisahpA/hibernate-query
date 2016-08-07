package cn.cxg.hibernate.query;

import org.hibernate.FetchMode;
import org.hibernate.sql.JoinType;

import java.util.*;

/**
 * 基本抓取模式（第一实体类型的查询)
 *
 * @author chenxianguan 2015年12月2日
 */
public class Fetch {

    /**
     * 参与查询的字段名数组--(支持xxx.xxx.xxx)
     */
    private final List<String> fields = new ArrayList<>();

    /**
     * 关联实体的连接方式---默认为INNER_JOIN。
     * <关联实体名称， 连接方式>
     */
    private final Map<String, JoinType> joinTypes = new HashMap<>();

    /**
     * <pre>关联对象的抓取方式
     * 其中key值为关联对象的名称（可通过点号向下级关联）
     * key--The association path
     * value--The fetch mode to apply
     * </pre>
     */
    private final Map<String, FetchMode> fetchModes = new HashMap<>();


    /**
     * 构建抓取模式
     *
     * @param fields 待查询的字段名数组
     */
    public Fetch(String... fields) {
        if (null != fields && fields.length != 0) {
            this.fields.addAll(Arrays.asList(fields));
        }
    }


    public List<String> getFields() {
        return fields;
    }

    public Map<String, JoinType> getJoinTypes() {
        return joinTypes;
    }

    public Map<String, FetchMode> getFetchModes() {
        return fetchModes;
    }

    @Override
    public String toString() {
        return "Fetch [fields=" + fields + ", joinTypes=" + joinTypes
                + ", fetchModes=" + fetchModes + "]";
    }

}
