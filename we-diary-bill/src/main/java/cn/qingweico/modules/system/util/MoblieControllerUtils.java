package cn.qingweico.modules.system.util;

/**
 * @author
 * */
public class MoblieControllerUtils {
    public static String QUERYSUCCESS = "查询成功！";
    public static String RECORDS = "records";
    public static String TOATL = "total";
    public static String SIZE = "size";
    public static String CURRENT = "current";
    public static String ORDERS = "orders";
    public static String SEARCHCOUNT = "searchCount";
    public static String PAGES = "pages";
    public static boolean TRUE = true;
    public static boolean FALSE = false;
    public static String FAILONDOING = "运行时错误";
    public static Integer FAILINTCODE = -1;
    /**白名单*/
    public static Integer BEWHITELIST = 1;
    /**黑名单*/
    public static Integer BEBLACKLIST = 2;
    /**启用*/
    public static String ENABLE = "Y";
    /**冻结*/
    public static String FREEZE = "N";
    /**所有入场车辆*/
    public static String ALLENTERCARS = "0";
    /**入场后仍在场内的车辆*/
    public static String INSIDEDENTERCARS = "1";
    /**入场后已离场的车辆*/
    public static String OUTSIDEDENTERCARS = "2";
    /** 无牌车 cardNo (0,无牌车, 1,有牌车)*/
    public static String NONEPLATE = "0";
    /** 有牌车 cardNo (0,无牌车, 1,有牌车)*/
    public static String HAVEPLATE = "1";
    /**正常的车场出入口  (1: 正常 ; 2: 禁止)*/
    public static String ARMNOMAL = "1";
    /**被禁止的车场出入口  (1: 正常 ; 2: 禁止)*/
    public static String ARMFORBIT = "2";
    /** 停车订单 未支付*/
    public static String UNPAID = "1";
    /**停车订单 支付成功*/
    public static String PAIDSUCCESS = "2";
    /**停车订单 支付失败*/
    public static String PAIDFAIL = "3";
    /**arm_type (0:进口;1:出口)*/
    public static String ARMTYPEENTER = "0";
    /**arm_type (0:进口;1:出口)*/
    public static String ARMTYPEOUT = "1";
    public static String PARKNOTEXIST = "车场找不到";
    /**临时车位满位禁止通行(0, 关闭, 1、开启)*/
    public static String PERMITTEMPCARIN = "0";
    /** 临时车位满位禁止通行(0, 关闭, 1、开启)*/
    public static String FORBITTEMPCARIN = "1";
    public static String CONFIGSUCCESS = "配置成功！";
    public static String CONFIGFAIL = "配置失败！";
    public static String PARMMUSTNOTEMPTY = "参数必须非空！";
    public static String VZICARPDNSURL = "https://www.vzicar.com/pdns";
    public static String VZICARPDNSPORT = "9080";
    public static String MOBILECONTROLLEREXCEPTION = "手持端接口调用异常";
    public static String NOPOWER = "没有操作权限！";
    public static String OPRATETYPE = "oprate";
    public static String OPRATERESULT = "message";
    public static String OPRATESUCCESS = "操作成功！";
    public static String OPRATEFAILY = "操作失败！";
    public static String SERIALMISSION = "失败未找到！";
    public static String OPRATERESULTMISSION = "未返回操作结果！";
    public static String FLAG = "flag";
    public static String USERID = "userId";
    public static String USERNAME = "userName";
    public static String PASSWORD = "passWord";
    public static String TOKENBENULL = "token为空";
    public static String PARAMCONFIGERROR = "车场重复配置";
    public static String UNDEFIND = "检测设备状态异常";
    public static String MOREPARKINGBEFOUND = "车场重复";
    /**收费模式(0, 场端收费 1, 云端收费) */
    public  static String PAYONCLOUDMODE = "1";
    /**收费模式(0, 场端收费 1, 云端收费) */
    public  static String PAYONMONOMERMODE = "0";
    public  static String CHANGEPAYMODETOMONOMER = "切换收费模式至车场缴费";
    public  static String CHANGEPAYMODETOCLOUD = "切换收费模式至云缴费";
    public  static String CHANGEPAYMODETOMONOMERSUCCESS = "收费模式已切换至车场缴费";
    public  static String CHANGEPAYMODETOCLOUDSUCCESS = "收费模式已切换至云缴费";
    public  static String PARKCODEBEBLACK = "车场编号空";
    public  static String GETPARAMSFAILY = "获取参数失败";
    public  static String PARAMERROR = "配置参数错误";
    public  static String PARAMBEEMPTY = "配置参数为空";
    /**临时车位满位禁止通行 temporary_stall_type(0, 关闭, 1、开启) */
    public  static String TEMPORARYSTALLTYPECLOSE = "0";
    /**临时车位满位禁止通行 temporary_stall_type (0, 关闭, 1、开启) */
    public  static String TEMPORARYSTALLTYPOPEN = "1";
    public  static String CHARGEMODE = "chargeMode";
    public  static String TEMPORARYSTALLTYPE = "temporaryStallType";
    public  static String CHARGEMODEERROR = "chargeModeError";
    public  static String TEMPORARYSTALLTYPEERROR = "temporaryStallTypeError";
    public  static String CHARGEMODEQUERYFLAG = "chargeModeQueryFlag";
    public  static String TEMPORARYSTALLTYPQUERYFLAG = "temporaryStallTypeQueryFlag";
    public  static String BLACKSTRING = "";
}
