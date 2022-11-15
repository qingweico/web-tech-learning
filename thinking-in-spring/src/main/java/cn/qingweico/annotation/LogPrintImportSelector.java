package cn.qingweico.annotation;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author zqw
 * @date 2022/9/23
 */
public class LogPrintImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // 选择一个或者多个要导入了类
        return new String[]{LogPrintConfig.class.getName()};
    }
}
