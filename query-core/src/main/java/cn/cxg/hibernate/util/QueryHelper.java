package cn.cxg.hibernate.util;

import cn.cxg.hibernate.query.Alias;
import cn.cxg.hibernate.query.Aliases;
import cn.cxg.hibernate.query.Query;
import org.hibernate.criterion.DetachedCriteria;

/**
 * 
 * 
 * @author chenxianguan 2015年12月3日
 *
 */
public class QueryHelper {
	
	/**
	 * 根据query查询对象中查询参数，生成detachedCriteria离线查询对象
	 * 
	 * @param query 查询条件包装对象
	 * @return 离线查询对象
	 */
	public static DetachedCriteria pretreatmentQuery(Query query) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(query
				.getDomainClass());

		// 过滤无用的查询条件
        WebQueryCriteriaHelper.filterNoUseQuery(query.getQuerys());
		
		// 先获取所有别名
		Aliases aliases = new Aliases(query);
		
		for (Alias alias : aliases.getAliasList()) {
			detachedCriteria.createAlias(alias.getAssociationPath(),
					alias.getAlias(), alias.getJoinType());
		}

		FetchHelper.pretreatmentFetch(
				detachedCriteria, aliases,
				query.getFetch());

		WebQueryCriteriaHelper.pretreatmentWebQueryCriteria(
				detachedCriteria, aliases, 
				query.getQuerys(), query.getDomainClass());

		OrderHelper.pretreatmentOrder(
				detachedCriteria, aliases,
				query.getOrders());

		return detachedCriteria;
	}

}
