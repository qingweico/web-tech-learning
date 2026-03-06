package cn.qingweico.common.util;

/**
 * @Description: 车辆类型颜色转换
 * @author: ZhuLiuWei
 * @date: 2022/3/8 9:30
 */
public enum CarColorEnum {

    UNKNOWN("0", "未知车牌", "未知"),
    BLUE("1", "蓝牌小汽车", "蓝色"),
    SINGLE_YELLOW("3", "单排黄牌", "黄色"),
    DOUBLE_YELLOW("4", "双排黄牌", "黄色"),
    POLICE("5", "警车车牌", "白色"),
    ARMED_POLICE("6", "武警车牌", "白色"),
    SPECIAL("7", "个性化车牌", "未知"),
    SINGLE_ARMY("8", "单排军车牌", "白色"),
    DOUBLE_ARMY("9", "双排军车牌", "白色"),
    EMBASSY("10", "使馆车牌", "黑色"),
    HK("11", "香港进出中国大陆车牌", "黑色"),
    AGRICULTURE("12", "农用车牌", "黄色"),
    TRAIN("13", "教练车牌", "黄色"),
    MACAO("14", "澳门进出中国大陆车牌", "黑色"),
    DB_ARMED_POLICE("15", "双层武警车牌", "白色"),
    GENERAL_ARMED_POLICE("16", "武警总队车牌", "白色"),
    DB_GENERAL_ARMED_POLICE("17", "双层武警总队车牌", "白色"),
    CIVIL("18", "民航车牌", "绿色"),
    ENERGY("19", "新能源车牌", "绿色");

    private final String value;
    private final String name;
    private final String color;

    CarColorEnum(String value, String name, String color) {
        this.value = value;
        this.name = name;
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public static CarColorEnum getEnumByCode(String value) {
        for (CarColorEnum vl : CarColorEnum.values()) {
            if (vl.getValue().equals(value)) {
                return vl;
            }
        }
        return UNKNOWN;
    }
}
