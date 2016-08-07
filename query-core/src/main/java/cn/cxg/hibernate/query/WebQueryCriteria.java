package cn.cxg.hibernate.query;

import org.hibernate.criterion.Junction.Nature;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 查询参数封装类
 * 
 * @author chenxianguan  2015年12月4日
 *
 */
public class WebQueryCriteria implements Serializable {

	protected String fieldName = null; // 查询条件的字段名称
	protected Object fieldValue = null; // 查询条件的值
	protected Object fieldValue1 = null; // 查询条件1，比如遇到between查询类型时就有这个值
	protected Object[] fieldValues = null; // 当查询条件用in时，使用数组传值
	protected int whereType = 0; // 查询条件的查询类型，可选值定义在下面，如joint_eq
	
	/**
	 * 外层条件连接方式，默认为AND
	 */
	protected Nature junctionType = Nature.AND;
	/**
	 * 联合条件内部之间的连接方法，默认为AND
	 */
	protected Nature subJunctionType = Nature.AND;
	/**
	 * 目前用于存放联合条件
	 */
	protected List<WebQueryCriteria> subWebQuerys = null;

	public WebQueryCriteria() {
	}
	
	public WebQueryCriteria(String fieldName, int whereType) {
		this.fieldName = fieldName;
		this.whereType = whereType;
	}

	public WebQueryCriteria(String fieldName, int whereType, Object fieldValue) {
		this.fieldName = fieldName;
		this.whereType = whereType;
		this.fieldValue = fieldValue;
	}
	
	public WebQueryCriteria(String fieldName, int whereType, Object[] fieldValues) {
		this.fieldName = fieldName;
		this.whereType = whereType;
		this.fieldValues = fieldValues;
	}
	
	public WebQueryCriteria(String fieldName, int whereType, Collection fieldValues) {
		this.fieldName = fieldName;
		this.whereType = whereType;
		this.fieldValues = fieldValues.toArray(new Object[fieldValues.size()]);
	}

	public WebQueryCriteria(String fieldName, int whereType, Object fieldValue, Object fieldValue1) {
		this.fieldName = fieldName;
		this.whereType = whereType;
		this.fieldValue = fieldValue;
		this.fieldValue1 = fieldValue1;
	}
	
	/**
	 * 使用联合查询（默认以AND连接）
	 * @param subWebQuerys 合并的下级查询
	 */
	public WebQueryCriteria(List<WebQueryCriteria> subWebQuerys){
		this.subWebQuerys = subWebQuerys;
	}
	
	/**
	 * 使用联合查询
	 * @param subWebQuerys 合并的下级查询
	 * @param subJunctionType 下级条件之间的连接方式
	 */
	public WebQueryCriteria(List<WebQueryCriteria> subWebQuerys, Nature subJunctionType){
		this.subWebQuerys = subWebQuerys;
		this.subJunctionType = subJunctionType;
	}
	

	public String getFieldName() {
		return fieldName;
	}

	/**
	 * 用于Sql条件表达式的条件字段名
	 * @param fieldName 字段名
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	/**
	 * 用于sql条件表达式的值-1
	 * @param fieldValue 查询值
	 */
	public void setFieldValue(Object fieldValue) {

		this.fieldValue = fieldValue;
	}

	public Object getFieldValue1() {
		return fieldValue1;
	}

	/**
	 * 用于sql条件表达式的值-2
	 * @param fieldValue1 查询值
	 */
	public void setFieldValue1(Object fieldValue1) {
		this.fieldValue1 = fieldValue1;
	}

	public int getWhereType() {
		return whereType;
	}

	/**
	 * 用于sql条件表达式的运算符
	 * @param whereType 条件方式
	 */
	public void setWhereType(int whereType) {
		this.whereType = whereType;
	}

	public Object[] getFieldValues() {
		return fieldValues;
	}

	/**
	 * 用于sql条件表达式的值-3 多个值
	 * @param fieldValues 多个值的数组
	 */
	public void setFieldValues(Object[] fieldValues) {
		this.fieldValues = fieldValues;
	}

	/**
	 * 用于sql条件表达式的值-3 多个值
	 * @param fieldValues 多个值的集合
	 */
	public void setFieldValues(Collection fieldValues) {
		this.fieldValues = fieldValues.toArray(new Object[fieldValues.size()]);
	}
	
	public List<WebQueryCriteria> getSubWebQuerys() {
		return subWebQuerys;
	}

	/**
	 * 设置多个联合的查询条件
	 * @param subWebQuerys 下级联合查询条件
	 */
	public void setSubWebQuerys(List<WebQueryCriteria> subWebQuerys) {
		this.subWebQuerys = subWebQuerys;
	}

	/**
	 * 获取外部连接方式，默认AND
	 * @return 于外部条件的连接方式
	 */
	public Nature getJunctionType() {
		return junctionType;
	}

	/**
	 * 设置外部条件连接方式
	 * @param junctionType 连接方式
	 */
	public void setJunctionType(Nature junctionType) {
		this.junctionType = junctionType;
	}

	/**
	 * 获取内部联合条件连接方式，默认AND
	 * @return 内部联合条件连接方式
	 */
	public Nature getSubJunctionType() {
		return subJunctionType;
	}

	/**
	 * 设置内部联合条件连接方式
	 * @param subJunctionType 内部联合条件连接方式
	 */
	public void setSubJunctionType(Nature subJunctionType) {
		this.subJunctionType = subJunctionType;
	}
	
	@Override
	public String toString() {
		return "WebQueryCriteria [fieldName=" + fieldName + ", fieldValue="
				+ fieldValue + ", fieldValue1=" + fieldValue1
				+ ", fieldValues=" + Arrays.toString(fieldValues)
				+ ", whereType=" + whereType + ", junctionType=" + junctionType
				+ ", subJunctionType=" + subJunctionType + ", subWebQuerys="
				+ subWebQuerys + "]";
	}



	// ---------------------查询条件---------------------------------------------------------------------
	/**
	 * =号
	 */
	public static final int joint_eq = 1;
	/**
	 * !=号
	 */
	public static final int joint_ne = 2;
	/**
	 * like
	 */
	public static final int joint_like = 3;
	/**
	 * isNull
	 */
	public static final int joint_isNull = 4;
	/**
	 * isNotNull
	 */
	public static final int joint_isNotNull = 5;
	/**
	 * >号
	 */
	public static final int joint_gt = 6;
	/**
	 * <号
	 */
	public static final int joint_lt = 7;
	/**
	 * <=号
	 */
	public static final int joint_le = 8;
	/**
	 * >=号
	 */
	public static final int joint_ge = 9;
	/**
	 * between
	 */
	public static final int joint_between = 10;
	/**
	 * not between
	 */
	public static final int joint_notbetween = 11;
	/**
	 * in
	 */
	public static final int joint_in = 12;
	/**
	 * not in
	 */
	public static final int joint_notin = 13;
	/**
	 * not like
	 */
	public static final int joint_not_like = 14;
	
	/**
	 * is empty
	 */
	public static final int joint_isEmpty = 15;
	
	/**
	 * is not empty
	 */
	public static final int joint_isNotEmpty = 16;
	
	/**
	 * sizeEq
	 */
	public static final int joint_sizeEq = 17;
	
	/**
	 * sizeNe
	 */
	public static final int joint_sizeNe = 18;
	
	/**
	 * sizeGt
	 */
	public static final int joint_sizeGt = 19;
	
	/**
	 * sizeLt
	 */
	public static final int joint_sizeLt = 20;
	
	/**
	 * sizeGe
	 */
	public static final int joint_sizeGe = 21;
	
	/**
	 * sizeLe
	 */
	public static final int joint_sizeLe = 22;
	

}
