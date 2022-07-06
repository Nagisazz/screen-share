package com.nagisazz.screenshare.common.exception;

import com.nagisazz.screenshare.common.result.ResultEnum;

import lombok.Getter;
import lombok.ToString;

/**
 * @author zhushengzhe
 * @date 2022/6/13 15:46
 */
@Getter
@ToString
public class BaseException extends RuntimeException {
    /**
     * 是否成功
     */
    private final boolean success = false;
    /**
     * 状态码
     */
    private final Integer code;
    /**
     * 异常信息
     */
    private final String message;

    public BaseException(final ResultEnum status) {
        super(status.getDesc());
        this.code = status.getCode();
        this.message = status.getDesc();
    }

    public BaseException(final Integer code, final String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(final Integer code, final String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    /**
     * 构建
     *
     * @param status 状态枚举
     */
    public static BaseException create(final ResultEnum status) {
        return new BaseException(status);
    }

    /**
     * 构建
     *
     * @param code    {@linkplain ResultEnum#getCode()}
     * @param message {@linkplain ResultEnum#getDesc()} ()}
     * @return BaseException
     */
    public static BaseException create(final Integer code, final String message) {
        return new BaseException(code, message);
    }
}

