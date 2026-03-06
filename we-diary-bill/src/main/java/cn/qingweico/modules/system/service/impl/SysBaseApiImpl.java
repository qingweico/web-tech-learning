package cn.qingweico.modules.system.service.impl;

import cn.qingweico.model.BusinessException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.qingweico.common.system.api.ISysBaseAPI;
import cn.qingweico.common.system.vo.DictModel;
import cn.qingweico.common.system.vo.LoginUser;
import cn.qingweico.common.util.IPUtils;
import cn.qingweico.common.util.SpringContextUtils;
import cn.qingweico.common.util.oConvertUtils;
import cn.qingweico.modules.system.entity.SysDict;
import cn.qingweico.modules.system.entity.SysLog;
import cn.qingweico.modules.system.entity.SysUser;
import cn.qingweico.modules.system.mapper.SysLogMapper;
import cn.qingweico.modules.system.mapper.SysUserMapper;
import cn.qingweico.modules.system.mapper.SysUserRoleMapper;
import cn.qingweico.modules.system.service.ISysDictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhouqingwei
 */
@Slf4j
@Service
public class SysBaseApiImpl implements ISysBaseAPI {
    public static final String DB_TYPE_MYSQL = "MYSQL";
    public static final String DB_TYPE_ORACLE = "ORACLE";
    public static final String DB_TYPE_POSTGRESQL = "POSTGRESQL";
    public static final String DB_TYPE_SQLSERVER = "SQLSERVER";
    public static String DB_TYPE = "";

    @Resource
    private SysLogMapper sysLogMapper;
    @Resource
    private SysUserMapper userMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;
    @Resource
    private ISysDictService sysDictService;

    @Override
    public void addLog(String logContent, Integer logType, Integer operatetype) {
        SysLog sysLog = new SysLog();
        sysLog.setLogContent(logContent);
        sysLog.setLogType(logType);
        sysLog.setOperateType(operatetype);
        try {
            HttpServletRequest request = SpringContextUtils.getHttpServletRequest();
            sysLog.setIp(IPUtils.getIpAddr(request));
        } catch (Exception e) {
            sysLog.setIp("127.0.0.1");
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (sysUser != null) {
            sysLog.setUserid(sysUser.getUsername());
            sysLog.setUsername(sysUser.getRealname());
        }
        sysLog.setCreated(new Date());
        //保存系统日志
        sysLogMapper.insert(sysLog);
    }

    @Override
    public LoginUser getUserByName(String username) {
        if (oConvertUtils.isEmpty(username)) {
            return null;
        }
        LoginUser loginUser = new LoginUser();
        SysUser sysUser = userMapper.getUserByName(username);
        if (sysUser == null) {
            return null;
        }
        BeanUtils.copyProperties(sysUser, loginUser);
        return loginUser;
    }

    @Override
    public List<String> getRolesByUsername(String username) {
        return sysUserRoleMapper.getRoleByUserName(username);
    }

    @Override
    public String getDatabaseType() throws SQLException {
        DataSource dataSource = SpringContextUtils.getApplicationContext().getBean(DataSource.class);
        return getDatabaseTypeByDataSource(dataSource);
    }

    @Override
    public List<DictModel> queryDictItemsByCode(String code) {
        return sysDictService.queryDictItemsByCode(code);
    }

    @Override
    public List<DictModel> queryTableDictItemsByCode(String table, String text, String code) {
        return sysDictService.queryTableDictItemsByCode(table, text, code);
    }

    @Override
    public List<DictModel> queryAllDepartBackDictModel() {
        return sysDictService.queryAllDepartBackDictModel();
    }


    /**
     * 获取数据库类型
     *
     * @param dataSource
     * @return
     */
    private String getDatabaseTypeByDataSource(DataSource dataSource) {
        if ("".equals(DB_TYPE)) {
            try (Connection connection = dataSource.getConnection()) {
                DatabaseMetaData md = connection.getMetaData();
                String dbType = md.getDatabaseProductName().toLowerCase();
                if (dbType.contains("mysql")) {
                    DB_TYPE = DB_TYPE_MYSQL;
                } else if (dbType.contains("oracle")) {
                    DB_TYPE = DB_TYPE_ORACLE;
                } else if (dbType.contains("sqlserver") || dbType.contains("sql server")) {
                    DB_TYPE = DB_TYPE_SQLSERVER;
                } else if (dbType.contains("postgresql")) {
                    DB_TYPE = DB_TYPE_POSTGRESQL;
                } else {
                    throw new BusinessException("数据库类型:[" + dbType + "]不识别!");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return DB_TYPE;

    }

    @Override
    public List<DictModel> queryAllDict() {
        // 查询并排序
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<SysDict>();
        queryWrapper.orderByAsc("created");
        List<SysDict> sysDictList = sysDictService.list(queryWrapper);
        // 封装成 model
        List<DictModel> list = new ArrayList<>();
        for (SysDict dict : sysDictList) {
            list.add(new DictModel(dict.getDictCode(), dict.getDictName()));
        }

        return list;
    }
}
