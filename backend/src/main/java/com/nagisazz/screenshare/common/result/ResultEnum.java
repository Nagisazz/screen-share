package com.nagisazz.screenshare.common.result;

/**
 * @auther zhushengzhe
 * @date 2020/2/4 12:26
 */
public enum ResultEnum {

    SUCCESS(200, "成功"),

    FAIL(500, "操作失败"),

    SYSTEM_ERROR(999, "未知错误");

    private Integer code;

    private String desc;

    ResultEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
