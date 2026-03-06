package cn.qingweico.common.system.base.controller;

import cn.qingweico.common.system.vo.LoginUser;
import cn.qingweico.common.util.IPUtils;
import cn.qingweico.common.util.ReflectUtils;
import lombok.Getter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author zhouqingwei
 */
@Getter
public class SuperController<E> {

    @Resource
    private HttpServletRequest request;


    /**
     * 根据请求heard中的token获取用户ID
     *
     * @return 用户ID
     */
    public String getUserId() {
        return getLoginUser().getId();
    }

    public String getUserName() {
        return getLoginUser().getUsername();
    }

    public LoginUser getLoginUser() {
        return (LoginUser) SecurityUtils.getSubject().getPrincipal();
    }


    public Subject getCurrentSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * @return
     */
    public String getIpAndPort() {
        return IPUtils.getIpAddr(request);
    }

    public Class<E> getSuperClass(Class basicClass) {
        return (Class<E>) ReflectUtils.getClassGenricType(basicClass, 1);
    }
}
