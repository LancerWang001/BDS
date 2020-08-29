package com;

/*
 * Constant project data here
 * */

public class Constants {
    // serial port address
    public static final String SERIAL_PORT_ADDR = "/dev/ttysWK3";

    public static final int SERIAL_PORT_RATE = 115200;

    // Radio wifi
    public static final String DT_HOST = "192.168.1.123";

    public static final int DT_PORT = 10161;

    // System signals

//    public static final String TARGET_CARD_NUM = "0333132";

    public static final String TARGET_CARD_NUM = "0332953";

    /* 上下位机协议 */

    // 允许
    public static final String SIGNAL_PERMIT = "FF";

    // 不允许
    public static final String SIGNAL_REJECT = "00";

    // 上报控制
    public static final String SIGNAL_UPPER_CONTROL = "7E3208%s%s%s%s";

    // 参数配置
    public static final String SIGNAL_CONFIG_PARAMS = "7E3007%s%s%s";

    // 系统自检
    public static final String SIGNAL_SELF_CHECK = "7E330800000000";

    // 频闪控制
    public static final String SIGNAL_STROBE_CONTROL = "7E3108%s%s%s%s";

    // 系统自检结果
    public static final String SIGNAL_SELF_CHECK_RGEX = "7E4108000000(.{2}).*";

    // 上报数据（下位机发送到上位机）
    public static final String SIGNAL_UPPER_DATA = "7E4019.{14}(.{2})(.{2})(.{2})(.{2})(.{2})(.{2})([S|N])(.{2})(.{2})(.{2})(.{2})(.{2})(.{2})([W|E]).*";

    /* 北斗协议 */

    // 发送读取卡号
    public static final String SIGNAL_READ_CARD = "$CCICA,0,00*";

    // 接收卡号读取结果
    public static String SIGNAL_READ_CARD_RGEX = "\\$BDICI,(.*?),.*";

    // 发送短报文
    public static final String SIGNAL_SHORT_MSG_S = "$CCTXA,%s,1,2,A4%s*";

    // 接收短报文
    public static final String SIGNAL_SHORT_MSG_R = "\\$BDTXR,(.*?),(.*?),(.*?),(.*?),(.*?)\\*.*";

    // 北斗发送结果
    public static final String SIGNAL_BD_SEND_RES = "\\BDFKI,(.*?),(.*?),(.*?),(.*?),(.*?)\\*";
}
