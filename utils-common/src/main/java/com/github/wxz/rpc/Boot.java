package com.github.wxz.rpc;

import com.github.wxz.rpc.netty.resolver.HttpBoot;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -15:57
 */
public class Boot {
    public static void main(String[] args) {

        //启动http服务
        HttpBoot.httpStart();

        //启动rpc
        new ClassPathXmlApplicationContext(
                "classpath:rpc-invoke-config-server.xml"
        );
    }
}
