package cn.qingweico.resource.util;

import java.util.Map;
import java.util.Set;

/**
 * @author zqw
 * @date 2022/6/19
 */
public class MapUtils {
    public static void toPrint(Map<?, ?> map) {
        if (map == null || map.size() == 0) {
            System.err.println("map is null or size == 0");
            return;
        }
        Set<? extends Map.Entry<?, ?>> entrySet = map.entrySet();
        for (Map.Entry<?, ?> entry : entrySet) {
            System.out.printf("[key: %s, value: %s]%n", entry.getKey(), entry.getValue());
        }
    }
}
