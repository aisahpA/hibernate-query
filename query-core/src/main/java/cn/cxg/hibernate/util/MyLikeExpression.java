package cn.cxg.hibernate.util;

import org.hibernate.criterion.LikeExpression;
import org.hibernate.criterion.MatchMode;

/**
 * like
 * <p>因为不能直接new LikeExpression，所以这里继承LikeExpression，并将构造方法公开
 * @author chenxianguan  2015年12月5日
 *
 */
public class MyLikeExpression extends LikeExpression {


	/**
	 * 构建like查询条件
	 * @param propertyName 属性字段名称
	 * @param value 属性值
	 * @param matchMode 匹配方式
	 * @param escapeChar 转义标识字符串，可使用'/'
	 * @param ignoreCase 是否区分大小写
	 */
	public MyLikeExpression(String propertyName, String value,
			MatchMode matchMode, Character escapeChar, boolean ignoreCase) {
		super(propertyName, value, matchMode, escapeChar, ignoreCase);
	}
	
}
