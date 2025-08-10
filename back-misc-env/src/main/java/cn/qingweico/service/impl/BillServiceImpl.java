package cn.qingweico.service.impl;

import cn.qingweico.entity.Bill;
import cn.qingweico.mapper.BillMapper;
import cn.qingweico.service.BillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zqw
 * @date 2023/6/22
 */
@Slf4j
@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

}
