package cn.qingweico.utils;

import java.util.Collection;

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
