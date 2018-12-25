package com.yzjiang.web.common;

import com.yzjiang.common.constant.ApiCode;

import java.io.Serializable;

/**
 * @Description
 * @Auther yzjiang
 * @Date 2018/10/8 0008 17:30
 */
public class ResultVO implements Serializable {
    private String code;
    private String message;
    private Object data;

    public static ResultVO ok() {
        return ResultVO.of(null);
    }

    public static ResultVO of(Object data) {
        ResultVO vo = new ResultVO();
        vo.setCode(ApiCode.SUCCESS);
        vo.setMessage("");
        vo.setData(data);
        return vo;
    }

    public static ResultVO fail(String code, String message) {
        ResultVO vo = new ResultVO();
        vo.setCode(code);
        vo.setMessage(message);
        return vo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultVO{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
