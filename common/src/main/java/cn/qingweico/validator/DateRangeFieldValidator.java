package cn.qingweico.validator;

import cn.qingweico.validate.IFieldValidator;
import cn.qingweico.validate.ValidatorHelper;
import cn.qingweico.validate.anno.DateRange;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zqw
 * @date 2025/7/26
 */
public class DateRangeFieldValidator implements IFieldValidator<DateRange, Object> {
    @Override
    public void  validate(Field field, Object value, Object entity, DateRange anno) {
        if (field.getType().equals(String.class) || field.getType().equals(Date.class)) {
            SimpleDateFormat format = new SimpleDateFormat(anno.format());

            Date date;
            Date begin;
            Date end;
            if (value instanceof Date) {
                date = (Date) value;
            } else {
                try {
                    date = format.parse(value.toString());
                } catch (ParseException e) {
                    String name = ValidatorHelper.getFieldName(field);
                    throw new RuntimeException(name + "不是有效的日期格式");
                }
            }

            try {
                begin = format.parse(anno.min());
                end = format.parse(anno.max());
            } catch (ParseException e) {
                throw new RuntimeException("在字段" + ValidatorHelper.getFieldName(field) + "上进行数据验证失败!DateRange验证只能用在日期类型或日期型字符串");
            }

            if (date.before(begin) || date.after(end)) {
                String msg = anno.error();
                String name = ValidatorHelper.getFieldName(field);
                if (StringUtils.isNotEmpty(msg)) {
                    throw new RuntimeException(msg.replace("{0}", name).replace("{1}", anno.min()).replace("{2}", anno.max()));
                } else {
                    throw new RuntimeException(ValidatorHelper.getFieldName(field) + "的格式不符合要求");
                }
            }
        } else {
            throw new RuntimeException("在字段" + ValidatorHelper.getFieldName(field) + "上进行数据验证失败!DateRange验证只能用在日期类型或日期型字符串");
        }
    }
}
