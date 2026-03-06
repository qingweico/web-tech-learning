/**
 *
 */
package cn.qingweico.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能:
 * 版本: 1.0
 * 开发人员: grh
 * 创建日期: 2021年02月20日
 * 修改日期: 2021年02月20日
 * description:车辆类型
 */
public enum CarType {

	UNKNOWN(0,"未知车牌",0),
    BLUE(1,"蓝牌小汽车",0),
	SGINLEYELLOW(3,"单排黄牌",0),
    DOUBLEYELLOW(4,"双排黄牌",0),
    POLICE(5,"警车车牌",1),
    ARMEDPOLICE(6,"武警车牌",1),
    SPECIAL(7,"个性化车牌",0),
    SGINLEARMY(8,"单排军车牌",1),
    DOUBLEARMY(9,"双排军车牌",1),
    EMBASSY(10,"使馆车牌",1),
    HK(11,"香港进出中国大陆车牌",0),
    ARGICULTURE(12,"农用车牌",0),
    TRAIN(13,"教练车牌",0),
    MACAO(14,"澳门进出中国大陆车牌",0),
    DBARMEDPOLICE(15,"双层武警车牌",1),
    GENELARMEDPOLICE(16,"武警总队车牌",1),
    DBGENELARMEDPOLICE(17,"双层武警总队车牌",1),
    CIVIL(18,"民航车牌",0),
    ENERGY(19,"新能源车牌",0);

	private final Integer value;
	private final String name;
    private final Integer property;

	private CarType(Integer value, String name, Integer property) {
		this.value = value;
		this.name = name;
        this.property = property;
	}

	public Integer getValue() {
		return value;
	}

    public Integer getProperty() {
        return property;
    }

    public String getName() {
		return name;
	}



	public static CarType getEnumByCode(int value){
	    for(CarType vl : CarType.values()){
	      if(vl.getValue()==value){
	        return vl;
	      }
	    }
	    return null;
	  }
    public static List<String> getFreeEnum(){
        List<String> result = new ArrayList<>();
        for(CarType vl : CarType.values()){
            if(vl.getProperty()==1){
                result.add(vl.getValue().toString());
            }
        }
        return result;
    }
}
