package com;

/*
 * Constant project data here
 * */

public class Constants {
    // shared preference
    public static final String DEVOPSCONFIG = "DEVOPSCONFIG";

    public static final String BDSDeviceConfig = "BDSDeviceConfig";

    public static final String ComntWay = "ComntWay";

    // serial port address
    public static final String SERIAL_PORT_ADDR = "/dev/ttysWK3";

//    public static final String SERIAL_PORT_ADDR = "/dev/ttyS0";

    public static final int SERIAL_PORT_RATE = 115200;

    // Radio wifi
//    public static final String DT_HOST = "192.168.124.5";

    public static final String DT_HOST = "192.168.1.123";

    public static final int DT_PORT = 10161;

    // System signals

//    public static final String TARGET_CARD_NUM = "0333132";

    public static final String TARGET_CARD_NUM = "0229573";

    /* 上下位机协议 */

    // 允许
    public static final String SIGNAL_PERMIT = "FF";

    // 不允许
    public static final String SIGNAL_REJECT = "00";

    // 上报控制
    public static final String SIGNAL_UPPER_CONTROL = "6E3208%s%s%s%s";

    // 参数配置
    public static final String SIGNAL_CONFIG_PARAMS = "6E3007%s%s%s";

    // 系统自检
    public static final String SIGNAL_SELF_CHECK = "6E330800000000";

    // 频闪控制
    public static final String SIGNAL_STROBE_CONTROL = "6E3109%s%s%s%s%s";

    // 系统自检结果
    public static final String SIGNAL_SELF_CHECK_RGEX = "6E4108(.{2})0000(.{2}).*";

    //系统休眠
    public static final String SIGNAL_BD_SYS_SLEEP = "6E3407%s0000";

    // 上报数据（下位机发送到上位机）
    public static final String SIGNAL_UPPER_DATA = "6E401A(.{2})(.{2})(.{2})(.{2})(.{2}).{6}(.{2})(.{2})(.{2})(.{2})(.{2})(.{2})(.{2})(.{2})(.{2})(.{2})(.{2})(.{2})(.{2})(.{2}).*";
    /* 北斗协议 */

    // 发送读取卡号
    public static final String SIGNAL_READ_CARD = "$CCICA,0,00*";

    // 接收卡号读取结果
    public static String SIGNAL_READ_CARD_RGEX = "\\$BDICI,(.*?),.*";

    // 发送读取信号强度
    public static final String SIGNAL_READ_STRENGTH = "$CCRMO,BSI,2,2*";

    // 接收读取信号强度
    public static final String SIGNAL_READ_STRENGTH_RGEX = "\\$BDBSI,(.*?),(.*?),(.{19})\\*.*";

    // 发送短报文
    public static final String SIGNAL_SHORT_MSG_S = "$CCTXA,%s,1,2,A4%s*";

    // 接收短报文
    public static final String SIGNAL_SHORT_MSG_R = "\\$BDTXR,(.*?),(.*?),(.*?),(.*?),(.*?)\\*.*";

    // 北斗发送结果
    public static final String SIGNAL_BD_SEND_RES = "\\BDFKI,(.*?),(.*?),(.*?),(.*?),(.*?)\\*";
}
