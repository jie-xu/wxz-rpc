package com.github.wxz.rpc.model;

import com.github.wxz.rpc.utils.HostUtil;

/**
 * @author xianzhi.wang
 * @date 2017/12/24 -13:51
 */
public class ApiRequest {
    private String clientIp = "";
    private String serverIp = HostUtil.serverIp;

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
