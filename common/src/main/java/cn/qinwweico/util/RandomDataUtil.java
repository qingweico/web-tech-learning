package cn.qinwweico.util;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Using faker to generate some random data, such as random number, address, name, etc.
 *
 * @author zqw
 * @date 2022/06/20
 */
public class RandomDataUtil {

    private static Faker faker = null;
    private static Faker localFaker = null;

    static {
        try {
            faker = new LazyInitializer<Faker>() {
                @Override
                protected Faker initialize() {
                    return new Faker();
                }
            }.get();
        } catch (ConcurrentException e) {
            //
        }
        try {
            localFaker = new LazyInitializer<Faker>() {
                @Override
                protected Faker initialize() {
                    return new Faker(Locale.CHINA);
                }
            }.get();
        } catch (ConcurrentException e) {
            //
        }
    }

    public static String name(boolean isChinese) {
        String ret;
        if (isChinese) {
            ret = localFaker.name().fullName();
        } else {
            ret = faker.name().fullName();
        }
        return ret;
    }

    public static Date birthday(boolean isChinese) {
        Date ret;
        if (isChinese) {
            ret = localFaker.date().birthday();
        } else {
            ret = faker.date().birthday();
        }
        return ret;
    }

    public static Date birthday() {
        return birthday(false);
    }

    public static String phone(boolean isChinese) {
        String ret;
        if (isChinese) {
            ret = localFaker.phoneNumber().phoneNumber();
        } else {
            ret = faker.phoneNumber().phoneNumber();
        }
        return ret;
    }

    public static String phone() {
        return phone(false);
    }

    public static String address(boolean isChinese) {
        String ret;
        if (isChinese) {
            ret = localFaker.address().fullAddress();
        } else {
            ret = faker.address().fullAddress();
        }
        return ret;
    }

    public static String address() {
        return address(false);
    }

    public static String name() {
        return name(false);
    }

    public static String zeroOrOne() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(2));
    }
    public static int nextInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }
}
