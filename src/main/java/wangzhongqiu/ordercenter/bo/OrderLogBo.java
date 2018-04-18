package wangzhongqiu.ordercenter.bo;



public class OrderLogBo {
	
	/**
	 * 业务订单号
	 */
	private String orderNo;

	/**
	 * 订单状态   PROC:处理中   SUSP:挂起  DONE:处理成功   FAILED:处理失败
	 */
    private String status;

    private String tradeState;
    
    private String stateOutput;

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

	public String getTradeState() {
		return tradeState;
	}

	public void setTradeState(String tradeState) {
		this.tradeState = tradeState;
	}

    public String getStateOutput() {
        return stateOutput;
    }

    public void setStateOutput(String stateOutput) {
        this.stateOutput = stateOutput;
    }
    
}
