package wangzhongqiu.ordercenter.util.result;


/**
 * 
 */
public abstract class Result {
    private ResultStatus status;
    private String message;

    protected Result(ResultStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * 提供静态方法检测是否成功，避免null值无法检测自身的情况
     *
     * @param result
     * @return 是否成功状态
     */
    public static boolean isSuccess(Result result) {
        return result != null && result.status == ResultStatus.SUCCESS;
    }

    /**
     * 同withStatus()方法，代替原方法
     *
     * @param status ResultStatus状态值
     * @return 当前实例
     */
    public Result withError(ResultStatus status) {
        this.status = status;
        return this;
    }

    /**
     * 设置状态为ResultStatus.SUCCESS
     *
     * @return 当前实例
     */
    public Result success() {
        this.status = ResultStatus.SUCCESS;
        return this;
    }

    /**
     * 增加信息覆盖ResultStatus的默认Message
     *
     * @param message
     * @return 当前实例
     */
    public Result withMessage(String message) {
        this.message = message;
        return this;
    }


    //public abstract Object getData();


    public ResultStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message == null ? status.getMessage() : message;
    }

}
