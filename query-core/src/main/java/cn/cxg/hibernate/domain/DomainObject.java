package cn.cxg.hibernate.domain;

import java.util.Objects;

/**
 * 实体对象的共同父类
 * <br/>它主要是重写了<tt>equals</tt>和<tt>hashCode</tt>方法：直接根据主键id进行判断
 * <br/>需要注意的是，当主键发生改变，比如存放到数据库自动产生主键值后，它的equals和hashCode将发生改变
 * ，而导致依赖这两个方法的行为发生奇怪的变化，尤其是在使用集合时需特别注意
 *
 * @author chenxianguan 2016/8/7
 */
public abstract class DomainObject implements IDomainObject {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(this.getClass().getName().equals(o.getClass().getName()))) {
            return false;
        }
        DomainObject other = (DomainObject) o;
        return Objects.equals(getPrimaryKey(), other.getPrimaryKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrimaryKey());
    }

    @Override
    public String toString() {
        return getClass().getName() + "#" + getPrimaryKey();
    }

}
