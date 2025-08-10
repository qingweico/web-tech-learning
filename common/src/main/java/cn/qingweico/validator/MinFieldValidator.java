package cn.qingweico.validator;

import cn.qingweico.validate.anno.Min;

import java.math.BigDecimal;

/**
 * @author zqw
 * @date 2025/7/26
 */
public class MinFieldValidator extends AbstractNumberRangeValidator<Min> {
    @Override
    protected int compare(BigDecimal fieldValue, BigDecimal limitValue) {
        return fieldValue.compareTo(limitValue);
    }

    @Override
    protected String getDefaultErrorMessage() {
        return "字段[%s]的值不能小于%s";
    }
}
