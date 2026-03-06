package cn.qingweico.modules.system.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Producer;
import cn.qingweico.common.api.vo.Result;
import cn.qingweico.common.aspect.annotation.LogLevelType;
import cn.qingweico.common.aspect.annotation.OperationLogDetail;
import cn.qingweico.common.aspect.annotation.OperationType;
import cn.qingweico.common.aspect.annotation.OperationUnit;
import cn.qingweico.common.constant.CommonConstant;
import cn.qingweico.common.system.api.ISysBaseAPI;
import cn.qingweico.common.system.util.JwtUtil;
import cn.qingweico.common.system.vo.LoginUser;
import cn.qingweico.common.util.CacheUtil;
import cn.qingweico.common.util.LoginUserUtils;
import cn.qingweico.common.util.PasswordUtil;
import cn.qingweico.common.util.encryption.AesEncryptUtil;
import cn.qingweico.common.util.encryption.EncryptedString;
import cn.qingweico.common.util.encryption.RASUtil;
import cn.qingweico.common.util.oConvertUtils;

import cn.qingweico.modules.system.entity.SysUser;
import cn.qingweico.modules.system.model.SysLoginModel;
import cn.qingweico.modules.system.service.ISysLogService;
import cn.qingweico.modules.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
import java.util.*;

/**
 * 用户登录控制器
 *
 * @author
 */
@RestController
@RequestMapping("/sys")
@Api(tags = "用户登录")
@Slf4j
public class LoginController {
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysBaseAPI sysBaseAPI;
    @Resource
    private ISysLogService logService;
    @Resource
    private Producer producer;
    @Resource
    private CacheUtil cacheUtil;


    public static int RESULT500 = 500;

    @RequestMapping(value = "/getUserId", method = RequestMethod.GET)
    @ApiOperation("获取登陆人id")
    public Result<String> getUserId() {
        Result<String> result = new Result<String>();
        String id = null;
        try {
            id = LoginUserUtils.getLoginUser().getId();
            if (ObjectUtil.isNotEmpty(id)) {
                result.setResult(id);
            }
        } catch (Exception e) {
            result.error500("获取失败");
        }

        return result;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation("登录接口")
    @OperationLogDetail(detail = "登录", level = LogLevelType.ONE, operationType = OperationType.LOGIN, operationUnit = OperationUnit.USER)
    public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel) throws Exception {
        Result<JSONObject> result;
        String username = sysLoginModel.getUsername();
        String oldPassword = sysLoginModel.getPassword().trim();
        String password = AesEncryptUtil.desEncrypt(URLDecoder.decode(oldPassword, CommonConstant.DEFAULT_CHARSET).replace(' ', '+'));
        if (StringUtils.isEmpty(password)) {
            password = oldPassword;
        }
        SysUser sysUser = sysUserService.getUserByName(username);
        result = sysUserService.checkUserIsEffective(sysUser);
        if (RESULT500 == result.getCode()) {
            return result;
        }
        // 校验用户是否有效
        if (!result.isSuccess()) {
            return result;
        }
        // 校验数据的合法性
        if (StringUtils.isEmpty(sysUser.getSignature())) {
            result.error500("该用户非法");
            return result;
        }
//        try {
//            if (!RASUtil.verifySign(sysUser.getSignature(), sysUser.getUsername())) {
//                result.error500("该用户非法");
//                return result;
//            }
//        } catch (Exception e) {
//            log.error("用户非法登录---> {}", e.getMessage(), e);
//            result.error500("该用户非法");
//            return result;
//        }
        //3. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            result.error500("用户名或密码错误");
            return result;
        }
        // 用户登录信息
        userInfo(sysUser, result);
        return result;
    }


    /**
     * 退出登录
     *
     * @return
     */
    @RequestMapping(value = "/logout")
    public Result<Object> logout() {
        //用户退出逻辑
        Subject subject = SecurityUtils.getSubject();
        LoginUser sysUser = (LoginUser) subject.getPrincipal();
        log.info(" 用户名:  {},退出成功！ ", sysUser.getRealname());
        subject.logout();
        cacheUtil.delete(CommonConstant.TOKEN_CACHE_NAME, CommonConstant.PREFIX_USER_TOKEN + sysUser.getId());
        cacheUtil.delete(CommonConstant.TOKEN_CACHE_NAME, CommonConstant.LOGIN_USER_CACHE_RULES_ROLE + sysUser.getUsername());
        cacheUtil.delete(CommonConstant.TOKEN_CACHE_NAME, CommonConstant.LOGIN_USER_CACHE_RULES_PERMISSION + sysUser.getUsername());
        return Result.ok("退出登录成功！");
    }

    /**
     * 获取访问量
     */
    @GetMapping("loginfo")
    public Result<JSONObject> loginfo() {
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject obj = new JSONObject();
        //update-begin--Author:zhangweijian  Date:20190428 for: 传入开始时间, 结束时间参数
        // 获取一天的开始和结束时间
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dayStart = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date dayEnd = calendar.getTime();
        // 获取系统访问记录
        Long totalVisitCount = logService.findTotalVisitCount();
        obj.put("totalVisitCount", totalVisitCount);
        Long todayVisitCount = logService.findTodayVisitCount(dayStart, dayEnd);
        obj.put("todayVisitCount", todayVisitCount);
        Long todayIp = logService.findTodayIp(dayStart, dayEnd);
        //update-end--Author:zhangweijian  Date:20190428 for: 传入开始时间, 结束时间参数
        obj.put("todayIp", todayIp);
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }

    /**
     * 获取访问量
     *
     * @return
     */
    @GetMapping("visitInfo")
    public Result<List<Map<String, Object>>> visitInfo() {
        Result<List<Map<String, Object>>> result = new Result<List<Map<String, Object>>>();
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date dayEnd = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date dayStart = calendar.getTime();
        List<Map<String, Object>> list = logService.findVisitCount(dayStart, dayEnd);
        result.setResult(oConvertUtils.toLowerCasePageList(list));
        return result;
    }


    /**
     * 登陆成功选择用户当前部门
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/selectDepart", method = RequestMethod.PUT)
    public Result<?> selectDepart(@RequestBody SysUser user) {
        String username = user.getUsername();
        if (oConvertUtils.isEmpty(username)) {
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            username = sysUser.getUsername();
        }
        String orgCode = user.getOrgCode();
        this.sysUserService.updateUserDepart(username, orgCode);
        return Result.ok();
    }


    /**
     * 用户信息
     *
     * @param sysUser
     * @param result
     * @return
     */
    private Result<JSONObject> userInfo(SysUser sysUser, Result<JSONObject> result) {
        String syspassword = sysUser.getPassword();
        String username = sysUser.getUsername();
        String userId = sysUser.getId();
        String parkCode = "";
        String parkName = "";
        // 生成token
        String token = JwtUtil.sign(username, userId, parkCode, parkName, syspassword);
        cacheUtil.put(CommonConstant.TOKEN_CACHE_NAME, CommonConstant.PREFIX_USER_TOKEN + userId, token);
        // 获取用户部门信息
        JSONObject obj = new JSONObject();
        obj.put("token", token);
        obj.put("userInfo", sysUser);
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }


    /**
     * 获取加密字符串
     */
    @GetMapping(value = "/getEncryptedString")
    public Result<Map<String, String>> getEncryptedString() {
        Result<Map<String, String>> result = new Result<>();
        Map<String, String> map = new HashMap<>(8);
        map.put("key", EncryptedString.key);
        map.put("iv", EncryptedString.iv);
        result.setResult(map);
        return result;
    }
}
