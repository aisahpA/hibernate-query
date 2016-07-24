package cn.cxg.hibernate.query;


import cn.cxg.hibernate.util.StringHelper;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.sql.JoinType;

import java.util.*;

/**
 * 别名集合对象
 * 用于存储一次查询相关的所有别名信息
 * 
 * @author chenxianguan  2015年12月4日
 *
 */
public class Aliases {
	
	/**
	 * 存储所有别名
	 */
	private final Set<Alias> aliasList = new LinkedHashSet<>();
	
	/**
	 * 存储已有的原属性路径和对应的别名
	 * {@code <Alias.associationPath, Alias.alias>}
	 */
	private final Map<String, String> aliasMap = new HashMap<>();
	
	
	public Aliases(){
		
	}
	
	/**
	 * 使用query构建别名集合对象
	 * @param query
	 */
	public Aliases(Query query){
		
		this.addAlias(query.getFetch());
		this.addAlias(query.getQuerys());
		this.addAlias(query.getOrders());
		
	}
	
	
	/**
	 * 添加别名，默认 {@link JoinType#INNER_JOIN}}
	 * <p>注意：此方法不会拆解路径
	 * @param associationPath A dot-seperated property path
	 * @see #addAlias(String, JoinType)
	 */
	public void addAlias(String associationPath){
		this.addAlias(associationPath, JoinType.INNER_JOIN);
	}
	
	/**
	 * 添加别名
	 * <p>注意：此方法不会拆解路径
	 * <p>FIXME 后添加的不能修改前面设置的别名，需要注意添加别名的顺序
	 * @param associationPath A dot-seperated property path
	 * @param joinType 连接方式
	 */
	public void addAlias(String associationPath, JoinType joinType){
		if(StringUtils.isEmpty(associationPath)){
			return;
		}
		
		if(aliasMap.containsKey(associationPath)){
			return;
		}
		
		// 设置别名
		// 以最后一个点号后面的部分作为别名起点
		String temp = StringHelper.getAfterLastDotPart(associationPath);
		if(null == temp){
			temp = associationPath;
		}
		
		String alias = temp;
		int i = 1;
		while(aliasMap.containsValue(alias)){
			alias = temp + i++ + "_";
		}
		
		aliasList.add(new Alias(associationPath, alias, joinType));
		aliasMap.put(associationPath, alias);
		
	}
	
	/**
	 * 添加别名
	 * 
	 * @param collection
	 */
	public void addAlias(Collection<String> collection){
		if(collection == null || collection.isEmpty()){
			return;
		}
		
		Set<String> associationPaths = StringHelper.convertString(collection);
		for (String associationPath : associationPaths) {
			this.addAlias(associationPath);
		}
	}
	
	/**
	 * 添加别名
	 * 
	 * @param querys
	 */
	public void addAlias(List<WebQueryCriteria> querys) {
		if (null == querys || querys.isEmpty()) {
			return;
		}
		Set<String> fields = this.getAllFieldNames(querys);
		
		this.addAlias(fields);		
	}
	
	/**
	 * 通过递归获得查询条件中所有字段的名称
	 * 
	 * @param querys 查询条件集合
	 * @return 查询条件中所有字段的名称
	 */
	public Set<String> getAllFieldNames(Collection<WebQueryCriteria> querys){
		Set<String> fields = new HashSet<String>();
		if(querys == null || querys.isEmpty()){
			return fields;
		}
		for (WebQueryCriteria query : querys) {
			fields.add(query.getFieldName());
			// 递归所有之级查询查询
			fields.addAll(this.getAllFieldNames(query.getSubWebQuerys()));
		}
		return fields;
	}
	
	/**
	 * 添加别名 
	 * 
	 * @param orders 其中key为属性字段
	 */
	public void addAlias(Map<String, String> orders){
		if(orders == null || orders.isEmpty()){
			return;
		}
		
		this.addAlias(orders.keySet());
	}
	
	/**
	 * 添加别名
	 * 
	 * @param fetch
	 */
	public void addAlias(Fetch fetch) {
		if (null == fetch 
				|| (fetch.getFields().isEmpty() && fetch.getFetchModes().isEmpty())) {
			return;
		}
		
		
		Set<String> associationPaths = StringHelper.convertString(fetch.getFetchModes().keySet());
		associationPaths.addAll(fetch.getFetchModes().keySet());
		
		associationPaths.addAll(StringHelper.convertString(fetch.getJoinTypes().keySet()));
		associationPaths.addAll(fetch.getJoinTypes().keySet());
				
		// 添加指定连接方式的别名
		for (String fetchEntityName : associationPaths) {
			JoinType fetchMode = fetch.getJoinTypes().get(fetchEntityName);
			this.addAlias(fetchEntityName, fetchMode);
		}
		
		// 添加抓取字段相关的别名
		this.addAlias(fetch.getFields());
	}
	
	/**
	 * 通过字段名称得到该字段所需要的别名
	 * 
	 * @param propertyName 要查询的字段名称
	 * @return 该字段所需要的别名 or null
	 */
	public String getAliasStr(String propertyName) {
		
		String str = StringHelper.getBeforeLastDotPart(propertyName);
		
		return null == str ? null : aliasMap.get(str);
	}

	/**
	 * 将指定的属性名转换带真实别名的属性名称
	 * 
	 * @param propertyName 要查询的字段名称
	 * @return 带真实别名的属性名称 or 没有别名时，直接返回属性名称
	 */
	public String getPropertyName(String propertyName) {
		
		String alias = this.getAliasStr(propertyName);
		if (null == alias) {
			return propertyName;
		}
		// 别名.属性名称
		return alias + "." + StringHelper.getAfterLastDotPart(propertyName);
	}

	public Set<Alias> getAliasList() {
		return aliasList;
	}

	public Map<String, String> getAliasMap() {
		return aliasMap;
	}

	@Override
	public String toString() {
		return "Aliases [aliasList=" + aliasList + ", aliasMap=" + aliasMap
				+ "]";
	}
	
}
