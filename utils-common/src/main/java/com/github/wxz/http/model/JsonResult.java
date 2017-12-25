package com.github.wxz.http.model;

/**
 * @author xianzhi.wang
 * @date 2017/12/24 -15:29
 */
public class JsonResult {

    private int code;
    private String message;

    public static JsonResult getInstance() {
        return getInstance(0, "");
    }

    public static JsonResult getInstance(int code, String message) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(code);
        jsonResult.setMessage(message);
        return jsonResult;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
