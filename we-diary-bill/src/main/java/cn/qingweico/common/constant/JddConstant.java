package cn.qingweico.common.constant;



/**
 * @author zhouqingwei
 */
public class JddConstant {

    /**
     * 参数校验失败 520
     */
    public static final Integer SC_JEECG_NO_PARAM = 520;

    /**
     * 字符串常量
     */
    public class StringNumber {
        /**
         *  字符串常量 -1
         */
        public static final String STRING_NEGATIVE_ONE ="-1";
        /**
         * 字符串常量 0
         */
        public static final String STRING_ZERO = "0";
        /**
         * 字符串常量 1
         */
        public static final String STRING_ONE = "1";
        /**
         * 字符串常量 2
         */
        public static final String STRING_TWO = "2";
        /**
         * 字符串常量 3
         */
        public static final String STRING_THREE = "3";
        /**
         * 字符串常量 4
         */
        public static final String STRING_FOUR = "4";
        /**
         * 字符串常量 5
         */
        public static final String STRING_FIVE = "5";
        /**
         * 字符串常量 6
         */
        public static final String STRING_SIX = "6";
        /**
         * 字符串常量 7
         */
        public static final String STRING_SEVEN = "7";
        /**
         * 字符串常量 8
         */
        public static final String STRING_EIGHT = "8";


    }

    public class DaoZha{
        /**
         * 遥控开闸
         */
        public static final String REMOTE_CONTROL = "AB550178010600000000AF";

    }

    /**
     * 数字类型常量
     */

    public class IntNumber {
        /**
         * 字符串常量 0
         */
        public static final int INT_ZERO = 0;
        /**
         * 字符串常量 1
         */
        public static final int INT_ONE = 1;
        /**
         * 字符串常量 2
         */
        public static final int INT_TWO = 2;
        /**
         * 字符串常量 3
         */
        public static final int INT_THREE = 3;

        /**
         * 字符串常量 4
         */
        public static final int INT_FOUR = 4;

        /**
         * 字符串常量 5
         */
        public static final int INT_FIVE = 5;

        /**
         * 字符串常量 6
         */
        public static final int INT_SIX = 6;

        /**
         * 字符串常量 7
         */
        public static final int INT_SEVEN = 7;

        /**
         * 字符串常量 8
         */
        public static final int INT_EIGHT = 8;

        /**
         * 字符串常量 9
         */
        public static final int INT_NINE = 9;




    }

    /**
     * @Description: 车场信息常量类
     */
    public class ParkInfo {


        /**
         * 收费模式:  0 场端收费
         */
        public static final String CHARGEMODE_ZERO = "0";


        /**
         * 收费模式:  1 云端收费
         */
        public static final String CHARGEMODE_ONE = "1";
    }

    /**
     * @Description: 车辆识别结果常量类
     */
    public class MqttRecResultPushLog {
        /**
         * 车辆类型: 0 无牌车
         */
        public static final String TYPE_ZERO = "0";

        /**
         * 是否是月租车 0: 临时车
         */
        public static final String IS_WHITE_ZERO = "0";
        /**
         * 是否是月租车 1: 月租车
         */
        public static final String IS_WHITE_ONE = "1";
        /**
         * 是否是月租车 2: 储值车
         */
        public static final String IS_WHITE_TWO = "2";
        /**
         * 是否是月租车 3: 军警车
         */
        public static final String IS_WHITE_THREE = "3";
        /**
         * 是否是月租车 4: 白名单
         */
        public static final String IS_WHITE_FOUR = "4";
        /**
         * 无牌车类型
         */
        public static final String THREE_ONE = "31";
    }

    /**
     * 停车场出入口配置信息常量类
     */
    public class ParkArmInfo {

        /**
         * 停车场出入口配置信息 armType 0: 入口
         */
        public static final String ARM_TYPE_ZERO = "0";
        /**
         * 停车场出入口配置信息 armType 1: 出口
         */
        public static final String ARM_TYPE_ONE = "1";
        /**
         * 停车场车道启用状态 armStatus 1:启用
         */
        public static final String PARK_ARM_STATUS_TRUE = "1";
        /**
         * 停车场车道启用状态 armStatus 1:启用
         */
        public static final String PARK_ARM_STATUS_FALSE = "0";
    }


    /**
     * 车场临时车、月租车过期配置 常量类
     */
    public class ConfigParkInfo {

        /**
         * 无入场记录是否开闸  1: 不开闸
         */
        public static final String ADMISSION_RECORDS_CAR_ONE = "1";
        /**
         * 无入场记录是否开闸  2: 免费放行
         */
        public static final String ADMISSION_RECORDS_CAR_TWO = "2";


        /**
         * 月租车过期处理方式 1: 月租车过期禁止入场
         */
        public static final String HANDLE_MONTHLY_RENTAL_CAR_ONE = "1";
        /**
         * 月租车过期处理方式 2: 月租车过期按临时车收费
         */
        public static final String HANDLE_MONTHLY_RENTAL_CAR_TWO = "2";
        /**
         * 月租车过期处理方式  3: 过期n天后禁止入场
         */
        public static final String HANDLE_MONTHLY_RENTAL_CAR_THREE = "3";

        /**
         * 军警车自动放行 0: 关闭
         */
        public static final String POLICE_CAR_ZERO = "0";
        /**
         * 军警车自动放行 1: 开启
         */
        public static final String POLICE_CAR_ONE = "1";

        /**
         * 月租车入场开闸方式 1: 自动开闸
         */
        public static final String MONTHLY_RENTAL_GATE_IN_ONE = "1";
        /**
         * 月租车入场开闸方式 2: 确认开闸
         */
        public static final String MONTHLY_RENTAL_GATE_IN_TWO = "2";


        /**
         * 临时车位满位禁止通行 0: 关闭
         */
        public static final String TEMPORARY_STALL_TYPE_ZERO = "0";
        /**
         * 临时车位满位禁止通行 1: 开启
         */
        public static final String TEMPORARY_STALL_TYPE_ONE = "1";


        /**
         * 临时车入场开闸方式 1: 自动开闸
         */
        public static final String TEMPORARY_GATE_IN_ONE = "1";
        /**
         * 临时车入场开闸方式 2: 确认开闸
         */
        public static final String TEMPORARY_GATE_IN_TWO = "2";
        /**
         * 临时车入场开闸方式  3: 不开闸
         */
        public static final String TEMPORARY_GATE_IN_THREE = "3";

    }

    /**
     * 白名单常量类
     */
    public class ParkWhiteListConstant {
        /**
         * 是否是白名单 1: 白名单
         */
        public static final String LICENSE_TYPE_ONE = "1";
        /**
         * 是否是白名单 2:黑名单
         */

        public static final String LICENSE_TYPE_TWO = "2";
        /**
         * 白名单状态 Y启用
         */
        public static final String LICENSE_STATUS_Y = "Y";
        /**
         * 白名单状态 N启用
         */
        public static final String LICENSE_STATUS_N = "N";

        /**
         * 是否是白名单 3:储值车
         */

        public static final String LICENSE_TYPE_THREE = "3";
    }

    /**
     * mqtt 常量类
     */
    public class MqttConstant {
        public static final String CMD = "cmd";

        public static final String ERROR_MSG = "error_msg";

        public static final String MSG_ID = "msgId";

        public static final String STATE_CODE = "state_code";

        public static final String MANUAL_TRIGGER = "manualTrigger";

        public static final String INFO = "info";

        public static final String SERIAL_DATA = "serialData";

        public static final String SET_DATETIME = "set_datetime";

        public static final String TRIGGER_IMAGE = "TriggerImage";

        public static final String WHITE_LIST_OPERATE = "white_list_operate";

        public static final String HEART_BEAT = "heartbeat";

        public static final String RESULT = "result";

        public static final String ALARM_INFO_PLATE = "AlarmInfoPlate";
    }

    /**
     * 扫码类型常量类
     */
    public class ScanCodeTypeConstant {
        /**
         * 场内码
         */
        public static final int CODE_TYPE_IN_PARK = 1;

        /**
         * 出口码
         */
        public static final int CODE_TYPE_EXIT_PARK = 2;

    }

    /**
     * 停车场开闸日志常量类
     */

    public class SdkOpenGateLog {

        /**
         * 开闸类型 1, 自动开闸
         */
        public static final String SWICH_TYPE_ONE = "1";
        /**
         * 开闸类型 0, 场端开闸
         */
        public static final String SWICH_TYPE_ZERO = "0";
        /**
         * 开闸类型 4, 补录场内开闸
         */
        public static final String SWICH_TYPE_CREATECARINTER = "4";
        /**
         * 开闸类型 5, 匹配场内开闸
         */
        public static final String SWICH_TYPE_MATCING = "5";
    }

    /**
     * 优惠券常量类
     */
    public class DiscountCouponConstant {
        /**
         * 类型(兼容免费券):1普通券2免费券-同行时长限制
         */
        public static final int COUPON_TYPE_ORDINARY = 1;
        /**
         * 类型(兼容免费券):1普通券2免费券-同行时长限制
         */
        public static final int CODE_TYPE_FREE_TIME = 2;

        /**
         * 获取方式1.人工发券2.自助领券
         */
        public static final int OBTAIN_WAY_MANUAL = 1;
        /**
         * 获取方式1.人工发券2.自助领券
         */
        public static final int CODE_TYPE_SELF = 2;

        /**
         * 使用情况 1.未使用 2.已使用
         */
        public static final int USAGE_STATUS_NOT_USED = 1;
        /**
         * 使用情况 1.未使用 2.已使用
         */
        public static final int USAGE_STATUS_USED = 2;

        /**
         * 状态(1启用, 0不启用)
         */
        public static final int STATUS_ENABLE = 1;
        /**
         * 状态(1启用, 0不启用)
         */
        public static final int STATUS_NOT_ENABLE = 0;

        /**
         * 是否平台券 1.平台券 2.商家券
         */
        public static final int RANGE_PLATFORM = 1;
        /**
         * 是否平台券 1.平台券 2.商家券
         */
        public static final int RANGE_MERCHANT = 2;

        /**
         * 优惠类型1.全免券 2.小时券3.折扣券4.现金券
         */
        public static final int DISCOUNT_TYPE_FREE = 1;
        /**
         * 优惠类型1.全免券 2.小时券3.折扣券4.现金券
         */
        public static final int DISCOUNT_TYPE_HOUR = 2;
        /**
         * 优惠类型1.全免券 2.小时券3.折扣券4.现金券
         */
        public static final int DISCOUNT_TYPE_REBATE = 3;
        /**
         * 优惠类型1.全免券 2.小时券3.折扣券4.现金券
         */
        public static final int DISCOUNT_TYPE_CASH = 4;

        /**
         * 是否删除 0.未删除 1.已删除
         */
        public static final int IS_DELETED_NO = 0;
        /**
         * 是否删除 0.未删除 1.已删除
         */
        public static final int IS_DELETED_YES = 1;
    }

    /**
     * 计费常量类
     */

    public static final class ChargeConstant {
        /**
         * 计费规则不存在
         */
        public static final Integer CHARGE_FEE_FIND_NULL = 404;
        /**
         * 免费时段关闭
         */
        public static final Integer FREE_TIME_STATUS_CLOSE = 2;
        /**
         * 是第一次算费
         */
        public static final boolean FIRST_CHARGE_FEE_TRUE = true;
        /**
         * 计费规则类型: 按次收费
         */
        public static final Integer CHARGE_MODE_ONE_TIMES = 1;
        /**
         * 计费规则类型: 按时间段收费
         */
        public static final Integer CHARGE_MODE_TIME = 2;
        /**计费规则模块,计费类型: 阶梯计费*/
        public static final Integer CHARGE_RULE_MODULE_CHARGE_TYPE_STEP = 3;
        /**
         * 计费规则类型: 阶梯计费
         */
        public static final Integer CHARGE_MODE_STEP = 3;
        /**
         * 收取首计时费用
         */
        public static final Integer CHARGE_FIRST_CHARGE_TRUE = 1;
        /**
         * 计时余数不计算
         */
        public static final Integer TIME_REMAINDER_MODE_N = 1;
        /**
         * 计时数加一
         */
        public static final Integer TIME_REMAINDER_MODE_Y = 2;
        /**
         * 从下一时段补足,不足计时单位向上取整
         * */
        public static final Integer TIME_REMAINDER_MODE_NEXT_UP = 3;
        /**
         * 区间最高限额是否启用
         */
        public static final Integer MONEY_LIMIT_ENABLE = 1;
        /**
         * 24小时的小时数
         */
        public static final int DAY_NIGHT_HOURES = 24;

        /**
         * 24小时的分钟数
         */
        public static final int DAY_NIGHT_MINUTES = 1440;
        /**
         * 是否以24小时计算一天
         */
        public static final int TWENTY_FOUR_HOUR_ENABLE_TRUE = 1;
        /**
         * 是否以自然日计算一天
         */
        public static final int TWENTY_FOUR_HOUR_ENABLE_FALSE = 2;
        public static final int TWENTY_FOUR_HOUR_NO_DIVIDE = 3;
        /**
         * 每日最高限额启用
         */
        public static final int LIMIT_MONEY_ENABLE_TRUE = 1;

        /***
         * redis保存计费信息过期时间,单位分钟
         */
        public static final int SAVE_FEE_TIMOUT = 10;
        /**
         * 进场白天
         */
        public static final int ENTER_DAY = 0;

        /**
         * 进场晚上
         */
        public static final int ENTER_NIGHT = 1;

        /**
         * 出场白天
         */
        public static final int OUT_DAY = 2;

        /**
         * 出场晚上
         */
        public static final int OUT_NIGHT = 3;

        /**
         * 按时段增量计费
         */
        public static final String TIME_PERIOD_CAL = "0";

        /**
         * 按白天夜间计费
         */
        public static final String DAY_NIGHT_CAL = "1";
        /**
         * 免费时段开启
         */
        public static final String FREE_TIME_STATUS_OPEN = "Y";

        /**
         * 计时余数比较小于
         */
        public static final int FEE_MINUTES_COMPARE_LESS = -1;
        /**
         * 计时余数比较等于
         */
        public static final int FEE_MINUTES_COMPARE_EQUAL = 0;
        /**
         * 计时余数比较大于
         */
        public static final int FEE_MINUTES_COMPARE_GREATER = 1;

    }

    public static final class ParkCarEnterLog {
        /**
         * 进场车辆类型(0,无牌车, 1,有牌车)
         */
        public static final String UNVEHICLE = "0";
        /**
         * 进场车辆类型(0,无牌车, 1,有牌车)
         */
        public static final String VEHICLE = "1";
        /**
         * 车辆未出场
         */
        public static final String CAR_NO_OUT = "0";
        /**
         * 车辆已出场
         */
        public static final String CAR_OUT = "1";
        /**正常入场*/
        public static final String IS_TRANSFER_NO = "0";
        /**已转卡记录*/
        public static final String IS_TRANSFER_NO_YET = "1";
        /**未转卡记录*/
        public static final String IS_TRANSFER_ALREADY = "2";
    }

    /**
     * 停车 出场无入场记录常量类
     */
    public final class AdmissionRecordsCarType {

        /**
         * 不开闸
         */
        public static final String NO_OPEN = "1";
        public static final int NO_OPEN_CODE = 201;
        public static final String NO_OPEN_MESSAGE = "未查询到入场记录";

        /**
         * 免费放行
         */
        public static final String FREE_RELEASE = "2";
        public static final int FREE_RELEASE_CODE = 202;
        public static final String FREE_RELEASE_MESSAGE = "祝您一路顺风!";

        /**
         * 自动补入场记录
         */
        public static final String AUTOMATIC_RECORD = "3";
    }


    /**
     * 保存云端入场/出场记录 成功失败状态
     */
    public final class SaveCloudType {
        /**
         * 保存成功
         */
        public static final String SAVE_SUCCESS = "Y";

        /**
         * 保存失败
         */
        public static final String SAVE_ERROR = "N";
    }

    public final class OrderConstant {

        /**
         * 未支付
         */
        public static final String ORDER_PAY_STATE_UNPAID = "1";
        /**
         * 支付成功
         */
        public static final String ORDER_PAY_STATE_SUCCESS = "2";
        /**
         * 支付失败
         */
        public static final String ORDER_PAY_STATE_FAILED = "3";
    }

    public final class SystemType {
        /**
         * linux
         */
        public static final String LINUX_SYSTEM = "0";

        /**
         * windows
         */
        public static final String WINDOWS_SYSTEM = "1";
    }

    public final class VehicleType {
        /**
         * 临时车
         */
        public static final String TEMPORARYCAR = "临时车";

        /**
         * 储值车
         */
        public static final String STOREDCAR = "储值车";
        /**
         * 白名单
         */
        public static final String WHITECAR = "白名单";

        /**
         * 黑名单
         */
        public static final String BLACKLISTCAR = "黑名单";
        /**
         * 月租车
         */
        public static final String MONTHLYCAR = "月租车";
    }

    /**
     * 车位管理 停车状态
     */
    public final class ParkSpace {
        /**
         * 0: 无车辆停靠
         */
        public static final String PARKSTATUS_ZERO = "0";

        /**
         * 1: 有车辆停靠
         */
        public static final String PARKSTATUS_ONE = "1";
    }

    /**
     * 狀態常量
     */
    public final class StatusType {
        /**
         * Y: 开启
         */
        public static final String STATUS_Y = "Y";

        /**
         * N: 关闭
         */
        public static final String STATUS_N = "N";
    }

    /**
     * 是否出场
     */
    public final class IsOutType {
        /**
         * 0: 未出场
         */
        public static final String ISOUT_ONE = "0";

        /**
         * 1: 出场
         */
        public static final String ISOUT_TWO = "1";
    }

    public static class ParkInfoConstant {
        public static final Integer CHARGE_FEE_EDITION = 1;
    }

    public final class AbnormalType {
        /**
         * 补录入场纪录缴费放行
         */
        public static final String ADD_ENTER_HASFEE = "1";
        /**
         * 补录入场记录免费放行
         */
        public static final String ADD_ENTER_NOFEE = "2";
        /**
         * 无入场记录缴费放行
         */
        public static final String NO_ENTER_HASFEE = "3";
        /**
         * 无入场记录免费放行
         */
        public static final String NO_ENTER_NOFEE = "4";
        /**
         * 模糊匹配入场纪录缴费放行
         */
        public static final String LIKE_ENTER_HASFEE = "5";
        /**
         * 模糊匹配入场纪录免费放行
         */
        public static final String LIKE_ENTER_NOFEE = "6";
        /**
         * 二次入场查询到有入场纪录且为未出场
         */
        public static final String TWO_ENTER = "7";
    }

    /**
     * 相机控制板类型
     */
    public final class CameraControl {
        /**
         * 相机 甄实
         */
        public static final String CAMERA_ZHENSHI = "0";
        /**
         * 相机 芊熠
         */
        public static final String CAMERA_QIANYI = "1";
        /**
         * 相机 华夏
         */
        public static final String CAMERA_HUAXIA = "2";

        /**
         * 控制板 方控
         */
        public static final String CONTROLPANEL_FANGKONG = "0";
        /**
         * 控制板 芊熤
         */
        public static final String CONTROLPANEL_QIANYI = "1";
        /**
         * 控制板 菲利德
         */
        public static final String CONTROLPANEL_FEILIDE = "2";
        /**
         * 控制板 启协
         */
        public static final String CONTROLPANEL_QIXIE = "3";
        /**
         * 控制板 小明
         */
        public static final String CONTROLPANEL_XIAOMING = "4";
        /**
         * 控制板 方控小竖屏
         */
        public static final String CONTROLPANEL_SMALL_FANGKONG = "5";
        /**
         * 控制板 YZL(绿卡)
         */
        public static final String CONTROLPANEL_YANGZHONGLAI_GREEN = "6";

        /**
         * 控制板 YZL(蓝卡)
         */
        public static final String CONTROLPANEL_YANGZHONGLAI_BLUE = "7";
        /**
         * 控制板 AK
         */
        public static final String CONTROLPANEL_ANKUAI = "8";
    }
    /*** 过期类型
     */
    public final class OverdueType {
        /**
         * 月租车过期
         */
        public static final String MONTH_OVERDUE = "0";
        /**
         * 白名单过期
         */
        public static final String WHITE_OVERDUE = "1";
    }


    /**
     * 云巅rabbitMq下发月租车状态
     */
    public final class CloudMonthlyStatus {
        /**
         * 新增
         */
        public static final String STATUS_ADD = "add";
        /**
         * 编辑
         */
        public static final String STATUS_EDIT = "edit";
    }
    /**
     * 删除图片类型
     */
    public static final class DeleteType {
        /**
         * 月
         */
        public static final Integer MONTH = 1;
        /**
         * 日
         */
        public static final Integer DAY = 0;
    }

    /**月租车套餐信息*/
    public final class MonthlyPackageConfigInfo{
        /**月租车过期, 按车辆类型入场*/
        public static final String OVERTIME_USE_ENTER_CAR_TYPE_OUT = "0";
    }
    /**入场记录转卡类型*/
    public final static class ParkCarEnterLogTransType{
        /**正常入场*/
        public static final String TRANS_TYPE_NORMAL= "0";
        /**预转卡*/
        public static final String TRANS_TYPE_PRIMARY = "1";
        /**已转卡*/
        public static final String TRANS_TYPE_ALREADY="2";
    }

    /**
     * 内外部车类型定义
     */
    public final class TemporaryCarType {
        /**
         * 内部临时车
         */
        public static final int TEMPORARY_CAR_INNER = -1;
        /**
         * 外部临时车
         */
        public static final int TEMPORARY_CAR_OUTER = -2;

    }
}
