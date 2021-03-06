package wangzhongqiu.ordercenter.util.utils;

import java.math.BigInteger;
import java.util.Date;

/**
 * 
 */
public class ConvertUtil {
    private static final BigInteger LONG_MAX = new BigInteger(String.valueOf(Long.MAX_VALUE));
    private static final BigInteger LONG_MIN = new BigInteger(String.valueOf(Long.MIN_VALUE));
    private static final BigInteger INTEGER_MAX = new BigInteger(String.valueOf(Integer.MAX_VALUE));
    private static final BigInteger INTEGER_MIN = new BigInteger(String.valueOf(Integer.MIN_VALUE));

    /**
     * 转换对象为整型，若格式错误或超出Int的最大和最小值范围，则抛异常
     *
     * @param o
     * @return 整型值
     * @throws IllegalArgumentException
     */
    public static int toInt(Object o) {
        try {
            if (o instanceof Integer) {
                return (Integer) o;
            } else {
                return Integer.parseInt(o.toString());
            }
/*
            if (o instanceof Integer) {
                return (Integer) o;
            } else if (o instanceof BigInteger) {
                if (isInt((BigInteger) o)) {
                    return ((BigInteger) o).intValue();
                }
            } else {
                return Integer.parseInt(o.toString());
            }
            throw new IllegalArgumentException("Convert Int Error: " + o);
*/
        } catch (Exception e) {
            throw new IllegalArgumentException("Convert Int Error: " + o, e);
        }
    }

    /**
     * 转换对象为长整型，若格式错误或超出Long的最大和最小值范围，则抛异常
     *
     * @param o
     * @return 长整型值
     * @throws IllegalArgumentException
     */
    public static long toLong(Object o) {
        try {
            if (o instanceof Long) {
                return (Long) o;
            } else {
                return Long.parseLong(o.toString());
            }
/*
            if (o instanceof Long) {
                return (Long) o;
            } else if (o instanceof Integer) {
                return ((Integer) o).longValue();
            } else if (o instanceof BigInteger) {
                if (isLong((BigInteger) o)) {
                    return ((BigInteger) o).longValue();
                }
            } else {
                return Long.parseLong(o.toString());
            }
            throw new IllegalArgumentException("Convert Long Error: " + o);
*/
        } catch (Exception e) {
            throw new IllegalArgumentException("Convert Long Error: " + o, e);
        }
    }

    /**
     * 若是bool/Boolean类型，则转换，否则抛异常
     *
     * @param o
     * @return bool值
     * @throws IllegalArgumentException
     */
    public static boolean toBool(Object o) {
        try {

            if (o instanceof Boolean) {
                return (Boolean) o;
            } else if ("true".equalsIgnoreCase(o.toString())) {
                return true;
            } else if ("false".equalsIgnoreCase(o.toString())) {
                return false;
            } else {
                throw new IllegalArgumentException("Convert Boolean Error: " + o);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Convert Boolean Error: " + o, e);
        }
    }

    // TODO: 检测BigDecimal的情形
    public static float toFloat(Object o) {
        try {
            if (o instanceof Float) {
                return (Float) o;
            } else {
                return Float.parseFloat(o.toString());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Convert Long Error: " + o, e);
        }
    }

    // TODO: 检测BigDecimal的情形
    public static double toDouble(Object o) {
        try {
            if (o instanceof Double) {
                return (Double) o;
            } else {
                return Double.parseDouble(o.toString());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Convert Long Error: " + o, e);
        }
    }

    /**
     * 检测Date类型，或转换toString()结果为Date型
     * <br/>支持格式yyyyMMdd、yyyyMMddHHmmss两种
     *
     * @param o 待转换对象
     * @return Date型日期
     * @throws IllegalArgumentException
     */
    public static Date toDate(Object o) {
        try {
            if (o instanceof Date) {
                return (Date) o;
            } else {
                return DateUtil.parse(o.toString());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Convert Long Error: " + o, e);
        }
    }

    private static boolean isLong(BigInteger bi) {
        return bi.max(LONG_MAX).equals(LONG_MAX) && bi.min(LONG_MIN).equals(LONG_MIN);
    }

    private static boolean isLongNonNegative(BigInteger bi) {
        return isLong(bi) && bi.longValue() >= 0;
    }

    private static boolean isInt(BigInteger bi) {
        return bi.max(INTEGER_MAX).equals(INTEGER_MAX) && bi.min(INTEGER_MIN).equals(INTEGER_MIN);
    }

    private static boolean isIntNonNegative(BigInteger bi) {
        return isInt(bi) && bi.intValue() >= 0;
    }
}
