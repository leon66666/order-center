package wangzhongqiu.ordercenter.util.utils;

import org.codehaus.jackson.map.ObjectMapper;

public class JacksonJsonUtil {
    static volatile ObjectMapper objectMapper = new ObjectMapper();

    private JacksonJsonUtil() {
    }

    public static ObjectMapper getMapper() {
        if (objectMapper == null) {
            synchronized (JacksonJsonUtil.class) {
                if (objectMapper == null) {
                    objectMapper = new ObjectMapper();
                }
            }
        }
        return objectMapper;
    }

}
