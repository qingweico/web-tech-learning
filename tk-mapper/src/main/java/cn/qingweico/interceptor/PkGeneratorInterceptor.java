package cn.qingweico.interceptor;

import cn.hutool.core.util.ReflectUtil;
import cn.qingweico.anno.AutoFill;
import cn.qingweico.anno.FieldType;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Properties;

/**
 * @author zqw
 * @date 2025/7/26
 */
@Slf4j
@Intercepts({@org.apache.ibatis.plugin.Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class PkGeneratorInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        if (args.length >= 2 && args[1] != null) {
            MappedStatement st = (MappedStatement) args[0];
            if (st != null && st.getSqlCommandType() == SqlCommandType.UPDATE) {
                Object model = args[1];
                if (model instanceof Map) {
                    model = ((Map<?, ?>) model).get("record");
                }
                Field[] allFields = ReflectUtil.getFieldsDirectly(model.getClass(), false);
                for (Field f : allFields) {
                    Class<?> fType = f.getType();
                    // 自动填充更新日期
                    if (fType.equals(LocalDateTime.class)) {
                        AutoFill autoFill = f.getAnnotation(AutoFill.class);
                        if (autoFill != null && autoFill.value() == FieldType.UPDATE_TIME) {
                            ReflectionUtils.makeAccessible(f);
                            ReflectionUtils.setField(f, model, LocalDateTime.now());
                        }
                    }
                }
            } else if (st != null && st.getSqlCommandType() == SqlCommandType.INSERT) {
                // 拦截到Insert语句 获取插入参数
                Object model = args[1];
                if (!model.getClass().isArray() && !(model instanceof Map) && !(model instanceof Iterable)) {
                    Field[] allFields = ReflectUtil.getFieldsDirectly(model.getClass(), false);
                    for (Field f : allFields) {
                        Class<?> fType = f.getType();
                        // 自动填充创建日期
                        if (fType.equals(LocalDateTime.class)) {
                            AutoFill autoFill = f.getAnnotation(AutoFill.class);
                            if (autoFill != null && (autoFill.value() == FieldType.CREATE_TIME
                                    || autoFill.value() == FieldType.UPDATE_TIME)) {
                                ReflectionUtils.makeAccessible(f);
                                ReflectionUtils.setField(f, model, LocalDateTime.now());
                            }
                        }
                    }
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties p) {

    }


}
