package cn.cxg.hibernate.query;

import java.util.*;

/**
 * 分页对象
 * 
 * @author chenxianguan  2016年1月15日
 *
 * @param <T>
 */
public class Page<T> {
	/**
	 * 不使用startIndex参数的标示值
	 */
	private static final int NON_USE_START_INDEX = -99;

	private int pageNumber = 1; // 当前页码（从1开始）
	private int pageSize = Constants.DEFAULT_PAGE_SIZE;; // 每页数据总条数
	private int startIndex = NON_USE_START_INDEX; // 真正的查询起点
	private int totalCount = 0; // 总记录数
	private Collection<T> results = new ArrayList<T>();// 查询结果集

	/**
	 * 默认构造方法
	 */
	public Page() {
		
	}
	
	/**
	 * 创建一个新的实例 Page.
	 * 
	 * @param pageNumber
	 *            当前页号(从1开始)
	 * @param pageSize
	 *            每页显示记录数
	 */
	public Page(int pageNumber, int pageSize) {	
		this.setPageNumber(pageNumber);
		this.setPageSize(pageSize);
	}

	/**
	 * 
	 * 获取当前页号(从1开始)
	 * 
	 * @return 当前页号
	 */
	public int getPageNumber() {
		return pageNumber;
	}
	
	/**
	 * 设置当前查询第几页（从1开始），参数为空是，设置为1
	 * 
	 * @param pageNumber 页码
	 */
	public void setPageNumber(Integer pageNumber){
		if(pageNumber == null || pageNumber <= 0){
			pageNumber = 1;
		}
		this.pageNumber = pageNumber;
	}

	/**
	 * 获取每页显示最大记录数
	 * 
	 * @return 每页显示最大记录数
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页的最大数量(参数为空或<=0时，设置为默认的值 {@link Constants#DEFAULT_PAGE_SIZE})
	 * 
	 * @param pageSize 每页最大数量
	 */
	public void setPageSize(Integer pageSize) {
		if(pageSize == null || pageSize <= 0){
			pageSize = Constants.DEFAULT_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}

	/**
	 * 获取查询结果集合
	 * 
	 * @return 查询结果集合，不会为null
	 */
	public Collection<T> getResults() {
		return results;
	}

	/**
	 * 获取查询结果的List形式，不会为null
	 * 
	 * @return 查询结果的List形式
	 */
	public List<T> getList() {		
		return results instanceof List ? 
			(List<T>) results : new ArrayList<T>(results);		
	}

	/**
	 * 获取查询结果的Set形式
	 * 
	 * @return 查询结果的Set形式，不会为null
	 */
	public Set<T> getSet() {		
		return results instanceof Set ? 
				(Set<T>) results : new HashSet<T>(results);		
	}

	/**
	 * 设置查询结果集合，当参数results为null时，会设置为长度为0的集合对象
	 * 
	 * @param results 待设置的查询结果
	 */
	public void setResults(Collection<T> results) {
		if(results == null){
			results = new ArrayList<T>();
		}
		this.results = results;
	}
	
	/**
	 * 获取查询起点，会优先使用设置的startIndex值
	 * 
	 * @return 查询起点
	 */
	public int getStartIndex() {
		if(startIndex == NON_USE_START_INDEX){
			return (this.getPageNumber()-1) * this.getPageSize();
		} else {
			return startIndex;
		}
	}
	
	/**
	 * 设置查询起点（从0开始）
	 * <br/>所以需要注意和{@link #setPageNumber(Integer)}不要同时使用
	 * <br/>并且，如果执行过该方法，那么在获取查询起点的时候，会优先使用这里设置的值，即pageNumber失效
	 * 
	 * @param startIndex 查询起点值，即mysql中limt ?,?的第一个参数
	 */
	public void setStartIndex(Integer startIndex){
		if(startIndex == null || startIndex < 0){
			startIndex = 0;
		}
		this.startIndex = startIndex;
	}
	
	/**
	 * 取消设置startIndex参数，从而分页时使用pageNumber的值
	 * 
	 * @since create chenxianguan 2016年2月17日 下午2:18:25
	 */
	public void cancelSetStartIndex(){
		this.startIndex = NON_USE_START_INDEX;
	}

	/**
	 * 获取不分页情况下总的记录数
	 * 
	 * @return 不分页情况下总的记录数
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置不分页情况下总的记录数
	 * 
	 * @param totalCount 不分页情况下总的记录数
	 */
	public void setTotalCount(int totalCount) {		
		this.totalCount = totalCount;
	}
	

}