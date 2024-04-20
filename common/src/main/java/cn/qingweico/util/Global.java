package cn.qingweico.util;

/**
 * @author zqw
 * @date 2024/1/28
 */
public class Global {
    public static int[] splitInteger(int value, int batch) {
        int quotient = value / batch;
        int remainder = value % batch;

        int[] result = new int[batch];
        for (int i = 0; i < batch; i++) {
            if (remainder > 0) {
                result[i] = quotient + 1;
                remainder--;
            } else {
                result[i] = quotient;
            }
        }
        return result;
    }
}
