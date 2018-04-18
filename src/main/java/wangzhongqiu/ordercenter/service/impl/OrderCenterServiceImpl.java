package wangzhongqiu.ordercenter.service.impl;

import wangzhongqiu.ordercenter.bo.OrderBo;
import wangzhongqiu.ordercenter.entity.Order;
import wangzhongqiu.ordercenter.entity.OrderLog;
import wangzhongqiu.ordercenter.entity.OrderRule;
import wangzhongqiu.ordercenter.logic.OrderLogLogic;
import wangzhongqiu.ordercenter.logic.OrderLogic;
import wangzhongqiu.ordercenter.logic.OrderRuleLogic;
import wangzhongqiu.ordercenter.service.OrderCenterService;
import wangzhongqiu.ordercenter.util.Constant;
import wangzhongqiu.ordercenter.util.result.OrderResultBean;
import wangzhongqiu.ordercenter.util.result.ResultBasicBean;
import wangzhongqiu.ordercenter.util.result.ResultBean;
import wangzhongqiu.ordercenter.util.result.ResultListBean;
import wangzhongqiu.ordercenter.util.result.ResultStatus;
import wangzhongqiu.ordercenter.util.utils.DateUtil;
import wangzhongqiu.ordercenter.util.utils.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 订单服务处理类
 */
@Service
public class OrderCenterServiceImpl implements OrderCenterService {

    private static Logger logger = Logger.getLogger(OrderCenterServiceImpl.class);

    @Autowired
    private OrderLogic logic;
    @Autowired
    private OrderRuleLogic ruleLogic;
    @Autowired
    private OrderLogLogic orderLogLogic;
    @Autowired
    private PlatformTransactionManager orderTransactionManager;
    @Autowired
    private TransactionDefinition txDefinition;

    public ResultBean<Order> getOrder(String orderNo) {
        ResultBean<Order> resultBean = ResultBean.build();
        try {
            Order order = logic.getByNo(orderNo);
            resultBean.success(order);
        } catch (Exception e) {
            logger.error("getOrder:" + orderNo, e);
            resultBean.withError(ResultStatus.SYSTEM_ERROR);
        }
        return resultBean;
    }

    public ResultBean<Order> newOrder(OrderBo bo) {
        logger.info("new order " + bo.getOrderNo() + " begin " + DateUtil.format(new Date(), DateUtil.DATE_FORMAT_MILLS_SHORT));
        ResultBean<Order> bean = ResultBean.build();
        Order o = new Order();
        Date date = new Date();
        o.setCreateTime(date);
        o.setOrderNo(bo.getOrderNo());
        o.setStateInput(bo.getInitStateInput());
        o.setTradeState(bo.getInitTradeState());
        o.setTradeType(bo.getTradeType());
        o.setUpdateTime(date);
        o.setVersion(0);
        if (!StringUtils.isEmpty(bo.getStatus())) {
            o.setStatus(bo.getStatus());
        } else {
            o.setStatus(Constant.ORDERSTATUS_PROC);
        }
        //事务
        TransactionStatus txStatus = orderTransactionManager.getTransaction(txDefinition);
        try {
            if (StringUtils.isNotBlank(bo.getRelatedOrderNo())) {
                //更新原订单
                logic.updateOrderRelate(bo.getRelatedOrderNo(), bo.getOrderNo());
            }
            logic.insertOrder(o);
            orderTransactionManager.commit(txStatus);
            logger.info("new order " + bo.getOrderNo() + " commit " + DateUtil.format(new Date(), DateUtil.DATE_FORMAT_MILLS_SHORT));
//            Thread.sleep(1000);
        } catch (Exception e) {
            logger.error("newOrder:" + bo.getOrderNo(), e);
            if (txStatus != null)
                orderTransactionManager.rollback(txStatus);
            bean.withError(ResultStatus.SYSTEM_ERROR);
            throw e;
        }
        bean.success(o);
        return bean;
    }

    /**
     * orderNo 对应的是批量相关接口中的 detail_no
     *
     * @param orderNo 订单号
     */
    @SuppressWarnings("rawtypes")
    public OrderResultBean processOrder(String orderNo) {
        OrderResultBean result = OrderResultBean.build();
        Order o = logic.getByNo(orderNo);
        if (null == o || !o.getStatus().equals("PROC")) {
            System.out.println("order is null " + DateUtil.format(new Date(), DateUtil.DATE_FORMAT_MILLS_SHORT));
            logger.warn("订单非执行中状态,订单号:" + orderNo + "当前状态:" + o.getStatus());
            result.withError(ResultStatus.ORDER_STATUS_ERROR);
            return result;
        }
        OrderRule rule = null;
        try {
            while (null != o && o.isRunnable()) {
                //获得Invoke服务、方法名称
                rule = ruleLogic.getOne(o.getTradeType(), o.getTradeState(), "");
                if (null != rule) {
                    logger.info("Invoke服务入参：orderNo = " + o.getOrderNo() + ", currStateService = " + rule.getCurrStateService() + ", stateInput = "
                            + o.getStateInput());
                    result = callService(rule.getCurrStateService(),
                            o.getStateInput());
                    logger.info("Invoke服务返回结果：orderNo = " + o.getOrderNo() + ", currStateService = " + rule.getCurrStateService() + ",currStateOutput = "
                            + result.getCurrStateOutput() + ", nextStateInput = " + result.getNextStateInput());

                    if (!OrderResultBean.isSuccess(result)) {
                        result.withError(result.getStatus());
                        return result;
                    }
                    ResultBean<Order> doOrderResult = doOrder(o, rule, result);
                    if (!ResultBean.isSuccess(doOrderResult)) {
                        result.withError(doOrderResult.getStatus());
                        return result;
                    }
                    o = doOrderResult.getValue();
                } else {
                    logger.error("无规则引擎，tradeType:" + o.getTradeType() + ",tradeStatus:" + o.getTradeState());
                    result.withError(ResultStatus.ORDER_RULE_NOTFOUND);
                    return result;
                }
            }
        } catch (Exception e) {
            logger.error("processOrder:" + orderNo, e);
            result.withError(ResultStatus.SYSTEM_ERROR);
            return result;
        }
        return result;
    }

    /**
     * @param orderNo    业务订单号
     * @param stateInput 当前订单状态所需要的
     * @return
     */
    public OrderResultBean resumeOrder(String orderNo, String stateInput, String... bussNo) {
        OrderResultBean result = OrderResultBean.build();
        Order o = logic.getByNo(orderNo);
        if (null == o || !o.getStatus().equals(Constant.ORDERSTATUS_SUSP)) {
            logger.warn("订单非挂起状态,订单号:" + orderNo + "当前状态:" + o.getStatus());
            result.withError(ResultStatus.ORDER_STATUS_ERROR);
            return result;
        }
        o.setStatus(Constant.ORDERSTATUS_PROC);
        if (null != stateInput) {
            o.setStateInput(stateInput);
        }
        if (null != bussNo && bussNo.length > 0) {
            o.setBussNo(bussNo[0]);
        } else {
            o.setBussNo(orderNo);
        }
        logic.updateSuspOrder(o);
        return processOrder(orderNo);
    }

    @SuppressWarnings("rawtypes")
    public OrderResultBean retryOrder(String orderNo) {
        OrderResultBean result = OrderResultBean.build();
        if (StringUtils.isBlank(orderNo)) {
            logger.warn("orderNo is empty");
            result.withError(ResultStatus.PARAM_ERROR);
            return result;
        }

        Order o = logic.getByNo(orderNo);
        if (o == null) {
            logger.warn("query order return null.");
            result.withError(ResultStatus.PARAM_ERROR);
            return result;
        }

        if (Constant.ORDERSTATUS_SUSP.equals(o.getStatus())) {
            o.setStatus(Constant.ORDERSTATUS_PROC);
            logic.updateSuspOrder(o);
        }

        OrderRule rule = null;
        try {
            while (o != null) {
                if (Constant.ORDERSTATUS_DONE.equals(o.getStatus())) {
                    result.success();
                    return result;
                }
                // 获得Invoke服务、方法名称
                rule = ruleLogic.getOne(o.getTradeType(), o.getTradeState(), "");
                if (null != rule) {
                    logger.info("Invoke服务入参：orderNo = " + o.getOrderNo() + ", currStateService = " + rule.getCurrStateService() + ", stateInput = "
                            + o.getStateInput());
                    result = callService(rule.getCurrStateService(), o.getStateInput());
                    logger.info("Invoke服务返回结果：orderNo = " + o.getOrderNo() + ", currStateService = " + rule.getCurrStateService() + ",currStateOutput = "
                            + result.getCurrStateOutput() + ", nextStateInput = " + result.getNextStateInput());
                    if (result.getCurrStateException() != null) {
                        if (!result.getCurrStateException().getMessage().startsWith("BORROWER")) {
                            throw result.getCurrStateException();
                        }
                    }
                    ResultBean<Order> doOrderResult = doOrder(o, rule, result);
                    if (!ResultBean.isSuccess(doOrderResult)) {
                        result.withError(doOrderResult.getStatus());
                        return result;
                    }
                    if (result.getCurrStateException() != null) {
                        if (result.getCurrStateException().getMessage().startsWith("BORROWER")) {
                            throw result.getCurrStateException();
                        }
                    }
                    o = doOrderResult.getValue();
                } else {
                    logger.error("无规则引擎，tradeType:" + o.getTradeType() + ",tradeStatus:" + o.getTradeState());
                    result.withError(ResultStatus.ORDER_RULE_NOTFOUND);
                    o.setStatus(Constant.ORDERSTATUS_FAILED);
                    o.setTradeState(ResultStatus.ORDER_RULE_NOTFOUND.name());
                    o.setUpdateTime(new Date());
                    logic.updateOrder(o);
                    return result;
                }
            }
        } catch (Exception e) {
            logger.error("retryOrder:" + orderNo, e);
            result.withError(ResultStatus.SYSTEM_ERROR);
            return result;
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    public OrderResultBean cancelOrder(String orderNo) {
        OrderResultBean result = OrderResultBean.build();
        if (StringUtils.isBlank(orderNo)) {
            logger.warn("orderNo is empty");
            result.withError(ResultStatus.PARAM_ERROR);
            return result;
        }

        Order order = logic.getByNo(orderNo);
        if (null == order) {
            logger.warn(String.format("%s, orderNo: %s", ResultStatus.ORDER_NOTFOUND.getMessage(), orderNo));
            result.withError(ResultStatus.ORDER_NOTFOUND);
            return result;
        }

        Date now = Calendar.getInstance().getTime();
        Order o = new Order();
        o.setOrderNo(orderNo);
        o.setStatus(Constant.ORDERSTATUS_CANCEL);
        o.setUpdateTime(now);

        TransactionStatus txStatus = orderTransactionManager.getTransaction(txDefinition);
        try {
            logic.updateOrder(o);
            orderTransactionManager.commit(txStatus);
        } catch (Exception e) {
            logger.error("cancelOrder:" + orderNo, e);
            if (null != txStatus)
                orderTransactionManager.rollback(txStatus);
            result.withError(ResultStatus.SYSTEM_DB_ERROR);
        }

        return result;
    }

    public ResultBasicBean<String> updateOrderBussNo(String orderNo, String bussNo) {
        ResultBasicBean<String> result = ResultBasicBean.build();
        try {
            if (StringUtils.isNotBlank(bussNo)) {
                Order o = new Order();
                o.setBussNo(bussNo);
                o.setOrderNo(orderNo);
                logic.updateOrder(o);
            }
        } catch (Exception e) {
            logger.error("updateOrderBussNo:" + orderNo, e);
            result.withError(ResultStatus.SYSTEM_ERROR);
        }
        return result;
    }

    private ResultBean<Order> doOrder(Order o, OrderRule rule, OrderResultBean processResult) {
        logger.info("进入doOrder : orderNo = " + o.getOrderNo() + ", currentStateService = " + rule.getCurrStateService());
        ResultBean<Order> result = ResultBean.build();
        try {
            // 将当前处理工作加入LOG
            OrderLog log = createOrderLog(o, processResult, rule);
            orderLogLogic.insertOrderLog(log);
            // 取下一条 流程
            rule = ruleLogic.getOne(rule.getTradeType(), rule.getCurrTradeState(), processResult.getCurrStateOutput());
            String orderNo = o.getOrderNo();
            if (null != rule) {
                o.setStatus(rule.getNextStatus());
                o.setTradeState(rule.getNextTradeState());
                if (!StringUtils.isEmpty(processResult.getNextStateInput()))
                    o.setStateInput(processResult.getNextStateInput());
                if (StringUtils.isNotBlank(processResult.getBussNo()))
                    o.setBussNo(processResult.getBussNo());
                o.setUpdateTime(new Date());
                logic.updateOrder(o);
            } else {
                logger.error("无规则引擎，tradeTye:" + o.getTradeType() + ",tradeStatus:" + o.getTradeState() + ",CurrStateOutput:" + processResult.getCurrStateOutput());
                result.withError(ResultStatus.ORDER_RULE_NOTFOUND);
                return result;
            }
            result.setValue(o);
        } catch (Exception e) {
            logger.error("doOrder:" + o.getOrderNo(), e);
            result.withError(ResultStatus.SYSTEM_ERROR);
            return result;
        }
        return result;
    }

    //反射业务方法
    private OrderResultBean<Object> callService(String serviceSignature, String stateInput) {
        String regex = ".+\\..+";
        if (!serviceSignature.matches(regex)) {
        }
        String[] serviceMethod = serviceSignature.split("\\.");
        String serviceName = serviceMethod[0];
        String methodName = serviceMethod[1];

        Object o = SpringContextUtil.getInstance().getBean(serviceName);
        Class clazz = o.getClass();
        Method method = ReflectionUtils.findMethod(clazz, methodName, String.class);
        logger.info("callService invoking " + serviceName + "." + methodName);
        return (OrderResultBean<Object>) ReflectionUtils.invokeMethod(method, o, stateInput);
    }

    private OrderLog createOrderLog(Order o, OrderResultBean processResult, OrderRule rule) {
        OrderLog log = new OrderLog();
        log.setOrderId(o.getId());
        log.setPriority(5);
        log.setStateInput(o.getStateInput());
        log.setStateOutput(processResult.getCurrStateOutput());
        log.setStatus(o.getStatus());
        log.setTradeType(rule.getTradeType());
        log.setTradeState(rule.getCurrTradeState());
        log.setUpdateTime(new Date());
        return log;
    }

    /**
     * 销毁订单
     *
     * @param orderNo
     * @return
     */
    public OrderResultBean destroyOrder(String orderNo) {
        OrderResultBean resultBean = OrderResultBean.build();
        Date now = new Date();
        Order o = new Order();
        o.setOrderNo(orderNo);
        o.setStatus(Constant.ORDERSTATUS_DONE);
        o.setUpdateTime(now);
        TransactionStatus txStatus = orderTransactionManager.getTransaction(txDefinition);
        try {
            logic.updateOrder(o);
            orderTransactionManager.commit(txStatus);
        } catch (Exception e) {
            logger.error("destroyOrder:" + orderNo, e);
            if (null != txStatus)
                orderTransactionManager.rollback(txStatus);
            resultBean.withError(ResultStatus.SYSTEM_DB_ERROR);
        }
        return resultBean;
    }

    /**
     * 还款业务使用
     *
     * @param rule
     * @return
     */
    private boolean isRepay(OrderRule rule) {
        boolean isRepay = false;
        String tradeType = rule.getTradeType();
        if ("AUTO_REPAY".equals(tradeType) || "API_RECHARGE".equals(tradeType)
                || "CHANNEL_RECHARGE".equals(tradeType) || "AMQUE_RECHARGE".equals(tradeType)) {
            isRepay = true;
        }
        return isRepay;
    }

    //判断是否是债权业务
    private boolean isTransferType(String tradeState) {
        boolean flag = false;
        if (Constant.LOAN_TRANSFER_MAP.containsKey(tradeState)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 按条件批量查询订单
     *
     * @param createTimeBefore 在此时间前创建
     * @param retryTime        重试次数
     * @return
     */
    public ResultListBean<Order> getOrderListByTradeTypeAndStatus(Date createTimeBefore, Integer retryTime, int pageSize) {
        ResultListBean<Order> resultListBean = ResultListBean.build();
        try {
            List<Order> orderList = logic.getOrderListByTradeTypeAndStatus(createTimeBefore, retryTime, pageSize);
            resultListBean.success(orderList);
        } catch (Exception e) {
            e.printStackTrace();
            resultListBean.withError(ResultStatus.SYSTEM_ERROR);
        }
        return resultListBean;
    }

    @Override
    public OrderResultBean updateOrder(Order order) {
        OrderResultBean resultBean = OrderResultBean.build();
        try {
            logic.updateOrder(order);
        } catch (Exception e) {
            logger.error("updateOrder:" + order.getOrderNo(), e);
            resultBean.withError(ResultStatus.SYSTEM_DB_ERROR);
        }
        return resultBean;
    }
}
