package cn.qingweico.common.system.util;

import cn.qingweico.model.BusinessException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Joiner;
import cn.qingweico.common.constant.DataBaseConstant;
import cn.qingweico.common.system.vo.LoginUser;
import cn.qingweico.common.system.vo.SysUserCacheInfo;
import cn.qingweico.common.util.SpringContextUtils;
import cn.qingweico.common.util.oConvertUtils;
import org.apache.shiro.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author zhouqingwei
 */
public class JwtUtil {

    /**
     * 过期时间7天
     */
    public static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;
    /**
     * 手持端首次登录默认1天有效
     */
    public static final long MOBILE_LONGING_EXPIRE_TIME = 24 * 60 * 60 * 1000;
    /**
     * 手持端刷新默认7天
     */
    public static final long MOBILE_FRESHEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;

    public static final String H5 = "H5";


    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            // 效验TOKEN
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }


    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户id
     */
    public static String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getTerminalType(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("terminalType").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,30min后过期
     *
     * @param username 用户名
     * @param userId   用户id
     * @param parkCode 车场code
     * @param parkName 车场名称
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String userId, String parkCode, String parkName, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withClaim("userId", userId)
                .withExpiresAt(date)
                .sign(algorithm);

    }

    /**
     * 生成签名,30min后过期
     *
     * @param username 用户名
     * @param userId   用户id
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String userId, String secret, String terminalType) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withClaim("userId", userId)
                .withClaim("terminalType", terminalType)
                .withExpiresAt(date)
                .sign(algorithm);

    }


    public static TokenUser parserToken(String token) {
        Map<String, Claim> claims = getClaims(token);
        TokenUser tokenUser = new TokenUser();
        if (claims.containsKey("clientId")) {
            tokenUser.setClientId(claims.get("clientId").asString());
        }
        if (claims.containsKey("mobile")) {
            tokenUser.setMobile(claims.get("mobile").asString());
        }
        if (claims.containsKey("neName")) {
            tokenUser.setNeName(claims.get("neName").asString());
        }
        if (claims.containsKey("userId")) {
            tokenUser.setUserId(claims.get("userId").asString());
        }
        if (claims.containsKey("username")) {
            tokenUser.setUserName(claims.get("username").asString());
        }
        return tokenUser;
    }


    public static Map<String, Claim> getClaims(String token) {
        return JWT.decode(token).getClaims();

    }

    /**
     * 根据request中的token获取用户账号
     *
     * @param request HttpServletRequest
     * @return username
     * @throws BusinessException BusinessException
     */
    public static String getUserNameByToken(HttpServletRequest request) throws BusinessException {
        String accessToken = request.getHeader("X-Access-Token");
        String username = getUsername(accessToken);
        if (oConvertUtils.isEmpty(username)) {
            throw new BusinessException("未获取到用户");
        }
        return username;
    }

    /**
     * 从session中获取变量
     *
     * @param key
     * @return
     */
    public static String getSessionData(String key) {
        //${myVar}%
        //得到${} 后面的值
        String moshi = "";
        if (key.contains("}")) {
            moshi = key.substring(key.indexOf("}") + 1);
        }
        String returnValue = null;
        if (key.contains("#{")) {
            key = key.substring(2, key.indexOf("}"));
        }
        if (oConvertUtils.isNotEmpty(key)) {
            HttpSession session = SpringContextUtils.getHttpServletRequest().getSession();
            returnValue = (String) session.getAttribute(key);
        }
        //结果加上${} 后面的值
        if (returnValue != null) {
            returnValue = returnValue + moshi;
        }
        return returnValue;
    }

    /**
     * 从当前用户中获取变量
     *
     * @param key
     * @param user
     * @return
     */
    public static String getUserSystemData(String key, SysUserCacheInfo user) {
        if (user == null) {
            user = JeecgDataAutorUtils.loadUserInfo();
        }
        //#{sys_user_code}%

        // 获取登录用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        String moshi = "";
        if (key.contains("}")) {
            moshi = key.substring(key.indexOf("}") + 1);
        }
        String returnValue = null;
        //针对特殊标示处理#{sysOrgCode}, 判断替换
        if (key.contains("#{")) {
            key = key.substring(2, key.indexOf("}"));
        } else {
            key = key;
        }
        //替换为系统登录用户帐号
        if (key.equals(DataBaseConstant.SYS_USER_CODE) || key.equals(DataBaseConstant.SYS_USER_CODE_TABLE)) {
            if (user == null) {
                returnValue = sysUser.getUsername();
            } else {
                returnValue = user.getSysUserCode();
            }
        }
        //替换为系统登录用户真实名字
        if (key.equals(DataBaseConstant.SYS_USER_NAME) || key.equals(DataBaseConstant.SYS_USER_NAME_TABLE)) {
            if (user == null) {
                returnValue = sysUser.getRealname();
            } else {
                returnValue = user.getSysUserName();
            }
        }

        //替换为系统用户登录所使用的机构编码
        if (key.equals(DataBaseConstant.SYS_ORG_CODE) || key.equals(DataBaseConstant.SYS_ORG_CODE_TABLE)) {
            if (user == null) {
                returnValue = sysUser.getOrgCode();
            } else {
                returnValue = user.getSysOrgCode();
            }
        }
        //替换为系统用户所拥有的所有机构编码
        if (key.equals(DataBaseConstant.SYS_MULTI_ORG_CODE)) {
            if (user.isOneDepart()) {
                returnValue = user.getSysMultiOrgCode().get(0);
            } else {
                returnValue = Joiner.on(",").join(user.getSysMultiOrgCode());
            }
        }
        //替换为当前系统时间(年月日)
        if (key.equals(DataBaseConstant.SYS_DATE) || key.equals(DataBaseConstant.SYS_DATE_TABLE)) {
            returnValue = user.getSysDate();
        }
        //替换为当前系统时间(年月日时分秒)
        if (key.equals(DataBaseConstant.SYS_TIME) || key.equals(DataBaseConstant.SYS_TIME_TABLE)) {
            returnValue = user.getSysTime();
        }
        if (returnValue != null) {
            returnValue = returnValue + moshi;
        }
        return returnValue;
    }
}
