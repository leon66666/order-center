package wangzhongqiu.ordercenter.service.impl;

import wangzhongqiu.ordercenter.bo.OrderLogBo;
import wangzhongqiu.ordercenter.entity.OrderLog;
import wangzhongqiu.ordercenter.logic.OrderLogLogic;
import wangzhongqiu.ordercenter.service.OrderLogService;
import wangzhongqiu.ordercenter.util.result.ResultBean;
import wangzhongqiu.ordercenter.util.result.ResultStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 日志服务实现类
 */

@Service
public class OrderLogServiceImpl implements OrderLogService {

    private static Logger logger = Logger.getLogger(OrderLogServiceImpl.class);

    @Autowired
    private OrderLogLogic logic;

    public ResultBean<OrderLog> getOrderLog(OrderLogBo orderLogBo) {
        ResultBean<OrderLog> resultBean = ResultBean.build();
        try {
            OrderLog log = new OrderLog();
            BeanUtils.copyProperties(orderLogBo, log);
            OrderLog orderLog = logic.selectOrderLog(log);
            if (null != orderLog)
                resultBean.success(orderLog);
        } catch (Exception e) {
            logger.error("getOrderLog:" + orderLogBo.getOrderNo(), e);
            resultBean.withError(ResultStatus.SYSTEM_ERROR);
        }
        return resultBean;
    }

}
