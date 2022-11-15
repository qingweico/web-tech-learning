package cn.qingweico;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;

/**
 * @author zqw
 * @date 2022/8/14
 */
public class Starter implements QuarkusApplication {
    @Override
    public int run(String... args) throws Exception {
        return 0;
    }

    public static void main(String[] args) {
        Quarkus.run(Starter.class, args);
    }
}
