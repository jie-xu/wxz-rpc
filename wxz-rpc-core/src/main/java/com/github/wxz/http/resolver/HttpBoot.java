package com.github.wxz.http.resolver;

import com.github.wxz.http.api.Api;
import com.github.wxz.http.api.ApiController;
import com.github.wxz.http.api.ApiManager;
import com.github.wxz.config.HttpServerConfig;
import com.github.wxz.rpc.parallel.ExecutorManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author xianzhi.wang
 * @date 2017/12/24 -14:37
 */
@Configuration
@ComponentScan("com.github.wxz.*")
public class HttpBoot implements Runnable {
    @Override
    public void run() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(HttpBoot.class);
        HttpServerConfig httpServerConfig = new HttpServerConfig();
        httpServerConfig.setIsEnableSsl(false);
        httpServerConfig.setPort(80);
        httpServerConfig.setIsResponseGzip(false);
        //开启新的线程,apiEcho
        ExecutorManager.execute(new ApiEchoResolver(httpServerConfig, annotationConfigApplicationContext.getBean(HttpCallback.class)));

        //注册服务
        Map<String, Api> map = annotationConfigApplicationContext.getBeansOfType(Api.class);
        for (Api api : map.values()) {
            if (api.getClass().isAnnotationPresent(ApiController.class)) {
                ApiController businessController = api.getClass().getAnnotation(ApiController.class);
                ApiManager.register(businessController.value(), api);
            }
        }
    }
}
