package cn.qingweico.annotation;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * @author zqw
 * @date 2022/9/24
 */
public class LogPrintDeferredImportSelector implements DeferredImportSelector {
    @Override
    @NonNull
    public String[] selectImports(@NonNull AnnotationMetadata importingClassMetadata) {
        return new String[]{LogPrintConfig.class.getName()};
    }
}
