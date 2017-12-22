package com.github.wxz.rpc;

import com.github.wxz.rpc.service.AddCalculate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author xianzhi.wang
 * @date 2017/12/20 -16:16
 */
public class Call {
    private static final Logger LOGGER = LoggerFactory.getLogger(Call.class);

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:rpc-invoke-config-client.xml");
        AddCalculate addCalculate = (AddCalculate) context.getBean("addCalculate");
        int result = addCalculate.add(5755757, 1000000);
        LOGGER.info("result {}", result);
        context.destroy();
    }

}
