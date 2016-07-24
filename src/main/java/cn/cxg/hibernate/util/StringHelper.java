package cn.cxg.hibernate.util;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 字符串工具类
 * 
 * @author chenxianguan 2015年12月3日
 *
 */
public class StringHelper {
	
	/**
	 * 从strs数组中提取关联实体名字，并去重
	 * <p>a.b.c.prop--&gt a.b.c, a.b, a --&gt a, a.b, a.b.c
	 * @param collection
	 * @return
	 */
	public static Set<String> convertString(Collection<String> collection) {
		Set<String> list = new LinkedHashSet<String>();
		for (String str : collection) {
			list.addAll(splitByDot(str));
		}
		resort(list);// 这个排序好像没什么用
		return list;
	}
	
	/**
	 * 从strs数组中提取关联实体名字，并去重
	 * <p>a.b.c.prop--&gt a.b.c, a.b, a --&gt a, a.b, a.b.c
	 * @param str 待拆解字符串
	 * @return 拆解结果，字符串不包含点号的时候返回空集合
	 */
	public static Set<String> splitByDot(final String str){
		Set<String> list = new LinkedHashSet<String>();
		if(str == null) return list;
		
		String temp = str;
		String beforePart;
		while (true) {
			beforePart = getBeforeLastDotPart(temp);
			if (null == beforePart) {
				break;
			}
			list.add(beforePart);
			temp = beforePart;
		}
		
		resort(list);
		return list;
	}
	
	/**
	 * 获取字符串最后一个点号之前的那部分字符串
	 * @param str 待处理字符串
	 * @return 字符串最后一个点号之前的那部分字符串 or 没有点号的返回null
	 */
	public static String getBeforeLastDotPart(final String str){
		if(null == str) return null;
		int index = str.lastIndexOf(".");
		if (index == -1) {
			return null;
		}
		return str.substring(0, index);
	}
	
	/**
	 * 获取字符串最后一个点号之后的那部分字符串
	 * @param str 待处理字符串
	 * @return 字符串最后一个点号之后的那部分字符串 or 没有点号的返回null
	 */
	public static String getAfterLastDotPart(final String str){
		if(null == str) return null;
		int index = str.lastIndexOf(".");
		if (index == -1) {
			return null;
		}
		return str.substring(index + 1, str.length());
	}

	/**
	 * 将set集合中的元素倒序排列
	 * 
	 * @param list
	 */
	private static void resort(Set<String> list) {
		if (null == list || list.size() == 0) {
			return;
		}
		Object[] objects = list.toArray();
		list.clear();
		for (int i = objects.length - 1; i >= 0; i--) {
			list.add(objects[i].toString());
		}
	}

	/**
	 * 将xxxx.xxx.xxx转换为 new String[]{"xxxx","xxx","xxx"})数组对象
	 * 
	 * @param field
	 * @return
	 */
	public static String[] convertFieldsArray(String field) {
		String[] array = field.split("\\.");
		if (array.length == 0) {
			return new String[] { field };
		}
		return array;

	}

	/**
	 * 将属性名称转换为对应的set方法名称
	 * 
	 * @param field
	 * @return
	 */
	public static String convertSetMethodName(String field) {
		String initial = field.substring(0, 1).toUpperCase();
		return "set" + initial + field.substring(1);

	}
	
	public static void main(String[] args) {
		
		System.out.println(splitByDot("aaa.bb.ccc.dd"));
		
		System.out.println("   ".equals(""));
	}

}
