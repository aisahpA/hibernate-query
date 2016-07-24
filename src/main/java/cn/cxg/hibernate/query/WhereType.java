package cn.cxg.hibernate.query;

/**
 * 查询类型
 * 这个先放在这儿吧，还没有使用。
 * 
 * @author chenxianguan  2015年12月5日
 *
 */
public enum WhereType {
	
	eq(1),
	eqOrIsNull(2),
	ne(3),
	neOrIsNotNull(4),
	like(5),
	ilike(6),
	gt(7),
	lt(8),
	le(9),
	ge(10),
	between(11),
	in(12),
	isNull(13),
	isNotNull(14),
	eqProperty(15),
	neProperty(16),
	ltProperty(17),
	leProperty(18),
	gtProperty(19),
	geProperty(20),
	not(21),
	isEmpty(22),
	isNotEmpty(23),
	sizeEq(24),
	sizeNe(25),
	sizeGt(26),
	sizeLt(27),
	sizeGe(28),
	sizeLe(29),
	;

	private int type;
	
	private WhereType(int type){
		this.type = type;
	}
	
	public int getValue(){
		return type;
	}
}
