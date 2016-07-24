package cn.cxg.hibernate.util;

import cn.cxg.hibernate.query.Aliases;
import org.hibernate.criterion.DetachedCriteria;

import java.util.Map;

/**
 * 解析装有排序信息 的Map集合，并将排序信息翻译给DetachedCriteria对象
 * 
 * @author chenxianguan 2015年12月3日
 *
 */
public class OrderHelper {

	/**
	 * 解析Map集合，并将其要查询的排序信息翻译给DetachedCriteria实例对象
	 * 
	 * @param detachedCriteria
	 * @param aliases
	 * @param orders
	 */
	public static void pretreatmentOrder(DetachedCriteria detachedCriteria, 
			Aliases aliases,
			Map<String, String> orders) {
		
		if (null == orders || orders.isEmpty()) {
			return;
		}

		for (String key : orders.keySet()) {
			
			String propertyName = aliases.getPropertyName(key);
			
			if ("desc".equals(orders.get(key).toLowerCase())) {
				detachedCriteria.addOrder(org.hibernate.criterion.Order
						.desc(propertyName));
			} else {
				detachedCriteria.addOrder(org.hibernate.criterion.Order
						.asc(propertyName));
			}
		}

	}

}
