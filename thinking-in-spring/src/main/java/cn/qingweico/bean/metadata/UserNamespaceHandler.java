package cn.qingweico.bean.metadata;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author zqw
 * @date 2021/12/21
 */
public class UserNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("user", new UserBeanDefinitionParser());
    }
}
