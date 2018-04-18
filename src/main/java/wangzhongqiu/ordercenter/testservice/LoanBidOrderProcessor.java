package wangzhongqiu.ordercenter.testservice;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import wangzhongqiu.ordercenter.util.result.OrderResultBean;

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
        }
    }

    private static Log log = LogFactory.getLog(LoanBidOrderProcessor.class);

    public OrderResultBean prepareEscrowRedirection(String stateInput) {
        OrderResultBean result = OrderResultBean.build();
        JSONObject stateInputJSON = JSONObject.parseObject(stateInput);
        result.setCurrStateOutput(OUTPUT.SUCCESS.name());
        result.setCurrStateResult(OUTPUT.SUCCESS.name());
        stateInputJSON.put("escrowTransOutputType", OUTPUT.SUCCESS.name());
        result.setNextStateInput(stateInputJSON.toJSONString());
        log.info("prepareEscrowRedirection执行完毕");
        return result;
    }

    public OrderResultBean getUserEscrowConfirmed(String stateInput) {
        OrderResultBean result = OrderResultBean.build();
        JSONObject stateInputJSON = null;
        stateInputJSON = JSONObject.parseObject(stateInput);
        result.setCurrStateOutput(OUTPUT.SUCCESS.name());
        result.setCurrStateResult(OUTPUT.SUCCESS.name());
        result.setNextStateInput(stateInputJSON.toJSONString());
        log.info("getUserEscrowConfirmed执行完毕");
        return result;
    }

    public OrderResultBean cancelLoanBid(String input) {
        OrderResultBean result = OrderResultBean.build();
        JSONObject stateInputJSON = JSONObject.parseObject(input);
        result.setCurrStateOutput(OUTPUT.SUCCESS.name());
        result.setCurrStateResult(OUTPUT.FAILED.name());
        log.info("cancelLoanBid执行完毕");
        return result;
    }
}
