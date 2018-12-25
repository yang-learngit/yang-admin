package com.yzjiang.common.constant;

/**
 * @Description
 * @Auther yzjiang
 * @Date 2018/10/8 0008 17:31
 */
public class ApiCode {

    /**
     * 成功
     */
    public static final String SUCCESS = "200";
    /**
     * 客户端错误
     */
    public static final String BAD_REQUEST = "400";
    /**
     * 客户端错误（必填项为空）
     */
    public static final String BAD_REQUEST_EMPTY_PARAMER = "40001";
    /**
     * 验证码不正确
     */
    public static final String BAD_REQUEST_KAPTCHA = "40002";
    /**
     * 未认证
     */
    public static final String UNAUTHORIZED = "401";
    /**
     * 拒绝执行
     */
    public static final String FORBIDDEN = "403";
    /**
     * Not Found
     */
    public static final String NOT_FOUND = "404";
    /**
     * 冲突验证
     */
    public static final String CONFLICT = "409";
    /**
     * 服务器错误
     */
    public static final String INTERNAL_SERVER_ERROR = "500";

}
