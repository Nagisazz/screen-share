package com.nagisazz.screenshare.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nagisazz.screenshare.common.exception.BaseException;
import com.nagisazz.screenshare.common.result.ResultEnum;

/**
 * @author zhushengzhe
 * @date 2022/6/13 15:46
 */
public class JWTUtil {

    private static final String SIGNATURE = "nagisazlp";

    private static final int EXPIRE_DATE = 60;

    /**
     * 生成token
     *
     * @param map   传入payload
     * @param keyId 用户唯一标识
     * @return 返回token
     */
    public static String getToken(Map<String, String> map, String keyId) {
        JWTCreator.Builder builder = JWT.create().withKeyId(keyId);
        map.forEach(builder::withClaim);
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, EXPIRE_DATE);
        builder.withExpiresAt(instance.getTime());
        return builder.sign(Algorithm.HMAC256(SIGNATURE));
    }

    /**
     * 验证token
     *
     * @param token 前端传输的token
     */
    public static void verify(String token) {
        JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token);
    }

    /**
     * 获取token数据
     *
     * @param token 前端传输的token
     * @return token数据
     */
    public static DecodedJWT decode(String token) {
        return JWT.require(Algorithm.HMAC256(SIGNATURE)).build().verify(token);
    }

    /**
     * 解密token
     *
     * @param token jwt token
     * @return Claims中数据需均为 {@link String} 类型
     */
    public static Map<String, String> getMap(String token) {
        if (StringUtils.isBlank(token)) {
            throw new BaseException(ResultEnum.FAIL.getCode(), "无token，请重新登录");
        }
        try {
            Map<String, String> map = new HashMap<>(32);
            //校验token
            JWTUtil.verify(token);
            //解密
            DecodedJWT decode = JWT.decode(token);
            String userId = decode.getKeyId();
            map.put("userId", userId);
            final Map<String, Claim> claims = decode.getClaims();
            claims.forEach((k, v) -> map.put(k, v.asString()));
            return map;
        } catch (JWTDecodeException e) {
            throw new BaseException(ResultEnum.FAIL.getCode(), "token解密失败", e);
        } catch (JWTVerificationException e) {
            throw new BaseException(ResultEnum.FAIL.getCode(), "token校验失败", e);
        }
    }

    /**
     * 解密token中数据，根据传入类型
     *
     * @param object {@link Claim} 转化类型
     * @param token  jwt token
     * @param <T>    转化类型
     * @return token中数据
     */
    public static <T> Map<String, T> getMap(Class<T> object, String token) {
        if (StringUtils.isBlank(token)) {
            throw new BaseException(ResultEnum.FAIL.getCode(), "无token，请重新登录");
        }
        try {
            Map<String, T> map = new HashMap<>(32);
            //校验token
            JWTUtil.verify(token);
            //解密
            DecodedJWT decode = JWT.decode(token);
            final Map<String, Claim> claims = decode.getClaims();
            claims.forEach((k, v) -> map.put(k, v.as(object)));
            return map;
        } catch (JWTDecodeException e) {
            throw new BaseException(ResultEnum.FAIL.getCode(), "token解密失败", e);
        } catch (JWTVerificationException e) {
            throw new BaseException(ResultEnum.FAIL.getCode(), "token校验失败", e);
        }
    }


}
