package wangzhongqiu.ordercenter.util.utils;

import java.io.Serializable;

/**
 * @ClassName: Filter
 * @Description:
 * 
 * @date  2015/3/12
 */
public class Filter implements Serializable {

	/**
	 * 此处为属性说明
	 */
	private static final long	serialVersionUID	= 3647468241598238318L;
	private String				field;
	private String				value;

	public Filter(String field, String value) {
		this.field = field;
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}