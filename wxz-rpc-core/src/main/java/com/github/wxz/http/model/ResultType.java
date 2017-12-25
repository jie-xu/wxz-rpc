package com.github.wxz.http.model;

/**
 * 结果类型
 *
 * @author xianzhi.wang
 * @date 2017/12/25 -9:59
 */
public enum ResultType {
    HTML(0), JSON(1);
    private int type;

    ResultType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
