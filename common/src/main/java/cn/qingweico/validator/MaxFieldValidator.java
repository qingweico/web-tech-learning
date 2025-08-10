package cn.qingweico.validator;

import cn.qingweico.validate.anno.Max;

import java.math.BigDecimal;

/**
 * @author zqw
 * @date 2025/7/26
 */
public class MaxFieldValidator extends AbstractNumberRangeValidator<Max> {
    @Override
    protected int compare(BigDecimal fieldValue, BigDecimal limitValue) {
        return limitValue.compareTo(fieldValue);
    }

    @Override
    protected String getDefaultErrorMessage() {
        return "字段[%s]的值不能大于%s";
    }
}
