package cn.cxg.hibernate.util;

import java.util.Collection;

public class PageBean<T> {

	private int startIndex; // 开始记录号
	private int pageSize;
	private int totalCount; 
	private int totalPageCount;
	private int[] showPageNumbers;
	private int pageIndex;
	private int currentPage;
	private Collection<T> results;
	
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getTotalPageCount() {
		return totalPageCount;
	}
	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	
	public int[] getShowPageNumbers() {
		return showPageNumbers;
	}
	public void setShowPageNumbers(int[] showPageNumbers) {
		this.showPageNumbers = showPageNumbers;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public Collection<T> getResults() {
		return results;
	}
	public void setResults(Collection<T> results) {
		this.results = results;
	}
	
}
