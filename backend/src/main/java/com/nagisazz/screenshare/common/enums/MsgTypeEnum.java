package com.nagisazz.screenshare.common.enums;

public enum MsgTypeEnum {

    /**
     * 全部发送
     */
    ALL("00"),
    /**
     * 上线
     */
    ONLINE("01"),
    /**
     * 下线
     */
    OFFLINE("02"),
    /**
     * 指定发送
     */
    SINGLE("03");

    private String value;

    MsgTypeEnum(String type) {
        value = type;
    }

    public String getValue() {
        return value;
    }

}
