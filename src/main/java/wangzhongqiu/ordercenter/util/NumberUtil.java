package wangzhongqiu.ordercenter.util;

import java.text.DecimalFormat;

/**
 * 
 */
public class NumberUtil {

    public static String getDecimalFormat(String str) {
        Integer initValue = 0;
        String outStr = "";
        if (str != null && !"".equals(str.trim())) {
            DecimalFormat fmt = new DecimalFormat("##,###,###,###,###.00");
            double d;
            try {
                d = Double.parseDouble(str);
                outStr = fmt.format(d);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return outStr;
    }

    public static String getPercent(String str) {
        Integer initValue = 0;
        String outStr = "";
        if (str != null && !"".equals(str.trim())) {
            DecimalFormat fmt = new DecimalFormat("#.#%");
            double d;
            try {
                d = Double.parseDouble(str);
                outStr = fmt.format(d);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return outStr;
    }


//        public static void  main(String args[]){
//            System.out.println(getPercent("0.0150"));
//        }

}
