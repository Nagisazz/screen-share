package com.nagisazz.screenshare.common.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @auther zhushengzhe
 * @date 2022/6/13 15:20
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperationResult {

    private Integer code;

    private String desc;

    private Object data;

    public static OperationResult buildVo(ResultEnum resultEnum, Object data) {
        return OperationResult.builder().code(resultEnum.getCode()).desc(resultEnum.getDesc()).data(data).build();
    }

    public static OperationResult buildVo(ResultEnum resultEnum) {
        return buildVo(resultEnum, null);
    }

    public static OperationResult buildVo(Object data) {
        return buildVo(ResultEnum.SUCCESS, data);
    }

    public static OperationResult buildVo() {
        return buildVo(ResultEnum.SUCCESS, null);
    }
}
