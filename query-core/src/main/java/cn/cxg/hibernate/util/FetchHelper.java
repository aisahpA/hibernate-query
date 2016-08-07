package cn.cxg.hibernate.util;

import cn.cxg.hibernate.query.Aliases;
import cn.cxg.hibernate.query.Fetch;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;

import java.util.Map;

/**
 * 解析Fetch对象，并将要查询的信息翻译给DetachedCriteria对象
 * 
 * @author chenxianguan 2015年12月3日
 *
 */
public class FetchHelper {

	/**
	 * 预处理search查询中的fetch对象
	 * 
	 * @param detachedCriteria 离线查询对象
	 * @param aliases 别名集合对象
	 * @param fetch 抓取对象
	 */
	public static void pretreatmentFetch(DetachedCriteria detachedCriteria,
										 Aliases aliases, Fetch fetch) {
		if (null == fetch || null == fetch.getFields()
				|| fetch.getFields().isEmpty()) {
			return;
		}

		// 设置要抓取的字段
		ProjectionList proList = Projections.projectionList();
		for (String field : fetch.getFields()) {
			proList.add(Property.forName(aliases.getPropertyName(field)));
		}
		detachedCriteria.setProjection(proList);
		
		// 设置抓取对象的方式
		for(Map.Entry<String, FetchMode> entry : fetch.getFetchModes().entrySet()){
			detachedCriteria.setFetchMode(entry.getKey(), entry.getValue());
		}
	}

}
