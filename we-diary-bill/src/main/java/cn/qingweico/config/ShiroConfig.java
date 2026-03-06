package cn.qingweico.config;

import cn.qingweico.modules.shiro.authc.ShiroRealm;
import cn.qingweico.modules.shiro.authc.aop.JwtFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: Scott
 * @date: 2018/2/7
 * @description: shiro 配置类
 */

@Configuration
public class ShiroConfig {

	/**
	 * Filter Chain定义说明
	 *
	 * 1、一个URL可以配置多个Filter, 使用逗号分隔
	 * 2、当设置多个过滤器时, 全部验证通过, 才视为通过
	 * 3、部分过滤器可指定参数, 如perms, roles
	 */
	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 拦截器
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

		//健康检查排除
		filterChainDefinitionMap.put("/actuator/health", "anon");
		//登录接口排除
		filterChainDefinitionMap.put("/operationLog/parkHandOverLog/LoginHaveStation", "anon");
		filterChainDefinitionMap.put("/sys/login", "anon");
		filterChainDefinitionMap.put("/sys/captcha", "anon");
		//sdk接口排除
		//升级后进行场内车入场数据填充数据
		filterChainDefinitionMap.put("/sdk/**", "anon");
		filterChainDefinitionMap.put("/dbUpgrade", "anon");
		//验证码接口排除
		filterChainDefinitionMap.put("/sys/getCaptchaTest", "anon");
		//手持端登录接口排除
		filterChainDefinitionMap.put("/sys/mobile/login", "anon");
		//获取加密串
		filterChainDefinitionMap.put("/sys/getEncryptedString", "anon");
		//校验用户是否存在
		filterChainDefinitionMap.put("/sys/user/checkOnlyUser", "anon");
		//用户更改密码
		filterChainDefinitionMap.put("/sys/user/passwordChange", "anon");
		//登录验证码
		filterChainDefinitionMap.put("/auth/2step-code", "anon");
		//图片预览不限制token
		filterChainDefinitionMap.put("/sys/common/view/**", "anon");
		//图片预览不限制token
		filterChainDefinitionMap.put("/sys/common/twomisson/**", "anon");
		//文件下载不限制token
		filterChainDefinitionMap.put("/sys/common/download/**", "anon");
		//pdf预览
		filterChainDefinitionMap.put("/sys/common/pdf/**", "anon");
		//pdf预览需要文件
		filterChainDefinitionMap.put("/generic/**", "anon");
		//websocket放开token
		filterChainDefinitionMap.put("/websocket/**", "anon");
		filterChainDefinitionMap.put("/videoWsService/**", "anon");
		//日报接口
		filterChainDefinitionMap.put("/parkdayreport/**", "anon");
		filterChainDefinitionMap.put("/doc.html", "anon");
		filterChainDefinitionMap.put("/**/*.js", "anon");
		filterChainDefinitionMap.put("/**/*.css", "anon");
		filterChainDefinitionMap.put("/**/*.html", "anon");
		filterChainDefinitionMap.put("/**/*.svg", "anon");
		filterChainDefinitionMap.put("/**/*.jpg", "anon");
		filterChainDefinitionMap.put("/**/*.png", "anon");
		filterChainDefinitionMap.put("/**/*.ico", "anon");
		filterChainDefinitionMap.put("/druid/**", "anon");
		filterChainDefinitionMap.put("/swagger-ui.html", "anon");
		filterChainDefinitionMap.put("/swagger**/**", "anon");
		filterChainDefinitionMap.put("/webjars/**", "anon");
		filterChainDefinitionMap.put("/v2/**", "anon");
		filterChainDefinitionMap.put("/**/*/test", "anon");
		filterChainDefinitionMap.put("/monthlyWebSocket/**", "anon");
		//新岗亭
		filterChainDefinitionMap.put("/sentryBox/login", "anon");
		filterChainDefinitionMap.put("/sentryBox/getParkWorkStationList", "anon");


		// 添加自己的过滤器并且取名为jwt
		Map<String, Filter> filterMap = new HashMap<>(1);
		filterMap.put("jwt", new JwtFilter());
		shiroFilterFactoryBean.setFilters(filterMap);
		// <!-- 过滤链定义, 从上向下顺序执行, 一般将/**放在最为下边
		filterChainDefinitionMap.put("/**", "jwt");

		// 未授权界面返回JSON
		shiroFilterFactoryBean.setUnauthorizedUrl("/sys/common/403");
		shiroFilterFactoryBean.setLoginUrl("/sys/common/403");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	@Bean("securityManager")
	public DefaultWebSecurityManager securityManager(ShiroRealm myRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(myRealm);

		/*
		 * 关闭shiro自带的session, 详情见文档
		 * http://shiro.apache.org/session-management.html#SessionManagement-
		 * StatelessApplications%28Sessionless%29
		 */
		DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
		DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
		defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
		subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
		securityManager.setSubjectDAO(subjectDAO);

		return securityManager;
	}

	/**
	 * 下面的代码是添加注解支持
	 * @return
	 */
	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}

}
