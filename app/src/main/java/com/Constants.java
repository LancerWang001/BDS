package com;

/*
 * Constant project data here
 * */

public class Constants {
    // serial port address
    public static final String SERIAL_PORT_ADDR = "/dev/ttysWK3";

    public static final int SERIAL_PORT_RATE = 115200;

    // System signals
    public static final String SIGNAL_PERMIT = "FF";

    public static final String SIGNAL_REJECT = "00";

    public static final String SIGNAL_UPPER_CONTROL = "7EH32H08H%sH%sH%sH%sH";

    public static final String SIGNAL_CONFIG_PARAMS = "7EH30H07H%#o%#%#o%#o";

    public static final String SIGNAL_SELF_CHECK = "7EH33H08H00H00H00H00H%#o";

    public static final String SIGNAL_STROBE_CONTROL = "7EH31H08H%#o%#o%#o%#o%#o";

    public static final String SIGNAL_UPPER_SELF_CHECK_RES = "7EH41H08H00H00H00H";

    public static final String SIGNAL_UPPER_DATA = "7EH40H";
}

