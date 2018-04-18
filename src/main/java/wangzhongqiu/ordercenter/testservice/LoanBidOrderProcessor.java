package wangzhongqiu.ordercenter.testservice;

import com.alibaba.fastjson.JSONObject;
import com.hoomsun.common.CouponType;
import com.hoomsun.common.InvestType;
import com.hoomsun.common.TradeMethodType;
import com.hoomsun.common.financeplan.OrderRetryStatus;
import com.hoomsun.model.*;
import com.hoomsun.model.financeplan.FinancePlanSubPoint;
import com.hoomsun.model.financeplan.OrderRetry;
import com.hoomsun.ordercenter.EscrowSdkUtil;
import com.hoomsun.ordercenter.SupervisionInterface;
import com.hoomsun.ordercenter.SupervisionOrderId;
import com.hoomsun.ordercenter.sdk.request.UserPreTransactionRequest;
import com.hoomsun.ordercenter.sdk.response.UserPreTransactionResponse;
import com.hoomsun.ordercenter.util.result.OrderResultBean;
import com.hoomsun.service.AccountService;
import com.hoomsun.service.base.FinancePlanSubPointBaseService;
import com.hoomsun.service.base.LoanBaseService;
import com.hoomsun.service.base.PointBaseService;
import com.hoomsun.service.base.UserBaseService;
import com.hoomsun.service.base.impl.LoanBaseServiceImpl;
import com.hoomsun.service.cache.RedisService;
import com.hoomsun.service.capitalLog.CapitalLogService;
import com.hoomsun.service.core.LoanBidService;
import com.hoomsun.service.coupon.CouponServices;
import com.hoomsun.service.escrow.AccountBalanceService;
import com.hoomsun.service.financeplan.FinancePlanSubPointServices;
import com.hoomsun.service.financeplan.OrderRetryServices;
import com.hoomsun.service.loan.LoanBusService;
import com.hoomsun.service.order.OrderService;
import com.hoomsun.util.CalculateUtil;
import com.hoomsun.util.redis.RedisResultFlag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class LoanBidOrderProcessor {

    public enum OUTPUT {
        SUCCESS() {
            @Override
            public String toString() {
                return "SUCCESS";
            }
        },
        FAILED() {
            @Override
            public String toString() {
                return "FAILED";
            }
        },
        CANCEL() {
            @Override
            public String toString() {
                return "CANCEL";
            }
        },
        PROCESS() {
            @Override
            public String toString() {
                return "PROCESS";
            }
        },
        FATAL() {
            @Override
            public String toString() {
                return "FATAL";
            }
        },
        AMOUNT_NOT_ENOUGH() {
            @Override
            public String toString() {
                return "AMOUNT_NOT_ENOUGH";
            }
        }
    }

    @Autowired
    private LoanBaseService loanBaseService;

    @Autowired
    private LoanBaseServiceImpl loanBaseServiceImpl;
    @Autowired
    private LoanBidService loanBidService;

    @Autowired
    private UserBaseService userBaseService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CouponServices couponServices;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AccountBalanceService accountBalanceService;

    @Autowired
    private CapitalLogService capitalLogService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRetryServices orderRetryService;

    @Autowired
    private PointBaseService pointService;

    @Autowired
    private LoanBusService loanBusService;
    @Autowired
    private FinancePlanSubPointBaseService subpointService;
    @Autowired
    private FinancePlanSubPointServices financePlanSubPointServices;

    private static Log log = LogFactory.getLog(LoanBidOrderProcessor.class);

    public String generateOrderNo(Integer loanId, Integer userId) {
        return SupervisionOrderId.genOrderId(SupervisionOrderId.INTERFACE_ORDERID.get(SupervisionInterface.investApplyByCust), SupervisionOrderId.CustomType.LOAN_BID, userId,
                SupervisionOrderId.BusinessType.LOAN, loanId);
    }

    /**
     * 组装参数发请求到存管
     * <p/>
     * 不考虑同步
     * 业务不可重复发起
     * 业务不可重复执行
     * 接口不可重复执行
     *
     * @param stateInput 参数
     * @return
     */
    public OrderResultBean<UserPreTransactionRequest> prepareEscrowRedirection(String stateInput) {
        OrderResultBean result = OrderResultBean.build();
        UserPreTransactionRequest request = new UserPreTransactionRequest();
        UserPreTransactionResponse response = new UserPreTransactionResponse();
        String orderNo;
        String detailNo;
        Integer loanId;
        Double amount;
        Integer userId;
        Integer couponId;
        JSONObject stateInputJSON = JSONObject.parseObject(stateInput);
        Integer subPointId = null;
        Account account = null;

        //前置校验
        try {
            orderNo = stateInputJSON.getString("orderNo");
            detailNo = stateInputJSON.getString("detailNo");
            loanId = stateInputJSON.getInteger("loanId");
            amount = stateInputJSON.getDouble("amount");
            userId = stateInputJSON.getInteger("userId");
            couponId = stateInputJSON.getInteger("couponId");
            subPointId = stateInputJSON.getInteger("subPointId");
            Loan loan = loanBaseService.loadLoan(loanId);
            User user = userBaseService.loadUser(userId);
            account = accountService.getAccount(user.getAccountId());
            if (subPointId == 0 || subPointId == null) {
                loanBaseService.checkBiddable(loanId, amount, userId, InvestType.WEB);
            } else {
                FinancePlanSubPoint subpoint = subpointService.loadSubpoint(subPointId);
                loanBaseServiceImpl.checkBiddable(loan, amount, subpoint, user, InvestType.FINANCEPLAN);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCurrStateOutput(OUTPUT.FAILED.name());
            result.setCurrStateResult(OUTPUT.FAILED.name());
            result.setCurrStateException(e);
            log.error("投标请求存管之前发生异常" + e.getMessage());
            result.setNextStateInput(stateInputJSON.toJSONString());
            return result;
        }


        //计划投标之前发送恒丰存管（解冻计划投标金额）
        /*try {
            FundFreezeRequest fundFreezeRequest = new FundFreezeRequest();
            FundFreezeResponse fundFreezeResponse = new FundFreezeResponse();
            if (subPointId > 0) {
                FinancePlanSubPoint fp = financePlanSubPointServices.getFinancePlanSubPointById(subPointId);
                //订单号
                fundFreezeRequest.setOrder_no(EscrowUtil.genOrderNo(fundFreezeRequest.getOrderPrefix(), BusinessType.FINANCEPLAN, subPointId, BusinessType.USER, userId));
                fundFreezeRequest.setAmount(amount.toString());
                fundFreezeRequest.setPartner_trans_time(new SimpleDateFormat("HHmmss").format(new Date()));
                fundFreezeRequest.setPartner_trans_date(new SimpleDateFormat("yyyyMMdd").format(new Date()));
                fundFreezeRequest.setFreeze_flg("02");
                fundFreezeRequest.setRemark("计划投标解冻");
                fundFreezeRequest.setPlatcust(account.getFundAcc().toString());
                fundFreezeRequest.setFreeze_order_no(fp.getProtocolNo());
                fundFreezeResponse = EscrowSdkUtil.execRequest(fundFreezeRequest, redisService);
                if (!fundFreezeResponse.isSuccess()) {
                    throw new Exception("计划投标解冻存管返回失败");
                }
            }
        } catch (Exception e) {
            if (e.getMessage().equals("计划投标解冻存管返回失败")) {
                result.setCurrStateOutput(OUTPUT.FAILED.name());
                result.setCurrStateResult(OUTPUT.FAILED.name());
                log.error(e.getMessage());
                result.setNextStateInput(stateInputJSON.toJSONString());
                return result;
            } else {
                //记录人工处理表
                result.setCurrStateOutput(OUTPUT.FATAL.name());
                result.setCurrStateResult(OUTPUT.FATAL.name());
                result.setCurrStateException(e);
                log.error("计划自动投标，解冻资金异常" + e.getMessage());
                result.setNextStateInput(stateInputJSON.toJSONString());
                return result;
            }
        }*/

        //计划投标之前发送恒丰存管（解冻计划投标金额）(废弃)
//        try {
//            FundFreezeRequest fundFreezeRequest = new FundFreezeRequest();
//            FundFreezeResponse fundFreezeResponse = new FundFreezeResponse();
//            if (subPointId > 0) {
//                FinancePlanSubPoint fp = financePlanSubPointServices.getFinancePlanSubPointById(subPointId);
//                //订单号
//                fundFreezeRequest.setOrder_no(EscrowUtil.genOrderNo(fundFreezeRequest.getOrderPrefix(), BusinessType.FINANCEPLAN, subPointId, BusinessType.USER, userId));
//                fundFreezeRequest.setAmount(amount.toString());
//                fundFreezeRequest.setPartner_trans_time(new SimpleDateFormat("HHmmss").format(new Date()));
//                fundFreezeRequest.setPartner_trans_date(new SimpleDateFormat("yyyyMMdd").format(new Date()));
//                fundFreezeRequest.setFreeze_flg("02");
//                fundFreezeRequest.setRemark("计划投标解冻");
//                fundFreezeRequest.setPlatcust(account.getFundAcc().toString());
//                fundFreezeRequest.setFreeze_order_no(fp.getProtocolNo());
//                fundFreezeResponse = EscrowSdkUtil.execRequest(fundFreezeRequest, redisService);
//                if (!fundFreezeResponse.isSuccess()) {
//                    throw new Exception("计划投标解冻存管返回失败");
//                }
//            }
//        } catch (Exception e) {
//            if (e.getMessage().equals("计划投标解冻存管返回失败")) {
//                result.setCurrStateOutput(OUTPUT.FAILED.name());
//                result.setCurrStateResult(OUTPUT.FAILED.name());
//                log.error(e.getMessage());
//                result.setNextStateInput(stateInputJSON.toJSONString());
//                return result;
//            } else {
//                //记录人工处理表
//                result.setCurrStateOutput(OUTPUT.FATAL.name());
//                result.setCurrStateResult(OUTPUT.FATAL.name());
//                result.setCurrStateException(e);
//                log.error("计划自动投标，解冻资金异常" + e.getMessage());
//                result.setNextStateInput(stateInputJSON.toJSONString());
//                return result;
//            }
//        }


        //往存管发起请求之前
        try {
            //券相关暂时没用
            BigDecimal value = new BigDecimal(0);
            if (null != couponId) {
                Coupon coupon = couponServices.getCouponById(couponId);
                CouponBatch cb = couponServices.getCouponBatchByCoupon(couponId);

                //如果是抵扣券 则计算抵扣金额
                if (CouponType.DISCOUNT.equals(cb.getCouponType())) {
                    value = CalculateUtil.caculateActualValueForDiscountCoupon(coupon.getDiscountRate(), new BigDecimal(amount), cb.getMaxDiscountAmount());
                }
                /*//如果是现金券 则计算金额
                if (CouponType.VOUCHER.equals(cb.getCouponType())) {
                    value = couponServices.getCouponValue(couponId);
                    value = value.compareTo(new BigDecimal(amount)) >= 0 ? new BigDecimal(amount) : value;
                }*/

                //如果是满减卷 则计算金额
                if (CouponType.MONEY_OFF.equals(cb.getCouponType())) {
                    value = coupon.getDerateAmount();
                }
            } else {
                value = new BigDecimal(0);
            }

            //// TODO: 2018/4/10  王中秋 懒猫
           /* //散标优惠券，调用恒丰接口，增加优惠券参数
            //请求恒丰存管批量投标接口
           /* request.setOrder_no(orderNo);//订单号，全局唯一
            request.setProd_id(loanId.toString());//标的ID
            request.setTotal_num("1");
            List<BuybatchExtRequest.Data> datas = new ArrayList<BuybatchExtRequest.Data>();
            BuybatchExtRequest.Data data = request.new Data();
            data.setDetail_no(detailNo);//全局唯一
            data.setPlatcust(account.getFundAcc().toString());
            data.setTrans_amt((amount + value.doubleValue()) + "");
            data.setSelf_amt(amount + "");
            data.setExperience_amt("0.00");
            data.setCoupon_amt(value.doubleValue() + "");
            data.setSubject_priority("0");//可提优先
            datas.add(data);
            request.setData(datas);*/
        } catch (Exception e) {
            result.setCurrStateOutput(OUTPUT.FAILED.name());
            result.setCurrStateResult(OUTPUT.FAILED.name());
            result.setCurrStateException(e);
            log.error("投标请求存管之前发生异常" + e.getMessage());
            result.setNextStateInput(stateInputJSON.toJSONString());
            return result;
        }


        //向存管发起请求
        try {
            response = EscrowSdkUtil.execRequest(request, redisService);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                //等价于向存管发起请求之前出错
                if (e.getMessage().contains("errPercent:" + request.getClass().getSimpleName() + "_before")) {
                    result.setCurrStateOutput(OUTPUT.FAILED.name());
                    result.setCurrStateResult(OUTPUT.FAILED.name());
                    result.setCurrStateException(e);
                    log.error("投标请求存管之前发生异常" + e.getMessage());
                    result.setNextStateInput(stateInputJSON.toJSONString());
                    return result;
                }
                //向存管发起请求，但是收到response之前报错。等价于向存管发起请求之后发生异常
                if (e.getMessage().contains("errPercent:" + request.getClass().getSimpleName() + "_after")) {
                    result.setCurrStateOutput(OUTPUT.PROCESS.name());
                    result.setCurrStateResult(OUTPUT.PROCESS.name());
                    log.error("投标收到存管请求之后发生异常" + e.getMessage());
                    result.setNextStateInput(stateInputJSON.toJSONString());
                    return result;
                }
            }
        }

        //向存管发起请求之后
        try {
            //// TODO: 2018/4/10  王中秋 懒猫
            /*if (response.isSuccess() && response.getSuccess_num().equals(response.getTotal_num())) {
                result.setCurrStateOutput(OUTPUT.SUCCESS.name());
                result.setCurrStateResult(OUTPUT.SUCCESS.name());
                stateInputJSON.put("escrowTransOutputType", OUTPUT.SUCCESS.name());
            } else {
                result.setCurrStateOutput(OUTPUT.FAILED.name());
                result.setCurrStateResult(OUTPUT.FAILED.name());
                stateInputJSON.put("escrowTransOutputType", OUTPUT.FAILED.name());
            }*/
            result.setNextStateInput(stateInputJSON.toJSONString());
            return result;
        } catch (Exception e) {
            result.setCurrStateOutput(OUTPUT.PROCESS.name());
            result.setCurrStateResult(OUTPUT.PROCESS.name());
            log.error("投标收到存管请求之后发生异常" + e.getMessage());
            result.setNextStateInput(stateInputJSON.toJSONString());
            return result;
        }
    }

    public OrderResultBean escrowCallback(UserPreTransactionResponse response) {
        OrderResultBean result = OrderResultBean.build();
        if (response.getCode().equals("SUCCESS")) {

        }
        if (response.getCode().equals("INIT")) {

        }
        return result;
    }

    public OrderResultBean escrowSelect(String orderNo) {
        OrderResultBean result = OrderResultBean.build();

        return result;
    }

    /**
     * 投标业务处理
     * <p/>
     * 不考虑异步
     * 业务不可重复发起
     * 业务不可重复执行
     * 接口不可重复执行
     *
     * @param stateInput
     * @return
     */
    public OrderResultBean<LoanLenderRecord> getUserEscrowConfirmed(String stateInput) {
        OrderResultBean result = OrderResultBean.build();
        JSONObject stateInputJSON = null;
        String orderNo = null;
        Integer userId = 0;
        Integer loanId = 0;
        Double amount = 0.0;
        String retryStatus = OrderRetryStatus.UNPROCESS;
        try {
            stateInputJSON = JSONObject.parseObject(stateInput);
            String escrowType = stateInputJSON.getString("escrowTransOutputType");
            String escrowMessage = stateInputJSON.getString("escrowTransOutputMessage");
            orderNo = stateInputJSON.getString("orderNo");
            userId = stateInputJSON.getInteger("userId");
            if (OUTPUT.SUCCESS.name().equals(escrowType)) {
                try {
                    RedisResultFlag redisResultFlag = new RedisResultFlag();
                    loanId = stateInputJSON.getInteger("loanId");
                    amount = stateInputJSON.getDouble("amount");
                    Integer couponId = stateInputJSON.getInteger("couponId");
                    Boolean isAuto = stateInputJSON.getBoolean("isAuto");
                    String tradeMethodType = stateInputJSON.getString("tradeMethodType");
                    String bussNo = stateInputJSON.getString("bussNo");
                    User user = userBaseService.loadUser(userId);

                    Integer subPointId = stateInputJSON.getInteger("subPointId");
                    //计划自动投标
                    if (subPointId > 0) {
                        FinancePlanSubPoint fpsp = financePlanSubPointServices.getFinancePlanSubPointById(subPointId);
                        LoanLenderRecord loanlender = null;
                        String tradeType = stateInputJSON.getString("tradeType");
                        try {
                            if (InvestType.FINANCEPLAN.name().equals(tradeType)) {
                                loanlender = loanBidService.bidForFinancePlan(user, loanId, InvestType.FINANCEPLAN, fpsp, amount, redisResultFlag, orderNo, bussNo);
                            } else if (InvestType.AUTO_INVEST_PLAN.name().equals(tradeType)) {
                                loanlender = loanBidService.bidForFinancePlan(user, loanId, InvestType.AUTO_INVEST_PLAN, fpsp, amount, redisResultFlag, orderNo, bussNo);
                            } else {
                                loanlender = loanBidService.bidForFinancePlan(user, loanId, InvestType.FINANCE_PLAN_NEW, fpsp, amount, redisResultFlag, orderNo, bussNo);
                            }
                        } catch (Exception e) {
                            if (e instanceof DataIntegrityViolationException) {
                                log.warn("计划自动投标唯一索引异常 " + e.getMessage() + " " + loanId);
                            } else {
                                throw (e);
                            }
                        }
                        result.setCurrStateOutput(OUTPUT.SUCCESS.name());
                        result.setCurrStateResult(OUTPUT.SUCCESS.name());
                    }
                    //散标投标
                    else {
                        TradeMethodType tmt = TradeMethodType.valueOf(tradeMethodType);
                        try {
                            LoanLenderRecord llr = loanBidService.bid(user, loanId, amount, isAuto, null, tmt, couponId, orderNo, bussNo);
                        } catch (Exception e) {
                            if (e instanceof DataIntegrityViolationException) {
                                log.warn("散标投标唯一索引异常 " + e.getMessage() + " " + loanId);
                            } else {
                                throw (e);
                            }
                        }
                        result.setCurrStateOutput(OUTPUT.SUCCESS.name());
                        result.setCurrStateResult(OUTPUT.SUCCESS.name());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    result.setNextStateInput(stateInputJSON.toJSONString());
                    result.setCurrStateOutput(OUTPUT.PROCESS.name());
                    result.setCurrStateResult(OUTPUT.PROCESS.name());
                    result.setCurrStateException(e);
                    retryStatus = OrderRetryStatus.ESCROW_SUCCESS;
                    try {
                        OrderRetry orderRetry = orderRetryService.getOrderRetryByOrderNo(orderNo);
                        if (orderRetry == null) {
                            log.error("没找到对应的记录" + orderNo);
                        } else if (OrderRetryStatus.SUCCESS.equals(orderRetry.getStatus()) || OrderRetryStatus.ESCROW_FAILED.equals(orderRetry.getStatus())) {
                            log.error("重复处理" + orderNo);
                        } else {
                            orderRetry.setStatus(retryStatus);
                            orderRetry.setUpdateTime(new Date());
                            orderRetryService.updateOrderRetry(orderRetry);
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            result.setNextStateInput(stateInputJSON.toJSONString());
            result.setCurrStateOutput(OUTPUT.PROCESS.name());
            result.setCurrStateResult(OUTPUT.PROCESS.name());
            result.setCurrStateException(e);
            retryStatus = OrderRetryStatus.ESCROW_PROCESS;
            try {
                OrderRetry orderRetry = orderRetryService.getOrderRetryByOrderNo(orderNo);
                if (orderRetry == null) {
                    log.error("没找到对应的记录" + orderNo);
                } else if (OrderRetryStatus.SUCCESS.equals(orderRetry.getStatus()) || OrderRetryStatus.ESCROW_FAILED.equals(orderRetry.getStatus())) {
                    log.error("重复处理" + orderNo);
                } else {
                    orderRetry.setStatus(retryStatus);
                    orderRetry.setUpdateTime(new Date());
                    orderRetryService.updateOrderRetry(orderRetry);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }

    public OrderResultBean cancelLoanBid(String input) {
        OrderResultBean result = OrderResultBean.build();
        try {
            JSONObject stateInputJSON = JSONObject.parseObject(input);
            Integer userId = stateInputJSON.getInteger("userId");
            String orderNo = stateInputJSON.getString("orderNo");
            Double amount = stateInputJSON.getDouble("amount");
            Integer loanId = stateInputJSON.getInteger("loanId");
            //回滚redis锁和投标冻结金额
            //散标优惠券，失败，回滚冻结的优惠券，账户金额
            loanBusService.rollback(orderNo);
            result.setCurrStateOutput(OUTPUT.SUCCESS.name());
            result.setCurrStateResult(OUTPUT.FAILED.name());
            return result;
        } catch (Exception e) {
            result.setCurrStateOutput(OUTPUT.FAILED.name());
            result.setCurrStateResult(OUTPUT.PROCESS.name());
            return result;
        }
    }

    /**
     * 撤单
     * <p/>
     * 同步异步都有
     * 业务不可重复发起
     * 业务不可重复执行
     * 接口不可重复执行
     *
     * @param input
     * @return
     */
/*    public OrderResultBean cancel(String input) {
        CancelApplyByPlatformRequest request = new CancelApplyByPlatformRequest();
        TransInput transInput = new TransInput();
        OrderResultBean result = OrderResultBean.build();
        String orderNo = null;
        String retryStatus = OrderRetryStatus.UNPROCESS;
        try {
            JSONObject stateInputJSON = JSONObject.parseObject(input);
            orderNo = stateInputJSON.getString("orderNo");
            transInput.setOrderId(orderNo);
            request.setAssoOrder(stateInputJSON.getString("relatedOrderNo"));
            request.setTransInput(transInput);
            CancelApplyByPlatformResponse response = EscrowSdkUtil.execRequest(request, redisService);
            String escrowType = response.getTransOutput().getType();
            String escrowMessage = response.getTransOutput().getMessage();
            String bussNo = response.getTransOutput().getBussNo();
            result.setBussNo(bussNo);
            if ("S".equals(escrowType)) {
                result.setCurrStateOutput(OUTPUT.SUCCESS.name());
                retryStatus = OrderRetryStatus.SUCCESS;
            } else if ("E".equals(escrowType)) {
                result.setCurrStateOutput(OUTPUT.FAIL.name());
                retryStatus = OrderRetryStatus.ESCROW_FAILED;
                log.error("撤销自动投标存管返回E:orderNo=" + stateInputJSON.getString("orderNo") + ",message:" + escrowMessage);
            } else if ("R".equals(escrowType)) {
                result.setCurrStateOutput(OUTPUT.FAIL.name());
                retryStatus = OrderRetryStatus.ESCROW_SUCCESS;
                log.warn("撤销自动投标存管返回R:orderNo=" + stateInputJSON.getString("orderNo"));
            } else if ("P".equals(escrowType)) {
                result.setCurrStateOutput(OUTPUT.FAIL.name());
                retryStatus = OrderRetryStatus.UNPROCESS;
                log.info("撤销自动投标存管返回P:orderNo=" + stateInputJSON.getString("orderNo"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCurrStateOutput(OUTPUT.FAIL.name());
            retryStatus = OrderRetryStatus.ESCROW_SUCCESS;
        } finally {
            try {
                OrderRetry orderRetry = orderRetryService.getOrderRetryByOrderNo(orderNo);
                if (orderRetry == null) {
                    log.error("没找到对应的记录" + orderNo);
                } else if (OrderRetryStatus.SUCCESS.equals(orderRetry.getStatus()) || OrderRetryStatus.ESCROW_FAILED.equals(orderRetry.getStatus())) {
                    log.error("重复处理" + orderNo);
                } else {
                    orderRetry.setStatus(retryStatus);
                    orderRetry.setUpdateTime(new Date());
                    orderRetryService.updateOrderRetry(orderRetry);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }*/
}
