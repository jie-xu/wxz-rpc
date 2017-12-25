package com.github.wxz;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 启动类
 *
 * @author xianzhi.wang
 * @date 2017/12/19 -15:57
 */
public class ProviderBoot {
    public static void main(String[] args) {
        //启动rpc
        new ClassPathXmlApplicationContext(
                "classpath:rpc-invoke-config-server.xml"
        );
    }
}
