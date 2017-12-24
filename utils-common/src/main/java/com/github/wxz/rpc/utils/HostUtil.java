package com.github.wxz.rpc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * @author xianzhi.wang
 * @date 2017/12/24 -13:53
 */
public class HostUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostUtil.class);
    public static String serverIp = "";

    static {
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            List<String> ipList = new ArrayList<>();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> ia = ni.getInetAddresses();
                while (ia.hasMoreElements()) {
                    InetAddress inetAddress = ia.nextElement();
                    String tempIp = inetAddress.getHostAddress();
                    if (tempIp != null && tempIp.startsWith("1") && !tempIp.startsWith("127")) {
                        ipList.add(tempIp);
                    }
                }
            }
            //10>172>192
            Collections.sort(ipList);
            LOGGER.info("Server ip:{}", ipList);
            if (ipList.size() > 0) {
                serverIp = ipList.get(0);
            }
        } catch (Exception e) {
            LOGGER.error("解析服务器ip异常", e);
        }
    }

}
