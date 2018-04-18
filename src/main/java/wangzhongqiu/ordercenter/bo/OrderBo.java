package wangzhongqiu.ordercenter.bo;


public class OrderBo extends BaseBo{

	/**
	 * 原始订单号 
	 */
	private String orderNo;
	
	/**
	 * 业务类型
	 */
	private String tradeType;
	
	/**
	 * 当前业务状态
	 */
	private String initTradeState;
	
	/**
	 * 业务透传参数
	 */
	private String initStateInput;
	
	/**
	 * 撤销订单号，如需撤销原始订单，此字段必填写
	 */
	private String relatedOrderNo;

	/**
	 * 订单状态
	 */
	private String status;

	public OrderBo() {
	}

	public OrderBo(String orderNo, String tradeType, String initTradeState, String initStateInput) {
		this.orderNo = orderNo;
		this.tradeType = tradeType;
		this.initTradeState = initTradeState;
		this.initStateInput = initStateInput;
	}

	public OrderBo(String orderNo, String tradeType, String initTradeState, String initStateInput,String status) {
		this.orderNo = orderNo;
		this.tradeType = tradeType;
		this.initTradeState = initTradeState;
		this.initStateInput = initStateInput;
		this.status = status;
	}


	public String getRelatedOrderNo() {
		return relatedOrderNo;
	}
	public void setRelatedOrderNo(String relatedOrderNo) {
		this.relatedOrderNo = relatedOrderNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getInitTradeState() {
		return initTradeState;
	}
	public void setInitTradeState(String initTradeState) {
		this.initTradeState = initTradeState;
	}
	public String getInitStateInput() {
		return initStateInput;
	}
	public void setInitStateInput(String initStateInput) {
		this.initStateInput = initStateInput;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
