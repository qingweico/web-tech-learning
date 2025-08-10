package cn.qingweico.anno;

import java.lang.annotation.*;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AutoFill {

    FieldType value();

}
