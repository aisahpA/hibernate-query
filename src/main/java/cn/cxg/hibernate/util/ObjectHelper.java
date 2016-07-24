package cn.cxg.hibernate.util;


import cn.cxg.hibernate.domain.IDomainObject;

import java.lang.reflect.*;
import java.util.*;

/**
 * 
 * @author chenxianguan 2015年12月2日
 *
 */
public class ObjectHelper {

	/**
	 * 让其解析PO对象时，只能解析以下数据的字段类型
	 */
	private static final Map<String, String> javaTypeMap = new HashMap<>();
	static {
		javaTypeMap.put("java.lang.Byte", "YES");
		javaTypeMap.put("java.lang.Character", "YES");
		javaTypeMap.put("java.lang.Short", "YES");
		javaTypeMap.put("java.lang.Integer", "YES");
		javaTypeMap.put("java.lang.Float", "YES");
		javaTypeMap.put("java.lang.Long", "YES");
		javaTypeMap.put("java.lang.Double", "YES");
		javaTypeMap.put("java.lang.String", "YES");
		javaTypeMap.put("java.util.Date", "YES");
	}

	/**
	 * 通过类结构获取该对象所有属性，该属性不包括Set集合和关联对象--该方法不支持解析继承的属性
	 * 
	 * @param clazz
	 * @return
	 */
	public static String[] getObjectProperty(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		List<String> propertys = new ArrayList<String>();
		for (Field field : fields) {
			if (javaTypeMap.containsKey(field.getType().getName())) {
				propertys.add(field.getName());
			}
		}

		return propertys.toArray(new String[0]);
	}

	/**
	 * 通过属性名称得到该属性的class，支持：xxx.xx.xx
	 * 
	 * @param clazz
	 * @param propertyName
	 * @return
	 */
	public static Class<?> getTypeClass(Class<?> clazz, String propertyName) {
		String[] array = propertyName.split("\\.");
		// 当前的属性不是关联对象的属性，也就是当前clazz类的直接属性
		if (null == array) {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.getName().equals(propertyName)) {
					return field.getType();
				}
			}
		}
		// 通过迭代的方式，找出当要查询的属性的数据类型
		Class<?> _clazz = clazz;
		for (String str : array) {
			for (Field field : _clazz.getDeclaredFields()) {
				if (field.getName().equals(str)) {
					if (field.getType() == Set.class) {
						// 得到当前属性的第一个字母，并将其转换为大写
						String firstLetter = field.getName().substring(0, 1)
								.toUpperCase();
						// 得当当前属性除第一个字母之外的所以字符
						String name = field.getName().substring(1);
						try {
							// 通过拼装的方式，得到该属性的set方法名称
							String setMethodName = "set" + firstLetter + name;

							// 用于获取set集合中的泛型类型
							Method method = _clazz.getMethod(setMethodName,
									Set.class);
							Type[] types = method.getGenericParameterTypes();
							ParameterizedType parameterizedType = (ParameterizedType) types[0];
							Type type = parameterizedType
									.getActualTypeArguments()[0];
							_clazz = (Class<?>) type;

							break;
						} catch (Exception e) {
							e.printStackTrace();
							throw new RuntimeException(e.getMessage());
						}
					}
					_clazz = field.getType();
					break;
				}
			}
		}
		return _clazz;
	}

	/**
	 * 通过属性名称获取对应的属性值(支持xxxx.xxx.xxx级联方式)
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(Object object, String fieldName) {

		return fieldName;

	}

	/**
	 * 将查询出的Object[]的结果转换为对应的domain对象
	 * 
	 * @param entityClass
	 * @param fields
	 * @param values
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws NoSuchFieldException
	 */
	public static IDomainObject convertDomain(Class<?> entityClass,
											  String[] fields, Object[] values) throws InstantiationException,
			IllegalAccessException, SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			InvocationTargetException, NoSuchFieldException {
		if (null == values || values.length == 0 || null == fields
				|| fields.length == 0) {
			return null;
		}
		Object object = entityClass.newInstance();
		for (int i = 0; i < fields.length; i++) {
			setProperty(object, StringHelper.convertFieldsArray(fields[i]),
					values[i]);
		}

		return (IDomainObject) object;

	}

	/**
	 * 设置对象的属性值
	 * 
	 * @param object
	 * @param field
	 * @param value
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static void setProperty(Object object, String field, Object value)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		// 获得field属性所对应的set方法
		Method method = object.getClass().getDeclaredMethod(
				StringHelper.convertSetMethodName(field), value.getClass());
		method.invoke(object, value);
	}

	/**
	 * 
	 * 设置对象的属性值
	 * 
	 * @param object
	 * @param fields
	 * @param value
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws NoSuchFieldException
	 */
	private static void setProperty(Object object, String[] fields, Object value)
			throws SecurityException, IllegalArgumentException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException,
			NoSuchFieldException {
		if (fields.length == 1) {
			setProperty(object, fields[0], value);
			return;
		}
		Object _object = object;
		for (int i = 0; i < fields.length - 1; i++) {
			// 记录上一次的Object对象
			Object oldObject = _object;
			// 将_object对象赋值为新的对象
			_object = object.getClass().getDeclaredField(fields[i]).getType()
					.newInstance();
			setProperty(oldObject, fields[0], _object);
		}
		setProperty(_object, fields[fields.length - 1], value);

	}
	
	/**
	 * 判断一个对象是否为空
	 * <p>空对象，空字符串，"  "，空数组，空集合都返回false
	 * @param testObj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object testObj){
		if(null == testObj){
			return true;
		}		
		if(testObj instanceof String){
			String obj = (String) testObj;			
			return "".equals(obj.trim());
		}
		if(testObj.getClass().isArray()){
			Object[] objArr = (Object[]) testObj;
			return objArr.length == 0;
		}
		if(testObj instanceof Collection){
			Collection c = (Collection) testObj;
			return c.isEmpty();
		}
		
		return false;
	}

}
