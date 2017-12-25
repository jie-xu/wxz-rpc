package com.github.wxz;

import com.github.wxz.http.resolver.HttpBoot;
import com.github.wxz.rpc.parallel.ExecutorManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 启动类
 * @author xianzhi.wang
 * @date 2017/12/19 -15:57
 */
public class Boot {
    public static void main(String[] args) {

        //启动http服务
        ExecutorManager.execute(new HttpBoot());

        //启动rpc
        new ClassPathXmlApplicationContext(
                "classpath:rpc-invoke-config-server.xml"
        );
    }
}