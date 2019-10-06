package org.datax.console.common.utils;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询返回对象
 * Created by ChengJie on 2018/12/7 12:24
 */
public class PageUtils implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long total;
	private List<?> list;

	public PageUtils() {
	}

	public PageUtils(List<?> list, Long total) {
		this.list = list;
		this.total = total;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<?> getRows() {
		return list;
	}

	public void setRows(List<?> rows) {
		this.list = rows;
	}

}
