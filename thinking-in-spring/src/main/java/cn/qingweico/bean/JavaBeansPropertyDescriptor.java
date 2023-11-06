package cn.qingweico.bean;

import cn.qingweico.bean.constants.Constants;
import cn.qingweico.ioc.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyEditorSupport;
import java.util.stream.Stream;

/**
 * @see java.beans.PropertyDescriptor
 * @see BeanInfo
 * @author zqw
 * @date 2021/9/26
 */
@Slf4j
public class JavaBeansPropertyDescriptor {
    public static void main(String[] args) throws IntrospectionException {
        // #1 实现类 而非 抽象类或者接口
        // #2 stopClass 排除某个类 Object#getClass()
        BeanInfo beanInfo = Introspector.getBeanInfo(User.class, Object.class);
        Stream.of(beanInfo.getPropertyDescriptors())
                .forEach((propertyDescriptor) -> {
                    Class<?> propertyType = propertyDescriptor.getPropertyType();
                    String propertyName = propertyDescriptor.getName();
                    log.info("propertyType : {}, propertyName : {}", propertyType, propertyName);
                    if (Constants.ID.equals(propertyName)) {
                        propertyDescriptor.setPropertyEditorClass(StringToLongPropertyEditor.class);
                        propertyDescriptor.createPropertyEditor(null);
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
