package cn.qingweico.common.system.vo;

import cn.qingweico.common.util.DateUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author zhouqingwei
 */
@Setter
@Getter
public class SysUserCacheInfo {

	private String sysUserCode;

	private String sysUserName;

	private String sysOrgCode;

	private List<String> sysMultiOrgCode;

	private boolean oneDepart;

    public String getSysDate() {
		return DateUtils.formatDate();
	}

	public String getSysTime() {
		return DateUtils.now();
	}

}
