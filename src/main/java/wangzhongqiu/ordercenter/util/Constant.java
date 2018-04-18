package wangzhongqiu.ordercenter.util;

import java.util.HashMap;

/**
 * @Description: 常量类
 */
public class Constant {

	/**
	 * 订单状态
	 */
	//初始化
	public static final String ORDERSTATUS_INIT = "INIT";
	//处理中
	public static final String ORDERSTATUS_PROC = "PROC";
	//挂起(跳转页面采用)
	public static final String ORDERSTATUS_SUSP = "SUSP";
	//结束(最终状态)
	public static final String ORDERSTATUS_DONE = "DONE";
	//失败
	public static final String ORDERSTATUS_FAILED = "FAILED";
    // 销毁
    public static final String ORDERSTATUS_CANCEL = "CANCEL";
	
	/**
	 * 任务状态
	 */
	//未执行
	public static final int TASK_STATUS_1 = 1;
	//执行中
	public static final int TASK_STATUS_2 = 2;
	//执行完成
	public static final int TASK_STATUS_3 = 3;
	//执行失败
	public static final int TASK_STATUS_4 = 4;

    // 业务返回finish
    public static final int TASK_STATUS_5 = 5;

	/**
	 * 订单规则使用_常量
	 */
	public static final String DO_SUSP = "DO_SUSP";

	public static final String CONTINUE = "CONTINUE";

	public static final String DELAY = "DELAY";

	/**
	 * 一次重试x条
	 */
	public static final Integer PAGE_SIZE = 500;
	/**
	 * 默认的重试次数小于x次的
	 */
	public static final Integer RETRY_TIME = 20;
	/**
	 * 默认的延迟时间x秒
	 */
	public static final Integer DELAY_TIME = 30;

	/**
	 * 债权业务处理
	 */
	public static final HashMap<String, String> LOAN_TRANSFER_MAP = new HashMap<String, String>() {
		{
			put("DEBT_BUY_CUST_CALLBACK", "DEBT_BUY_BY_CUST");
			put("DEBT_BUY_PLATFORM_CALLBACK", "DEBT_BUY_BY_PLATFORM");
			put("DEBT_BUY_PLATFORM_SYNC", "DEBT_BUY_BY_PLATFORM");

			put("DEBT_APPLY_CUST_CALLBACK", "DEBT_APPLY_BY_CUST");
			put("DEBT_APPLY_PLATFORM_CALLBACK", "DEBT_APPLY_BY_PLATFORM");
			put("DEBT_APPLY_PLATFORM_SYNC", "DEBT_APPLY_BY_PLATFORM");
		}
	};
}
