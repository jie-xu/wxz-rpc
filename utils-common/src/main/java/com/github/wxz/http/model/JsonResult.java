package com.github.wxz.http.model;

/**
 * @author xianzhi.wang
 * @date 2017/12/24 -15:29
 */
public class JsonResult {

    private int code;
    private  transient ResultType resultType = ResultType.HTML;
    private String message;

    public static JsonResult jsonInstance() {
        return getInstance(0, ResultType.JSON, "");
    }

    public static JsonResult htmlInstance() {
        return getInstance(0, ResultType.HTML, "");
    }

    public static JsonResult successHtmlInstance(String message) {
        return getInstance(0, ResultType.HTML, message);
    }

    public static JsonResult successJsonInstance(String message) {
        return getInstance(0, ResultType.JSON, message);
    }

    public static JsonResult failJsonInstance(String message) {
        return getInstance(-1, ResultType.JSON, message);
    }
    public static JsonResult failHtmlInstance(String message) {
        return getInstance(-1, ResultType.HTML, message);
    }

    public static JsonResult getInstance(int code, ResultType resultType, String message) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setResultType(resultType);
        jsonResult.setCode(code);
        jsonResult.setMessage(message);
        return jsonResult;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
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
