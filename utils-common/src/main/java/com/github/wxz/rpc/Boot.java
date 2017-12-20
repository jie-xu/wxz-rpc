package com.github.wxz.rpc;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author xianzhi.wang
 * @date 2017/12/19 -15:57
 */
public class Boot {
    public static void main(String[] args) {

        new ClassPathXmlApplicationContext(
                "classpath:rpc-invoke-config-server.xml"
        );
    }
}
