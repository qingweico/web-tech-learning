package cn.qingweico.modules.shiro.authc;

import cn.qingweico.common.constant.CommonConstant;
import cn.qingweico.common.exception.*;
import cn.qingweico.common.system.util.JwtUtil;
import cn.qingweico.common.system.vo.LoginUser;
import cn.qingweico.common.util.CacheUtil;
import cn.qingweico.common.util.oConvertUtils;
import cn.qingweico.modules.shiro.vo.DefConstants;
import cn.qingweico.modules.system.entity.SysUser;
import cn.qingweico.modules.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author zqw
 */
@Component
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Resource
    @Lazy
    private ISysUserService sysUserService;


    @Resource
    private CacheUtil cacheUtil;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("————权限认证 [ roles、permissions]————");
        LoginUser sysUser;
        String username = null;
        if (principals != null) {
            sysUser = (LoginUser) principals.getPrimaryPrincipal();
            username = sysUser.getUsername();
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        Set<String> roleSet = sysUserService.getUserRolesSet(username);
        info.setRoles(roleSet);

        Set<String> permissionSet = sysUserService.getUserPermissionsSet(username);
        info.addStringPermissions(permissionSet);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws RuntimeException {
        log.debug("————身份认证————");
        String token = (String) auth.getCredentials();
        if (null == token) {
            throw new TokenNullException("token为空!");
        }
        // 校验token有效性
        LoginUser loginUser = this.checkUserTokenIsEffect(token);
        return new SimpleAuthenticationInfo(loginUser, token, getName());
    }

    /**
     * 校验token的有效性
     *
     * @param token token
     */
    public synchronized LoginUser checkUserTokenIsEffect(String token) throws AuthenticationException {

        String username = JwtUtil.getUsername(token);
        String userId = JwtUtil.getUserId(token);
        if (username == null) {
            throw new TokenInvalidException("token非法无效!");
        }

        LoginUser loginUser = new LoginUser();
        SysUser sysUser = sysUserService.getUserByName(username);
        if (sysUser == null) {
            throw new UserNotExistException("用户不存在!");
        }
        String cacheToken = cacheUtil.get(CommonConstant.TOKEN_CACHE_NAME, CommonConstant.PREFIX_USER_TOKEN + userId);

        if (oConvertUtils.isNotEmpty(cacheToken)) {
            if (!cacheToken.equals(token)) {
                log.info("---------token不一致");
                throw new UserDuplicateLoginException("你已在其他终端登录,请重新登录!");
            }
        }
        // 校验token是否超时失效 & 或者账号密码是否错误
        if (!jwtTokenRefresh(token, username, userId, sysUser.getPassword())) {
            throw new TokenExpiredException("Token失效,请重新登录!");
        }
        // 判断用户状态
        if (sysUser.getStatus() != 1) {
            throw new UserAccountLockException("账号已被锁定,请联系管理员!");
        }
        BeanUtils.copyProperties(sysUser, loginUser);
        return loginUser;
    }

    /**
     * JWTToken刷新生命周期 (解决用户一直在线操作, 提供Token失效问题)
     * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)
     * 2、当该用户再次请求时, 通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
     * 3、当该用户这次请求JWTToken值还在生命周期内, 则会通过重新PUT的方式k、v都为Token值, 缓存中的token值生命周期时间重新计算(这时候k、v值一样)
     * 4、当该用户这次请求jwt生成的token值已经超时, 但该token对应cache中的k还是存在, 则表示该用户一直在操作只是JWT的token失效了, 程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值, 该缓存生命周期重新计算
     * 5、当该用户这次请求jwt在生成的token值已经超时, 并在cache中不存在对应的k, 则表示该用户账户空闲超时, 返回用户信息已失效, 请重新登录。
     * 6、每次当返回为true情况下, 都会给Response的Header中设置Authorization, 该Authorization映射的v为cache对应的v值。
     * 7、注: 当前端接收到Response的Header中的Authorization值会存储起来, 作为以后请求token使用
     * 参考方案: https://blog.csdn.net/qq394829044/article/details/82763936
     *
     * @param userName
     * @param passWord
     * @return
     */
    public boolean jwtTokenRefresh(String token, String userName, String userId, String passWord) {
        String cacheToken = cacheUtil.get(CommonConstant.TOKEN_CACHE_NAME, CommonConstant.PREFIX_USER_TOKEN + userId);
        if (oConvertUtils.isNotEmpty(cacheToken)) {

            // 校验token有效性
            String terminalType = JwtUtil.getTerminalType(token);
            if (!JwtUtil.verify(token, userName, passWord)) {
                String newAuthorization = "";
                if ((StringUtils.isNotBlank(terminalType)) && (JwtUtil.H5.equals(terminalType))) {
                    newAuthorization = JwtUtil.sign(userName, userId, passWord, JwtUtil.H5);
                    cacheUtil.put(CommonConstant.TOKEN_CACHE_NAME, CommonConstant.PREFIX_USER_TOKEN + userId, newAuthorization);
                } else {
                    String parkCode = "";
                    String parkName = "";
                    newAuthorization = JwtUtil.sign(userName, userId, parkCode, parkName, passWord);
                    cacheUtil.put(CommonConstant.TOKEN_CACHE_NAME, CommonConstant.PREFIX_USER_TOKEN + userId, newAuthorization);
                }
                log.info("---------刷新token");
                HttpServletResponse httpServletResponse = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
                httpServletResponse.setHeader(DefConstants.X_ACCESS_TOKEN, newAuthorization);
                httpServletResponse.setHeader("Access-Control-Expose-Headers", DefConstants.X_ACCESS_TOKEN);
            } else {
                if ((StringUtils.isNotBlank(terminalType)) && (JwtUtil.H5.equals(terminalType))) {
                    cacheUtil.put(CommonConstant.TOKEN_CACHE_NAME, CommonConstant.PREFIX_USER_TOKEN + userId, cacheToken);
                } else {
                    cacheUtil.put(CommonConstant.TOKEN_CACHE_NAME, CommonConstant.PREFIX_USER_TOKEN + userId, cacheToken);
                }
            }
            return true;
        }
        return false;
    }
}
