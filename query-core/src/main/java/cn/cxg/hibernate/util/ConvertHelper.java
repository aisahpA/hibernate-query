package cn.cxg.hibernate.util;

import org.apache.commons.beanutils.ConvertUtils;

/**
 * 在 {@link org.apache.commons.beanutils.ConvertUtils }基础上，注册新的转换实现，
 * 并可处理字符串转枚举对象
 * 
 * @author chenxianguan 2015年12月2日
 *
 */
public class ConvertHelper extends ConvertUtils {
	static {
		// 注册新的转换实现类
		register(new DateConverter(), java.util.Date.class);
	}
	
	/**
	 * 覆盖父类ConvertUtils的方法{@link ConvertUtils#convert(String, Class)}
	 * 
	 * @param value 待转换的值
	 * @param clazz 转换结果的类型
	 * @return 转换结果
	 */
	@SuppressWarnings("unchecked")
	public static Object convert(String value, Class clazz) {
		
		// 处理枚举对象
		if(clazz.isEnum()){
			return Enum.valueOf(clazz, value);
		}
		
		return ConvertUtils.convert(value, clazz);
	}

}
