package cn.qingweico.annotation;

import org.springframework.beans.factory.FactoryBean;

import javax.annotation.Resource;

/**
 * 使用 FactoryBean 接口 将 Bean 装入 Spring 容器中
 *
 * @author zqw
 * @date 2022/9/24
 */
public class LogPrintFactoryBean implements FactoryBean<String> {

    @Resource
    private LogPrintConfig logPrintConfig;

    @Override
    public String getObject() throws Exception {
        return logPrintConfig.log();
    }

    @Override
    public Class<?> getObjectType() {
        return String.class;
    }
}
