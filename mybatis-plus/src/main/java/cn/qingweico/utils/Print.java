package cn.qingweico.utils;

import cn.qingweico.constant.Symbol;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author zqw
 * @date 2021/1/17
 */
public class Print {
    /**
     * To print collection
     *
     * @param c ? extends Collection<?>
     */
    public static void toPrint(Collection<?> c) {
        for (Object o : c) {
            System.out.println(o);
        }
    }
}
