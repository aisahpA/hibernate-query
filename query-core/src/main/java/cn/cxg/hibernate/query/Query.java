package cn.cxg.hibernate.query;

import org.hibernate.FetchMode;
import org.hibernate.criterion.Junction.Nature;
import org.hibernate.sql.JoinType;

import java.util.*;

/**
 * 查询对象
 *
 * @author chenxianguan 2015年12月2日
 */
public class Query {
    /**
     * 当前查询对象的class
     */
    private Class<?> domainClass;
    /**
     * Fetch抓取对象
     */
    private Fetch fetch;
    /**
     * 条件对象集合
     */
    private List<WebQueryCriteria> querys = new ArrayList<>();
    /**
     * 排序对象集合
     */
    private LinkedHashMap<String, String> orders;


    /**
     * 构建查询对象
     *
     * @param domainClass 当前查询对象的class
     */
    public Query(Class<?> domainClass) {
        this.domainClass = domainClass;
    }


    /**
     * 设置Fetch的字段
     *
     * @param fields 单独查询的字段
     */
    public Query setFetch(String... fields) {
        this.fetch = new Fetch(fields);
        return this;
    }

    /**
     * 添加关联方式
     *
     * @param fieldName 字段属性
     * @param joinTypes 关联方式
     */
    public Query addJointype(String fieldName, JoinType joinTypes) {
        if (null == this.fetch) {
            this.fetch = new Fetch();
        }
        this.fetch.getJoinTypes().put(fieldName, joinTypes);
        return this;
    }

    /**
     * 添加抓取方式
     *
     * @param fieldName 字段属性
     * @param mode      及时获取还是懒加载
     */
    public Query addFetchMode(String fieldName, FetchMode mode) {
        if (null == this.fetch) {
            this.fetch = new Fetch();
        }
        this.fetch.getFetchModes().put(fieldName, mode);
        return this;
    }

    /**
     * 往条件集合中追加条件对象
     *
     * @param condition 查询条件
     */
    public Query addCondition(WebQueryCriteria condition) {
        if (null == querys) {
            this.querys = new ArrayList<>();
        }
        this.querys.add(condition);
        return this;
    }

    /**
     * 往条件集合中追加条件对象.
     * 此方法适合添加没有条件值的查询
     *
     * @param fieldName 查询条件字段名
     * @param whereType 查询条件类型
     */
    public Query addCondition(String fieldName, Integer whereType) {
        this.addCondition(new WebQueryCriteria(fieldName, whereType));
        return this;
    }

    /**
     * 往条件集合中追加条件对象
     *
     * @param fieldName  查询条件字段名
     * @param compare    查询条件类型{@link WebQueryCriteria}
     * @param fieldValue 查询条件值
     */
    public Query addCondition(String fieldName, Integer compare, Object fieldValue) {
        this.addCondition(new WebQueryCriteria(fieldName, compare, fieldValue));
        return this;
    }

    /**
     * 往条件集合中追加条件对象.
     * 此方法只目前只适用于in查询:
     * {@link WebQueryCriteria#joint_in},
     * {@link WebQueryCriteria#joint_notin}
     *
     * @param fieldName   查询条件字段名
     * @param compare     查询条件类型{@link WebQueryCriteria}}
     * @param fieldValues 查询条件范围值
     */
    public Query addCondition(String fieldName, Integer compare, Object[] fieldValues) {
        this.addCondition(new WebQueryCriteria(fieldName, compare, fieldValues));
        return this;
    }

    /**
     * 往条件集合中追加条件对象.
     * 此方法只目前只适用于in查询:
     * {@link WebQueryCriteria#joint_in},
     * {@link WebQueryCriteria#joint_notin}
     *
     * @param fieldName   查询条件字段名
     * @param compare     查询条件类型{@link WebQueryCriteria}}
     * @param fieldValues 查询条件范围值
     */
    public Query addCondition(String fieldName, Integer compare, Collection fieldValues) {
        this.addCondition(new WebQueryCriteria(fieldName, compare, fieldValues));
        return this;
    }

    /**
     * 往条件集合中追加条件对象.
     * 此方法目前适用于between查询  :
     * {@link WebQueryCriteria#joint_between},
     * {@link WebQueryCriteria#joint_notbetween}
     *
     * @param fieldName   查询条件字段名
     * @param compare     查询条件类型 {@link WebQueryCriteria}
     * @param fieldValue1 查询条件起点值
     * @param fieldValue2 查询条件终点值
     */
    public Query addCondition(String fieldName, Integer compare, Object fieldValue1, Object fieldValue2) {
        this.addCondition(new WebQueryCriteria(fieldName, compare, fieldValue1, fieldValue2));
        return this;
    }

    /**
     * 往条件集合中追加条件对象.
     * 此方法目前适用于多个字段使用同一个like值的查询  :
     * {@link WebQueryCriteria#joint_like}
     *
     * @param fieldNames   多个字段的名称数组
     * @param likeValue    匹配的Like值
     * @param junctionType 这几个字段对like的连接方式
     * @since create chenxianguan 2016年2月3日 下午1:43:53
     */
    public Query addLikeCondition(String[] fieldNames, String likeValue, Nature junctionType) {

        if (likeValue == null || "".equals(likeValue)) {
            return this;
        }

        List<WebQueryCriteria> likeQuerys = new ArrayList<>();
        for (String fieldName : fieldNames) {
            likeQuerys.add(new WebQueryCriteria(fieldName, WebQueryCriteria.joint_like, likeValue));
        }

        this.addCondition(new WebQueryCriteria(likeQuerys, junctionType));
        return this;
    }

    /**
     * 往条件集合中追加条件对象.
     * 此方法目前适用于多个字段使用同一个like值的查询  :
     * {@link WebQueryCriteria#joint_like}.
     * 默认各条件之间使用OR（{@link Nature#OR}）连接
     *
     * @param fieldNames 多个字段的名称数组
     * @param likeValue  匹配的Like值
     * @see #addLikeCondition(String[], String, Nature)
     * @since create chenxianguan 2016年2月3日 下午1:43:53
     */
    public Query addLikeCondition(String[] fieldNames, String likeValue) {

        this.addLikeCondition(fieldNames, likeValue, Nature.OR);
        return this;
    }

    /**
     * 往排序对象中追加排序对象
     * 按照添加的顺序排序
     *
     * @param fieldName 排序字段名
     * @param direct    ASC or DESC
     */
    public Query addOrder(String fieldName, String direct) {
        if (null == this.orders) {
            this.orders = new LinkedHashMap<>();
        }
        this.orders.put(fieldName, direct);
        return this;
    }

    public Class<?> getDomainClass() {
        return domainClass;
    }

    public void setDomainClass(Class<?> domainClass) {
        this.domainClass = domainClass;
    }

    public Fetch getFetch() {
        return fetch;
    }

    public List<WebQueryCriteria> getQuerys() {
        return querys;
    }

    public void setQuerys(List<WebQueryCriteria> querys) {
        this.querys = querys;
    }

    public LinkedHashMap<String, String> getOrders() {
        return orders;
    }

    public void setOrders(LinkedHashMap<String, String> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Query [domainClass=" + domainClass + ", fetch=" + fetch
                + ", querys=" + querys + ", orders=" + orders + "]";
    }


}
