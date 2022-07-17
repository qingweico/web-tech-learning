package cn.qingweico.binding;

import cn.qingweico.ioc.domain.User;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zqw
 * @date 2021/12/28
 */
public class DataBinderExample {
    public static void main(String[] args) {
        User user = new User();
        Map<String, Object> source = new HashMap<>();
        source.put("id", 1);
        source.put("name", "li");

        // 忽略未知的属性 ignoreUnknownFields 默认为true
        source.put("country", "CHINA");
        // 支持嵌套属性 autoGrowNestedPaths 默认为true
        source.put("company.totalOfStaff", 2000);
        PropertyValues propertyValues = new MutablePropertyValues(source);

        DataBinder binder = new DataBinder(user);


        binder.setRequiredFields("id", "name", "city");

        binder.bind(propertyValues);
        System.out.println(user);
        BindingResult result = binder.getBindingResult();

        System.out.println(result);
    }
}
