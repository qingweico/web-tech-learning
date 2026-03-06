package cn.qingweico.common.util;

import cn.qingweico.common.system.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;

/**
 * @version 1.0
 * @author:  jdd尚岳
 * @date:  2021-02-18 18:16
 */
@Slf4j
public class LoginUserUtils {

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    public static LoginUser getLoginUser() {
        LoginUser principal = null;
        try {
              principal = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        }catch (Exception e){

            return null;
        }
        return principal;
    }


}
