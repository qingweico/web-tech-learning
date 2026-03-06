package cn.qingweico.common.system.base.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

public class SuperEntity<T extends Model<?>> extends Model<T> implements Serializable {
}
