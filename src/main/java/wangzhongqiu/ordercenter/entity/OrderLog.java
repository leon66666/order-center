package wangzhongqiu.ordercenter.entity;

import java.util.Date;


public class OrderLog {
	
	/**
	 * 自增ID
	 */
    private Integer id;
    
	/**
	 * 业务订单id
	 */
	private Integer orderId;

	/**
	 * 订单状态   PROC:处理中   SUSP:挂起  DONE:处理成功   FAILED:处理失败
	 */
    private String status;

	/**
	 * 优先级(暂未使用)
	 */
    private Integer priority;

	/**
	 *	业务类型
	 */
    private String tradeType;

	/**
	 * 当前状态
	 */
    private String tradeState;

	/**
	 * 执行服务方法输入
	 */
    private String stateInput;

    /**
	 * 执行服务方法输出
	 */
    private String stateOutput;

	/**
	 * 创建/更新时间
	 */
    private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

    public String getStateOutput() {
        return stateOutput;
    }

    public void setStateOutput(String stateOutput) {
        this.stateOutput = stateOutput;
    }

	@Override
	public String toString() {
		return "OrderLog{" +
				"id=" + id +
				", orderId=" + orderId +
				", status='" + status + '\'' +
				", priority=" + priority +
				", tradeType='" + tradeType + '\'' +
				", tradeState='" + tradeState + '\'' +
				", stateInput='" + stateInput + '\'' +
				", stateOutput='" + stateOutput + '\'' +
				", updateTime=" + updateTime +
				'}';
	}
}
