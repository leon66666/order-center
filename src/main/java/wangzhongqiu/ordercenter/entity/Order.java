package wangzhongqiu.ordercenter.entity;

import java.util.Date;


public class Order {
	
	/**
	 * 自增ID
	 */
    private Integer id;

	/**
	 * 业务订单号
	 */
	private String orderNo;

	/**
	 * 订单状态   PROC:处理中   SUSP:挂起  DONE:处理成功   FAILED:处理失败
	 */
    private String status;

    private Integer priority;

    private String tradeType;

    private String tradeState;

    private String stateInput;

    private Date createTime;

    private Date updateTime;

    private Integer version;
    
	private String relatedOrderNo;
    
    private String bussNo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeState() {
		return tradeState;
	}

	public void setTradeState(String tradeState) {
		this.tradeState = tradeState;
	}

	public String getStateInput() {
		return stateInput;
	}

	public void setStateInput(String stateInput) {
		this.stateInput = stateInput;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getRelatedOrderNo() {
		return relatedOrderNo;
	}

	public void setRelatedOrderNo(String relatedOrderNo) {
		this.relatedOrderNo = relatedOrderNo;
	}

	public String getBussNo() {
		return bussNo;
	}

	public void setBussNo(String bussNo) {
		this.bussNo = bussNo;
	}
    
	public boolean isRunnable(){
		return "PROC".equals(this.status);
	}
    
}
