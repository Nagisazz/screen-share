package com.nagisazz.screenshare.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nagisazz.screenshare.common.exception.BaseException;
import com.nagisazz.screenshare.common.result.ResultEnum;
import com.nagisazz.screenshare.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhushengzhe
 * @date 2022-06-14 11:39
 * 获取token并验证token
 **/
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                             Object object) {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        log.info("接口调用：{}，参数列表：{}", httpServletRequest.getRequestURL()
                , JSON.toJSONString(httpServletRequest.getParameterMap()));

        // 验证token
        // 从http请求头中取出 token
        String token = httpServletRequest.getHeader("authorization");
        // 执行认证
        if (token == null) {
            throw new BaseException(ResultEnum.FAIL.getCode(), "无token，请重新登录");
        }

        try {
            //校验token
            JWTUtil.verify(token);
            //解密
            DecodedJWT decode = JWT.decode(token);
            Long userId = Long.parseLong(decode.getKeyId());

            log.info("接口调用：{}，参数列表：{}，调用userId：{}", httpServletRequest.getRequestURI()
                    , JSON.toJSONString(httpServletRequest.getParameterMap()), userId);

            // 添加request参数，用于传递foreignKey
            httpServletRequest.setAttribute("userId", userId);
            String sessionKey = decode.getClaim("session_key").as(String.class);
            // 添加request参数，用于传递参数
            httpServletRequest.setAttribute("sessionKey", sessionKey);

        } catch (JWTDecodeException e) {
            throw new BaseException(ResultEnum.FAIL.getCode(), "token解密失败", e);
        } catch (Exception e) {
            throw new BaseException(ResultEnum.FAIL.getCode(), "token校验失败", e);
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object handler, ModelAndView modelAndView) {
        //do nothing
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object handler, Exception e) {
        //do nothing
    }
}
