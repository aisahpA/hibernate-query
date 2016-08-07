package cn.cxg.hibernate.util;

import cn.cxg.hibernate.query.Aliases;
import cn.cxg.hibernate.query.WebQueryCriteria;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction.Nature;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 解析WebQueryCriteria对象，并将要查询的条件信息翻译给detachedCriteria对象
 * 
 * @author chenxianguan  2015年12月3日
 *
 */
public class WebQueryCriteriaHelper {
	
	/**
	 * 解析WebQueryCriteria条件集合，
	 * 并将其要查询的条件信息翻译给detachedCriteria实例对象
	 * @param detachedCriteria 装载查询条件的离线查询对象
	 * @param aliases 携带所有别名的对象
	 * @param querys 携带查询条件对象的集合
	 * @param rootEntityClazz 查询根对象的实体类型 
	 */
	public static void pretreatmentWebQueryCriteria(
			DetachedCriteria detachedCriteria,
			Aliases aliases,
			List<WebQueryCriteria> querys,
			Class<?> rootEntityClazz) {
		
		if (ObjectHelper.isEmpty(querys)) {
			return;
		}
		
		// 过滤掉空值
		final List<WebQueryCriteria> finalQuerys = new ArrayList<>();
		for (WebQueryCriteria query : querys) {
			if (query != null) {
				finalQuerys.add(query);
			}
		}
		//
		convert(rootEntityClazz, finalQuerys);
		
		// 开始转换并添加到条件中
		List<Criterion> criterions = createGroupCriterion(finalQuerys, aliases);
		for(Criterion criterion : criterions){
			detachedCriteria.add(criterion);
		}
	}
	
	/**
	 * 根据多个查询条件构建一组查询
	 * @param querys 携带查询条件对象的集合
	 * @param aliases 携带所有别名的对象
	 * @return hibernate查询条件集合 或 长度为0的集合
	 */
	public static List<Criterion> createGroupCriterion(List<WebQueryCriteria> querys, 
			Aliases aliases){
		
		List<Criterion> criterions = new ArrayList<>();
		if(querys == null || querys.isEmpty()) return criterions;
		
		Criterion criterion;		
		String realyPropertyNameInHql;	
		for (WebQueryCriteria query : querys) {
			
			realyPropertyNameInHql = aliases.getPropertyName(query.getFieldName());
			
			criterion = createCriterion(realyPropertyNameInHql, query, aliases);
			if(criterion != null){
				criterions.add(criterion);
			}
		}
		
		return criterions;
	}
	
	/**
	 * 创建hibernate查询条件
	 * @param realyPropertyNameInHql 在hql中的属性名（如果有别名，需要带上别名前缀）
	 * @param query 一个查询条件对象
	 * @param aliases 携带所有别名的对象
	 * @return hibernate查询条件 or null
	 */
	public static Criterion createCriterion(String realyPropertyNameInHql, 
			WebQueryCriteria query, Aliases aliases){
		
		if(ObjectHelper.isEmpty(realyPropertyNameInHql) 
				&& ObjectHelper.isEmpty(query.getSubWebQuerys())){
			return null;
		}
		
		Criterion criterion = null;
		List<Criterion> subCriterions = createGroupCriterion(query.getSubWebQuerys(), aliases);
		
		// 有子级联合条件优先级高于普通条件
		if(!subCriterions.isEmpty()){
			if(subCriterions.size() == 1){
				criterion = subCriterions.get(0);
			} else {
				// 设置多个子级条件之间的关系
				if(query.getSubJunctionType().equals(Nature.AND)){
					criterion = Restrictions.conjunction(subCriterions.toArray(new Criterion[0]));
				} else if(query.getSubJunctionType().equals(Nature.OR)){
					criterion = Restrictions.disjunction(subCriterions.toArray(new Criterion[0]));
				}
			}
			
		} 
		// 没有子级条件的查询
		else {
			
			Object value = query.getFieldValue();
			Object value1 = query.getFieldValue1();
			Object[] values = query.getFieldValues();
			boolean emptyValue = ObjectHelper.isEmpty(value);
			boolean emptyValue1 = ObjectHelper.isEmpty(value1);
			boolean emptyValues = ObjectHelper.isEmpty(values);
			
			switch (query.getWhereType()) {
			case WebQueryCriteria.joint_eq:
				if (emptyValue) {
					break;
				}
				criterion = Restrictions.eq(realyPropertyNameInHql, value);
				break;
			case WebQueryCriteria.joint_ne:
				if (emptyValue) {
					break;
				}
				criterion = Restrictions.ne(realyPropertyNameInHql, value);
				break;
			case WebQueryCriteria.joint_like:
				if (emptyValue) {
					break;
				}
				criterion = new MyLikeExpression(realyPropertyNameInHql,
						escapeSQLLike(value.toString()), 
						MatchMode.ANYWHERE, '/', false);
				break;
			case WebQueryCriteria.joint_isNull:
				criterion = Restrictions.isNull(realyPropertyNameInHql);
				break;
			case WebQueryCriteria.joint_isNotNull:
				criterion = Restrictions.isNotNull(realyPropertyNameInHql);
				break;
			case WebQueryCriteria.joint_gt:
				if (emptyValue) {
					break;
				}
				criterion = Restrictions.gt(realyPropertyNameInHql, value);
				break;
			case WebQueryCriteria.joint_lt:
				if (emptyValue) {
					break;
				}
				criterion = Restrictions.lt(realyPropertyNameInHql, value);
				break;
			case WebQueryCriteria.joint_le:
				if (emptyValue) {
					break;
				}
				criterion = Restrictions.le(realyPropertyNameInHql, value);
				break;
			case WebQueryCriteria.joint_ge:
				if (emptyValue) {
					break;
				}
				criterion = Restrictions.ge(realyPropertyNameInHql, value);
				break;
			case WebQueryCriteria.joint_between:				
				if(emptyValue && emptyValue1){
					break;
				}
				if(emptyValue && !emptyValue1){				
					criterion = Restrictions.lt(realyPropertyNameInHql, value1);
					
				} else if(!emptyValue && emptyValue1){				
					criterion = Restrictions.ge(realyPropertyNameInHql, value);
					
				} else if(value.equals(value1)){
					criterion = Restrictions.eq(realyPropertyNameInHql, value);
					
				} else{
					criterion = Restrictions.between(
							realyPropertyNameInHql, 
							value, value1);
				}
				break;
			case WebQueryCriteria.joint_notbetween:
				if(emptyValue && emptyValue1){
					break;
				}
				if(emptyValue && !emptyValue1){				
					criterion = Restrictions.not(Restrictions.lt(realyPropertyNameInHql, value1));
					
				} else if(!emptyValue && emptyValue1){				
					criterion = Restrictions.not(Restrictions.ge(realyPropertyNameInHql, value));
					
				} else if(value.equals(value1)){
					criterion = Restrictions.not(Restrictions.eq(realyPropertyNameInHql, value));
					
				} else{
					criterion = Restrictions.not(Restrictions.between(realyPropertyNameInHql, value, value1));
				}
				break;
			case WebQueryCriteria.joint_in:
				if (emptyValues) {
					break;
				}
				List<Object> list = Arrays.asList(values);
				criterion = createJoinOr(realyPropertyNameInHql,0,1000,list);
				break;
			case WebQueryCriteria.joint_notin:
				if (emptyValues) {
					break;
				}
				List<Object> notInList = Arrays.asList(query.getFieldValues());
				criterion = Restrictions.not(createJoinOr(realyPropertyNameInHql,0,1000,notInList));
				break;
			case WebQueryCriteria.joint_not_like:
				if (emptyValue) {
					break;
				}
				criterion = Restrictions.not(
						new MyLikeExpression(realyPropertyNameInHql, 
								escapeSQLLike(value.toString()), 
								MatchMode.ANYWHERE, '/', false));
				break;
			case WebQueryCriteria.joint_isEmpty:				
				criterion = Restrictions.isEmpty(realyPropertyNameInHql);
				break;
			case WebQueryCriteria.joint_isNotEmpty:			
				criterion = Restrictions.isNotEmpty(realyPropertyNameInHql);
				break;
			case WebQueryCriteria.joint_sizeEq:
				if (emptyValue) {
					break;
				}
				criterion = Restrictions.sizeEq(realyPropertyNameInHql, Integer.valueOf(value.toString()));
				break;
			case WebQueryCriteria.joint_sizeNe:
				if (emptyValue) {
					break;
				}
				criterion = Restrictions.sizeNe(realyPropertyNameInHql, Integer.valueOf(value.toString()));
				break;
			case WebQueryCriteria.joint_sizeGt:
				if (emptyValue) {
					break;
				}
				criterion = Restrictions.sizeGt(realyPropertyNameInHql, Integer.valueOf(value.toString()));
				break;
			case WebQueryCriteria.joint_sizeLt:
				if (emptyValue) {
					break;
				}
				criterion = Restrictions.sizeLt(realyPropertyNameInHql, Integer.valueOf(value.toString()));
				break;
			case WebQueryCriteria.joint_sizeGe:
				if (emptyValue) {
					break;
				}
				criterion = Restrictions.sizeGe(realyPropertyNameInHql, Integer.valueOf(value.toString()));
				break;
			case WebQueryCriteria.joint_sizeLe:
				if (emptyValue) {
					break;
				}
				criterion = Restrictions.sizeLe(realyPropertyNameInHql, Integer.valueOf(value.toString()));
				break;
									
			default:
				if (emptyValue) {
					break;
				}
				criterion = Restrictions.eq(realyPropertyNameInHql, value);
				break;
			}
		}
		
		// 设置此条件与同级条件的连接关系，默认为AND
		if(null != criterion && query.getJunctionType().equals(Nature.OR)){
			criterion = Restrictions.or(criterion);
		}
		
		return criterion;
	}
	
	/**
	 * 使用/转义
	 * @param likeStr 待转义字符串
	 * @return 转义后的字符串
	 */
	public static String escapeSQLLike(String likeStr) {  
        String str = StringUtils.replace(likeStr, "_", "/_");  
        str = StringUtils.replace(str, "%", "/%");  
        str = StringUtils.replace(str, "?", "_");
        str = StringUtils.replace(str, "*", "%");  
        return str;  
    }

	/**
	 * 将condition对象中的propertyName所对应的值的类型全部转换为propertyName的数据类型
	 * @param clazz
	 * @param querys
	 */
	private static void convert(Class<?> clazz, List<WebQueryCriteria> querys) {
		if(clazz == null){
			return;
		}
		if (ObjectHelper.isEmpty(querys)) {
			return;
		}
		
		for (WebQueryCriteria query : querys) {
			
			// 递归转换子查询对象
			convert(clazz, query.getSubWebQuerys());
			
			if(ObjectHelper.isEmpty(query.getFieldName())){
				continue;
			}
			
			Class<?> fieldClazz = ObjectHelper.getTypeClass(clazz, query.getFieldName());
			
			if (!ObjectHelper.isEmpty(query.getFieldValue())) {
				query.setFieldValue(ConvertHelper.convert(query.getFieldValue(), fieldClazz));
			}
			if (!ObjectHelper.isEmpty(query.getFieldValue1())) {
				query.setFieldValue1(ConvertHelper.convert(query.getFieldValue1(), fieldClazz));
			}

			if (!ObjectHelper.isEmpty(query.getFieldValues())) {
				Object[] values = new Object[query.getFieldValues().length];
				int i = 0;
				for (Object object : query.getFieldValues()) {
					values[i] = ConvertHelper.convert(object, fieldClazz);
					i++;
				}
				query.setFieldValues(values);
			}
		}
	}
	
	private static Criterion createJoinOr(String fieldName,int start,int end,List<Object> list){
		//处理SQL IN 中集合超过1000报错问题
		int size = list.size();
		
		int ratio = size/1000;
		
		if(ratio>0 && size!=1000){
			
			if(end>size){
				
				end = size;
				
				int preEnd = end-1;
				//最后一次拆分
				return Restrictions.or(Restrictions.in(fieldName, list.subList(start, start==preEnd?end:preEnd)), Restrictions.in(fieldName, list.subList(end-1, end)));
			}
			return Restrictions.or(Restrictions.in(fieldName, list.subList(start, end)), createJoinOr(fieldName,end,end+1000,list));
		}
		return Restrictions.in(fieldName, list);
	}

	/**
	 * 过滤无用的查询条件
     *
	 * @param querys 查询条件集合
     */
	static void filterNoUseQuery(List<WebQueryCriteria> querys) {
        if (querys == null || querys.isEmpty()) {
            return;
        }

        List<WebQueryCriteria> invalids = new ArrayList<>();
        for (WebQueryCriteria query : querys) {

            boolean emptyValue = ObjectHelper.isEmpty(query.getFieldValue());
            boolean emptyValue1 = ObjectHelper.isEmpty(query.getFieldValue1());
            boolean emptyValues = ObjectHelper.isEmpty(query.getFieldValues());

            switch (query.getWhereType()) {
                case WebQueryCriteria.joint_eq:
                case WebQueryCriteria.joint_ne:
                case WebQueryCriteria.joint_like:
                case WebQueryCriteria.joint_gt:
                case WebQueryCriteria.joint_lt:
                case WebQueryCriteria.joint_le:
                case WebQueryCriteria.joint_ge:
                case WebQueryCriteria.joint_not_like:
                case WebQueryCriteria.joint_sizeEq:
                case WebQueryCriteria.joint_sizeNe:
                case WebQueryCriteria.joint_sizeGt:
                case WebQueryCriteria.joint_sizeLt:
                case WebQueryCriteria.joint_sizeGe:
                case WebQueryCriteria.joint_sizeLe:
                    if (emptyValue) {
                        invalids.add(query);
                    }
                    break;
                case WebQueryCriteria.joint_between:
                case WebQueryCriteria.joint_notbetween:
                    if(emptyValue && emptyValue1){
                        invalids.add(query);
                    }
                    break;
                case WebQueryCriteria.joint_in:
                case WebQueryCriteria.joint_notin:
                    if (emptyValues) {
                        invalids.add(query);
                    }
                    break;
                case WebQueryCriteria.joint_isNull:
                case WebQueryCriteria.joint_isNotNull:
                case WebQueryCriteria.joint_isEmpty:
                case WebQueryCriteria.joint_isNotEmpty:
                    break;
                default:
                    break;
            }
        }
        querys.removeAll(invalids);
	}
}
