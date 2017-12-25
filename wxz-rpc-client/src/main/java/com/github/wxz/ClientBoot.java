package com.github.wxz;

import com.github.wxz.api.AddCalculate;
import com.github.wxz.core.rpc.parallel.ExecutorManager;
import com.github.wxz.http.HttpBoot;
import com.github.wxz.http.HttpJmxBoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author xianzhi.wang
 * @date 2017/12/20 -16:16
 */
public class ClientBoot {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientBoot.class);

    public static void main(String[] args) {
        //启动http服务
        ExecutorManager.execute(new HttpBoot());
        ExecutorManager.execute(new HttpJmxBoot());
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");
        AddCalculate addCalculate = (AddCalculate) context.getBean("addCalculate");
        int result = addCalculate.add(5755757, 1000000);
        LOGGER.info("result {}", result);
        context.destroy();
    }

}
