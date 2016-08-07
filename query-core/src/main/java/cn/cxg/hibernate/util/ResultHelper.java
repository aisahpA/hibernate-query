package cn.cxg.hibernate.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理执行hibernate查询出的结果集
 * FIXME 这里面的转换方法好像有些问题
 * 
 * @author chenxianguan  2015年12月5日
 *
 */
public class ResultHelper {
	/**
	 * 将查询的出来的list集合，如果当前查询为投影查询时，则将Object数组转为Map集合，其中key当前查询的字段的名称
	 * ，value为查询字段所对应的值
	 * 
	 * @param list
	 * @param fields
	 * @param entityClass
	 */
	public static List convertListToMap(List list, List<String> fields,
			Class entityClass) {
		if (list.isEmpty()) {
			return list;
		}

		List newList = new ArrayList();
		for (Object object : list) {
			Object _object = convertListToMap(object, fields, entityClass);
			if (null != object) {
				newList.add(_object);
			}
		}
		return newList;
	}

	/**
	 * 将查询的出来的object对象，如果当前查询为投影查询时，则将Object数组转为Map集合，其中key当前查询的字段的名称
	 * ，value为查询字段所对应的值
	 * 
	 * @param object
	 * @param fields
	 * @param entityClass
	 * @return
	 */
	public static Object convertListToMap(Object object, List<String> fields,
			Class entityClass) {
		if (null == object) {
			return null;
		}
		if (null == fields || fields.isEmpty() || fields.size() == 1) {
			return object;
		}
		Map maps = new HashMap();
		if (object instanceof Object[]) {
			Object[] _object = (Object[]) object;
			for (int i = 0; i < _object.length; i++) {
				maps.put(fields.get(i), _object[i]);
			}
		}
		return maps;

	}

	/**
	 * 将投影查询出的结果集Object[]数组转换为PO对象，并将Object[] 数组中的结果集设置到对应的PO对象的属性中
	 * 
	 * @param list
	 * @param fields
	 * @param entityClass
	 */
	public static <T> List<T> convertListToObject(List list,
			List<String> fields, Class<T> entityClass) throws Exception {

		if (list == null || list.isEmpty()) {
			return new ArrayList<T>(0);
		}
		if (fields == null || fields.isEmpty()) {
			return new ArrayList<T>(0);
		}
		String[] fieldsArr = fields.toArray(new String[0]);

		List<T> objects = new ArrayList<T>(list.size());
		// 当此查询为投影查询时，则表明list的每一个元素都是Object数组，并且将Object数组中的值设置到domain对象所对应的属性中去
		if (list.get(0) instanceof Object[]) {
			for (int i = 0; i < list.size(); i++) {
				Object[] values = (Object[]) list.get(i);
				Object object = ObjectHelper.convertDomain(entityClass, fieldsArr, values);
				if (null == object) {
					return new ArrayList<T>(0);
				}
				objects.add((T) object);
			}

		}
		// 当查询的字段只有一个时，此时的结果集不为object对象，且不能强制转换为Object[]数组，将object对象中的结果值设置到domain对象对应的属性上
		else if (list.get(0).getClass() != entityClass) {
			for (int i = 0; i < list.size(); i++) {
				Object object = ObjectHelper.convertDomain(entityClass,fieldsArr, new Object[] { list.get(i) });
				if (null == object) {
					new ArrayList<T>(0);
				}
				objects.add((T) object);
			}
		}
		return objects;
	}

	/**
	 * 将投影查询出的结果集Object[]数组转换为PO对象，并将Object[] 数组中的结果集设置到对应的PO对象的属性中
	 * 
	 * @param object
	 * @param fields
	 * @param entityClass
	 * @return
	 * @throws Exception
	 */
	public static <T> T convertListToObject(Object object, List<String> fields,
			Class<T> entityClass) throws Exception {
		List list = new ArrayList(1);
		list.add(object);
		List<T> objects = convertListToObject(list, fields, entityClass);
		if (objects.size() > 0) {
			return objects.get(0);
		}
		return null;
	}

}
