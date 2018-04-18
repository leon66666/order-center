package wangzhongqiu.ordercenter.util.result;

import java.io.Serializable;

public class ResultBasicBean<T> extends Result implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 3821513127373587099L;

	protected ResultBasicBean(ResultStatus status, String message) {
		super(status, message);
	}

	public static <T> ResultBasicBean<T> build() {
        return new ResultBasicBean<T>(ResultStatus.SUCCESS, null);
    }
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    /**
     * 成功
     */
    public void success(T value) {
        this.success();
        this.value = value;
    }
}
