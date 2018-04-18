package wangzhongqiu.ordercenter.util.result;


/**
 * 订单处理结果
 * Zhu, Haomin 2015/10/31
 */
public class OrderResultBean<T> extends Result{

	protected OrderResultBean(ResultStatus status, String message) {
		super(status, message);
	}
	private String nextStateInput;

    private String currStateOutput;

    private T currStateResult;
    
    private Exception currStateException;
    
    private String bussNo;
    
    private String content;

    public static <T> OrderResultBean<T> build() {
        return new OrderResultBean<T>(ResultStatus.SUCCESS, null);
    }

    public String getNextStateInput() {
        return nextStateInput;
    }

    public void setNextStateInput(String nextStateInput) {
        this.nextStateInput = nextStateInput;
    }

    public String getCurrStateOutput() {
        return currStateOutput;
    }

    public void  setCurrStateOutput(String currStateOutput) {
        this.currStateOutput = currStateOutput;
    }

    public T getCurrStateResult() {
        return currStateResult;
    }

    public void setCurrStateResult(T currStateResult) {
        this.currStateResult = currStateResult;
    }

	public Exception getCurrStateException() {
		return currStateException;
	}

	public void setCurrStateException(Exception currStateException) {
		this.currStateException = currStateException;
	}

	public String getBussNo() {
		return bussNo;
	}

	public void setBussNo(String bussNo) {
		this.bussNo = bussNo;
	}

	
}
