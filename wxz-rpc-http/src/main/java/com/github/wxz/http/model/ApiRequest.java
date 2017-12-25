package com.github.wxz.http.model;


import com.github.wxz.core.utils.HostUtils;

/**
 * @author xianzhi.wang
 * @date 2017/12/24 -13:51
 */
public class ApiRequest {
    private String clientIp = "";
    private String serverIp = HostUtils.serverIp;

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
