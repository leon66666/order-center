package wangzhongqiu.ordercenter.test;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wangzhongqiu.ordercenter.bo.OrderBo;
import wangzhongqiu.ordercenter.service.OrderCenterService;

/**
 * @author wangzhongqiu
 * @date 2017/11/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-ordercenter.xml"})
public class ServiceTest {
    @Autowired
    private OrderCenterService orderCenterService;

    @Test
    public void test() throws Exception {
        String orderNo = "123";
        JSONObject stateInputJSON = new JSONObject();
        stateInputJSON.put("orderNo", "111");
        stateInputJSON.put("detailNo", "111");
        stateInputJSON.put("userId", "111");
        stateInputJSON.put("loanId", "111");
        stateInputJSON.put("amount", "111");
        stateInputJSON.put("couponId", "111");
        stateInputJSON.put("isAuto", "111");
        stateInputJSON.put("subPointId", "111");
        orderCenterService.newOrder(new OrderBo(orderNo, "LOAN_BID", "INIT", stateInputJSON.toJSONString()));
        orderCenterService.processOrder(orderNo);
    }

}
