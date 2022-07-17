package cn.qingweico.generic;

import org.springframework.core.ResolvableType;

/**
 * @author zqw
 * @date 2022/1/24
 */
public class ResolvableTypeExample {
    public static void main(String[] args) {
        ResolvableType t = ResolvableType.forClass(StringList.class);

        System.out.println(t.getSuperType());
        System.out.println(t.getSuperType().getSuperType());

        // 获取 Raw Type
        System.out.println(t.asCollection().resolve());

        // 获取泛型参数类型
        System.out.println(t.asCollection().resolveGeneric(0));
    }
}
