package wangzhongqiu.ordercenter.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单状态规则
 * 
 *
 */
public class OrderRule implements Serializable{

	private Integer id;

    private String tradeType;

    private String currTradeState;
    
    private String currStateService;

    private String currServiceType;

    private String currStateOutput;

    private String nextStatus;

    private Integer nextPriority;

    private String nextTradeState;
    
    private String backTradeType;
    
    private String backTradeState;

    private Date updateTime;
    
    private Integer isRetry;
    
    private Integer coefficient;

    private Integer retryTime;
    
    private Integer isDelay;
    
    private Integer delayTime;
    
	public Integer getIsRetry() {
		return isRetry;
	}

	public void setIsRetry(Integer isRetry) {
		this.isRetry = isRetry;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getCurrTradeState() {
		return currTradeState;
	}

	public void setCurrTradeState(String currTradeState) {
		this.currTradeState = currTradeState;
	}

	public String getCurrStateService() {
		return currStateService;
	}

	public void setCurrStateService(String currStateService) {
		this.currStateService = currStateService;
	}

	public String getCurrServiceType() {
		return currServiceType;
	}

	public void setCurrServiceType(String currServiceType) {
		this.currServiceType = currServiceType;
	}

	public String getCurrStateOutput() {
		return currStateOutput;
	}

	public void setCurrStateOutput(String currStateOutput) {
		this.currStateOutput = currStateOutput;
	}

	public String getNextStatus() {
		return nextStatus;
	}

	public void setNextStatus(String nextStatus) {
		this.nextStatus = nextStatus;
	}

	public Integer getNextPriority() {
		return nextPriority;
	}

	public void setNextPriority(Integer nextPriority) {
		this.nextPriority = nextPriority;
	}

	public String getNextTradeState() {
		return nextTradeState;
	}

	public void setNextTradeState(String nextTradeState) {
		this.nextTradeState = nextTradeState;
	}

	public String getBackTradeType() {
		return backTradeType;
	}

	public void setBackTradeType(String backTradeType) {
		this.backTradeType = backTradeType;
	}

	public String getBackTradeState() {
		return backTradeState;
	}

	public void setBackTradeState(String backTradeState) {
		this.backTradeState = backTradeState;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getIsDelay() {
		return isDelay;
	}

	public void setIsDelay(Integer isDelay) {
		this.isDelay = isDelay;
	}

	public Integer getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(Integer delayTime) {
		this.delayTime = delayTime;
	}

	public Integer getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(Integer coefficient) {
		this.coefficient = coefficient;
	}

	public Integer getRetryTime() {
		return retryTime;
	}

	public void setRetryTime(Integer retryTime) {
		this.retryTime = retryTime;
	}
    
}
