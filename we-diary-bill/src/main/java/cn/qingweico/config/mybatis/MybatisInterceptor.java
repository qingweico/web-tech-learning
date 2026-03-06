package cn.qingweico.config.mybatis;

import cn.qingweico.common.system.vo.LoginUser;
import cn.qingweico.common.util.DateUtil;
import cn.qingweico.common.util.LoginUserUtils;
import cn.qingweico.common.util.oConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

/**
 * mybatis拦截器, 自动注入创建人、创建时间、修改人、修改时间
 *
 * @Author scott
 * @Date 2019-01-19
 */
@Slf4j
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MybatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String sqlId = mappedStatement.getId();
        log.debug("------sqlId------" + sqlId);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
        log.debug("------sqlCommandType------" + sqlCommandType);

        if (parameter == null) {
            return invocation.proceed();
        }
        if (SqlCommandType.INSERT == sqlCommandType) {
            Field[] fields = oConvertUtils.getAllFields(parameter);
            for (Field field : fields) {
                log.debug("------field.name------" + field.getName());
                try {
                    // 获取登录用户信息
                    if ("createdBy".equals(field.getName()) || "createdBy".equals(field.getName())) {
                        LoginUser sysUser = LoginUserUtils.getLoginUser();
                        field.setAccessible(true);
                        Object local_createBy = field.get(parameter);
                        field.setAccessible(false);
                        if (local_createBy == null || "".equals(local_createBy)) {
                            String createdBy = "jeecg";
                            if (sysUser != null) {
                                // 登录账号
                                createdBy = sysUser.getUsername();
                            }
                            if (oConvertUtils.isNotEmpty(createdBy)) {
                                field.setAccessible(true);
                                field.set(parameter, createdBy);
                                field.setAccessible(false);
                            }
                        }
                    }
                    // 注入创建时间
                    if ("created".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_createDate = field.get(parameter);
                        field.setAccessible(false);
                        if (local_createDate == null || "".equals(local_createDate)) {
                            field.setAccessible(true);
                            log.info(field.getGenericType().getTypeName());
                            if (field.getType().isAssignableFrom(String.class)) {
                                field.set(parameter, DateUtil.getTime());
                            } else {
                                field.set(parameter, new Date());
                            }
                            field.setAccessible(false);
                        }
                    }
                    //注入部门编码
                    if ("sysOrgCode".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_sysOrgCode = field.get(parameter);
                        field.setAccessible(false);
                        if (local_sysOrgCode == null || "".equals(local_sysOrgCode)) {
                            LoginUser sysUser = LoginUserUtils.getLoginUser();
                            String sysOrgCode = "";
                            // 获取登录用户信息
                            if (sysUser != null) {
                                // 登录账号
                                sysOrgCode = sysUser.getOrgCode();
                            }
                            if (oConvertUtils.isNotEmpty(sysOrgCode)) {
                                field.setAccessible(true);
                                field.set(parameter, sysOrgCode);
                                field.setAccessible(false);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        if (SqlCommandType.UPDATE == sqlCommandType) {
            Field[] fields = null;
            if (parameter instanceof ParamMap) {
                ParamMap<?> p = (ParamMap<?>) parameter;
                if (p.containsKey("et")) {
                    parameter = p.get("et");
                    if (parameter == null) {
                        parameter = p.get("ew");
                    }
                } else {
                    parameter = p.get("param1");
                }
                fields = oConvertUtils.getAllFields(parameter);
            } else {
                fields = oConvertUtils.getAllFields(parameter);
            }

            for (Field field : fields) {
                log.debug("------field.name------" + field.getName());
                try {
                    if ("lastUpdBy".equals(field.getName()) || "lastUpdBy".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_updateBy = field.get(parameter);
                        field.setAccessible(false);
                        if (local_updateBy == null || "".equals(local_updateBy)) {
                            String lastUpdBy = "jeecg";
                            // 获取登录用户信息
                            LoginUser sysUser = LoginUserUtils.getLoginUser();
                            if (sysUser != null) {
                                // 登录账号
                                lastUpdBy = sysUser.getUsername();
                            }
                            if (oConvertUtils.isNotEmpty(lastUpdBy)) {
                                field.setAccessible(true);
                                field.set(parameter, lastUpdBy);
                                field.setAccessible(false);
                            }
                        }
                    }
                    if ("lastUpd".equals(field.getName()) || "lastUpd".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_updateDate = field.get(parameter);
                        field.setAccessible(false);
//						if (local_updateDate == null || local_updateDate.equals("")) {
                        field.setAccessible(true);
                        field.set(parameter, new Date());
                        field.setAccessible(false);
//						}
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // TODO Auto-generated method stub
    }

}
