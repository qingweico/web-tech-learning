package cn.qingweico.modules.shiro.util;

import cn.qingweico.common.system.util.JwtUtil;
import cn.qingweico.common.system.util.TokenUser;
import cn.qingweico.common.system.vo.LoginUser;

/**
 * @author zqw
 */
public class UserTokenManager {

    public static LoginUser parseToken(String token) {
        TokenUser tokenUser = JwtUtil.parserToken(token);
        return LoginUser.builder()
                .id(tokenUser.getUserId())
                .username(tokenUser.getUserName())
                .build();
    }
}
