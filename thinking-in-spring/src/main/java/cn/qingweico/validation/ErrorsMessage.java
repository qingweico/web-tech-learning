package cn.qingweico.validation;

import cn.qingweico.ioc.domain.User;
import org.springframework.context.MessageSource;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author zqw
 * @date 2021/12/28
 * @see Errors
 */
public class ErrorsMessage {
    public static void main(String[] args) {
        User user = new User();
        Errors errors = new BeanPropertyBindingResult(user, "user");
        errors.reject("user.properties", "check user properties!");
        errors.rejectValue("name", "the name must be not null!");


        List<ObjectError> objectErrors = errors.getAllErrors();
        MessageSource messageSource = createMessageSource();
        for (ObjectError objectError : objectErrors) {
            String message = messageSource.getMessage(Objects.requireNonNull(objectError.getCode()),
                    objectError.getArguments(), Locale.ENGLISH);
            System.out.println(message);
        }
    }

    static MessageSource createMessageSource() {
        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.addMessage("user.properties", Locale.ENGLISH, "check user properties!");
        messageSource.addMessage("the name must be not null!", Locale.ENGLISH, "the name of User must be not null!");
        return messageSource;
    }
}
