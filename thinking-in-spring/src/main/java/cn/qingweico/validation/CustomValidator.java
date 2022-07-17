package cn.qingweico.validation;

import cn.qingweico.ioc.domain.User;
import org.springframework.context.MessageSource;
import org.springframework.validation.*;

import java.util.Locale;
import java.util.Objects;

/**
 * @author zqw
 * @date 2021/12/28
 */
public class CustomValidator {
    public static void main(String[] args) {
        User user = new User();
        user.setName("li");
        Validator validator = new UserValidator();
        Errors errors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, errors);
        System.out.println(validator.supports(user.getClass()));
        MessageSource messageSource = ErrorsMessage.createMessageSource();
        for (ObjectError objectError : errors.getAllErrors()) {
            String message = messageSource.getMessage(Objects.requireNonNull(objectError.getCode()),
                    objectError.getArguments(), Locale.ENGLISH);
            System.out.println(message);
        }
    }

    static class UserValidator implements Validator {
        @Override
        public boolean supports(Class<?> clazz) {
            return User.class.isAssignableFrom(clazz);
        }

        @Override
        public void validate(Object target, Errors errors) {
            User user = (User) target;
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "the name must be not null!");
            String name = user.getName();
            System.out.println("the user name: " + name);
        }
    }
}
