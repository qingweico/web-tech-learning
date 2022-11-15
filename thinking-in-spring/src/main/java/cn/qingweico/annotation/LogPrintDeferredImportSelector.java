package cn.qingweico.annotation;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author zqw
 * @date 2022/9/24
 */
public class LogPrintDeferredImportSelector implements DeferredImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{LogPrintConfig.class.getName()};
    }
}
