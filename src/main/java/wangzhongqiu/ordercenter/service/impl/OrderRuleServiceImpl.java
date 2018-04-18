package wangzhongqiu.ordercenter.service.impl;

import wangzhongqiu.ordercenter.entity.OrderRule;
import wangzhongqiu.ordercenter.logic.OrderRuleLogic;
import wangzhongqiu.ordercenter.service.OrderRuleService;
import wangzhongqiu.ordercenter.util.result.ResultBean;
import wangzhongqiu.ordercenter.util.result.ResultListBean;
import wangzhongqiu.ordercenter.util.result.ResultStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单规则引擎服务实现类
 */

@Service
public class OrderRuleServiceImpl implements OrderRuleService {

    private static Logger logger = Logger.getLogger(OrderRuleServiceImpl.class);

    @Autowired
    private OrderRuleLogic logic;

    public ResultBean<OrderRule> getOrderRule(String tradeType, String tradeState) {
        ResultBean<OrderRule> resultBean = ResultBean.build();
        try {
            List<OrderRule> list = logic.getOrderRule(tradeType, tradeState);
            if (null != list && list.size() > 0)
                resultBean.success(list.get(0));
        } catch (Exception e) {
            logger.error("getOrderRule, tradeType:" + tradeType + ", tradeState:" + tradeState, e);
            resultBean.withError(ResultStatus.SYSTEM_ERROR);
        }
        return resultBean;
    }

    /**
     * 取得需要重试的订单规则
     *
     * @return
     */
    public ResultListBean<OrderRule> getOrderRuleRetry() {
        ResultListBean<OrderRule> resultListBean = ResultListBean.build();
        try {
            List<OrderRule> list = logic.getOrderRuleRetry();
            if (null != list && list.size() > 0)
                resultListBean.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            resultListBean.withError(ResultStatus.SYSTEM_ERROR);
        }
        return resultListBean;
    }

}
