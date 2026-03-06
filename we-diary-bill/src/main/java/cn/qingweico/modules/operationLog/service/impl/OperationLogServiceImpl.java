package cn.qingweico.modules.operationLog.service.impl;

import cn.qingweico.modules.operationLog.entity.OperationLog;
import cn.qingweico.modules.operationLog.mapper.OperationLogMapper;
import cn.qingweico.modules.operationLog.service.IOperationLogService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @author zqw
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

}
