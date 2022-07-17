package cn.qingweico.bean;

import cn.qingweico.ioc.domain.User;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyEditorSupport;
import java.util.stream.Stream;

/**
 * @author zqw
 * @date 2021/9/26
 */
public class BeanInfoExample {
    public static void main(String[] args) throws IntrospectionException {
        // #1 实现类 而非 抽象类或者接口
        // #2 stopClass 排除某个类 Object#getClass()
        BeanInfo beanInfo = Introspector.getBeanInfo(User.class, Object.class);
        Stream.of(beanInfo.getPropertyDescriptors())
                .forEach((propertyDescriptor) -> {
                    Class<?> propertyType = propertyDescriptor.getPropertyType();
                    System.out.println(propertyType);
                    String propertyName = propertyDescriptor.getName();
                    System.out.println(propertyName);
                    if ("id".equals(propertyName)) {
                        propertyDescriptor.setPropertyEditorClass(StringToLongPropertyEditor.class);
                    }
                });
        // 输出 User 中所有的 MethodDescriptor
        Stream.of(beanInfo.getMethodDescriptors()).forEach(System.out::println);

    }

    static class StringToLongPropertyEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            Long value = Long.valueOf(text);
            setValue(value);
        }
    }
}
