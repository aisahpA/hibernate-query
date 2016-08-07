package cn.cxg.hibernate.query;

import org.hibernate.sql.JoinType;

/**
 * 实体查询时，别名对象
 * 
 * @author chenxianguan 2015年12月2日
 *
 */
public class Alias {
	
	// A dot-seperated property path
	private String associationPath;
	
	// 别名
	private String alias;
	
	// 关联方式--默认为INNER_JOIN
	private JoinType joinType = JoinType.INNER_JOIN;

	public Alias() {

	}
	
	/**
	 * 构建别名对象，使用默认的INNER_JOIN
	 * @param associationPath A dot-seperated property path
	 */
//	public Alias(String associationPath) {
//		this.associationPath = associationPath;
//	}
	
	/**
	 * 构建别名对象
	 * @param associationPath A dot-seperated property path
	 * @param joinType 关联方式
	 */
//	public Alias(String associationPath, JoinType joinType) {
//		this.associationPath = associationPath;
//		if (null != joinType) {
//			this.joinType = joinType;
//		}
//	}
	
	/**
	 * 构建别名对象，使用默认的INNER_JOIN
	 * @param associationPath A dot-seperated property path
	 * @param alias 别名
	 */
	public Alias(String associationPath, String alias) {
		this.associationPath = associationPath;
		this.alias = alias;		
	}

	/**
	 * 构建别名对象
	 * @param associationPath A dot-seperated property path
	 * @param alias 别名
	 * @param joinType 关联方式
	 */
	public Alias(String associationPath, String alias, JoinType joinType) {
		this.associationPath = associationPath;
		this.alias = alias;
		if (null != joinType) {
			this.joinType = joinType;
		}
	}

	public String getAssociationPath() {
		return associationPath;
	}

	public void setAssociationPath(String associationPath) {
		this.associationPath = associationPath;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public JoinType getJoinType() {
		return joinType;
	}

	public void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}

	@Override
	public String toString() {
		return "Alias [associationPath=" + associationPath + ", alias=" + alias
				+ ", joinType=" + joinType + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result
				+ ((associationPath == null) ? 0 : associationPath.hashCode());
		result = prime * result
				+ ((joinType == null) ? 0 : joinType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alias other = (Alias) obj;
		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;
		if (associationPath == null) {
			if (other.associationPath != null)
				return false;
		} else if (!associationPath.equals(other.associationPath))
			return false;
		if (joinType != other.joinType)
			return false;
		return true;
	}
	
	

}
