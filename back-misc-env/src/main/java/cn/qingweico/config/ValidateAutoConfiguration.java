package cn.qingweico.config;

import cn.qingweico.aop.GlobalValidateAspect;
import cn.qingweico.validate.CustomValidFieldValidator;
import cn.qingweico.validate.ValidatorHelper;
import cn.qingweico.validator.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zqw
 * @date 2025/8/2
 */
@Configuration
public class ValidateAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public GlobalValidateAspect globalValidateAspect(ValidatorHelper validatorHelper) {
        return new GlobalValidateAspect(validatorHelper);
    }

    @Bean
    @ConditionalOnMissingBean
    public InFieldValidator inFieldValidator() {
        return new InFieldValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    public InEnumValidator inEnumValidator() {
        return new InEnumValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    public CustomValidFieldValidator customValidFieldValidator() {
        return new CustomValidFieldValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    public DateRangeFieldValidator dateRangeFieldValidator() {
        return new DateRangeFieldValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    public LengthFieldValidator lengthFieldValidator() {
        return new LengthFieldValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    public MaxFieldValidator maxFieldValidator() {
        return new MaxFieldValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    public MinFieldValidator minFieldValidator() {
        return new MinFieldValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    public NotNullFieldValidator notNullFieldValidator() {
        return new NotNullFieldValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    public RangeFieldValidator rangeFieldValidator() {
        return new RangeFieldValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    public RegExpressFieldValidator regExpressFieldValidator() {
        return new RegExpressFieldValidator();
    }

    @Bean
    @ConditionalOnMissingBean
    public ValidatorHelper validatorHelper() {
        return new ValidatorHelper();
    }
}
