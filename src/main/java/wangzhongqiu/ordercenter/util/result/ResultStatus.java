package wangzhongqiu.ordercenter.util.result;

/**
 * 
 * @version 1.0
 * @date 2015/3/11 12:23
 */
public enum ResultStatus {
    SUCCESS(0, "成功"),

    // ------------------系统错误-----------------------
    SYSTEM_ERROR(1000, "系统错误"),
    SYSTEM_DB_ERROR(1001, "数据库系统错误"),
    APPID_NOTEXISTED(1500, "应用ID不存在"),
    SIGNATURE_ERROR(1510, "签名错误"),
    CAPTCHA_ERROR(1600, "验证码错误"),
    CAPTCHA_REQUIRED(1601, "验证码必需"),
    PARAM_ERROR(1602, "参数异常"),
    PARAM_RECHARGE_AMOUNT_ERROR(1603, "充值金额最少1.0元"),
    // ------------------业务异常-------------------
    ORDER_STATUS_ERROR(2000,"订单非执行中状态"),
    ORDER_RULE_NOTFOUND(2001, "无规则引擎"),
    ORDER_NOTFOUND(3001, "此订单号不存在"),
    ORDER_UNSUCCESS(3002, "订单失败"),
    ORDER_UNKNOW(3003, "订单状态位置"),
    REPAY_UNSUCCESS(3004, "订单状态位置"),
    INPROCESS(2,"快捷充值正在处理");
    private int code;
    private String message;

    ResultStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 状态信息
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 状态代码字符串
     */
    public String getName() {
        return this.name();
    }

    /**
     * 向外部输出的状态代码字符串
     */
    public String getOutputName() {
        return this.name();
    }

    @Override
    public String toString() {
        return getName();
    }
}
