package cn.qingweico.bean.scope;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.core.NamedThreadLocal;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zqw
 * @date 2021/11/21
 */
public class ThreadLocalScope implements Scope {

    public static final String SCOPE_NAME = "threadLocalScope";
    private final NamedThreadLocal<Map<String, Object>> threadLocal = new NamedThreadLocal<Map<String, Object>>(SCOPE_NAME) {
        @Override
        public Map<String, Object> initialValue() {
            return new HashMap<>(0);
        }
    };

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Map<String, Object> context = getContext();
        Object o = context.get(name);
        if (o == null) {
            o = objectFactory.getObject();
            context.put(name, o);

        }
        return o;
    }
    @NotNull
    private Map<String, Object> getContext () {
        return threadLocal.get();
    }

    @Override
    public Object remove(String name) {
        Map<String, Object> context = getContext();
        return context.remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        System.out.println("registerDestructionCallback()");
    }

    @Override
    public Object resolveContextualObject(String key) {
        Map<String, Object> context = getContext();
        return context.get(key);
    }

    @Override
    public String getConversationId() {
        Thread thread = Thread.currentThread();
        return String.valueOf(thread.getId());
    }

}
