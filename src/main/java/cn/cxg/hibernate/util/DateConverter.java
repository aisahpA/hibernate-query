package cn.cxg.hibernate.util;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * 日期转换
 * 
 * @author chenxianguan 2015年12月2日
 *
 */
public class DateConverter implements Converter {
	
	private static final String[] parsePatterns = new String[] { 
		"yyyy-MM-dd",
		"yyyy/MM/dd", 
		"yyyy-MM-dd HH:mm:ss", 
		"yyyy年MM月dd日 HH:mm:ss", 
		"yyyy-MM-dd HH:mm:ss.SSS",
		"HH:mm:ss"};
	
	private Object defaultValue;
	private boolean useDefault;

	/**
	 * 构造方法
	 */
	public DateConverter() {
		defaultValue = null;
		useDefault = false;
	}

	/**
	 * 构造方法（指定默认值）
	 * 
	 * @param defaultValue
	 *            默认值
	 */
	public DateConverter(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object convert(Class class1, Object value) {
		if (value == null) {
			if (useDefault)
				return defaultValue;
			else
				throw new ConversionException("No value specified");
		}
		if (value instanceof Date) {
			return value;
		}

		Date date = null;
		try {
			date = DateUtils.parseDate(value.toString(), parsePatterns);
		} catch (ParseException e) {
			// 转换失败 /字符数据格式不符
			return defaultValue;
		}

		return date;
	}

}
