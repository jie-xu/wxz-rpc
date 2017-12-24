package com.github.wxz.rpc.spring;

import com.google.common.io.CharStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * XML Schema 文件
 *
 * @author xianzhi.wang
 * @date 2017/12/19 -15:45
 */
public class RpcNamespaceHandler extends NamespaceHandlerSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcNamespaceHandler.class);

    static {
        Resource resource = new ClassPathResource("rpc-logo.txt");
        try {
            Reader reader = new InputStreamReader(resource.getInputStream(), "UTF-8");
            String text = CharStreams.toString(reader);
            LOGGER.info(text);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("Environment: jdk.version    => {}", System.getProperty("java.version"));
        LOGGER.info("Environment: user.dir       => {}", System.getProperty("user.dir"));
        LOGGER.info("Environment: java.io.tmpdir => {}", System.getProperty("java.io.tmpdir"));
        LOGGER.info("Environment: user.timezone  => {}", System.getProperty("user.timezone"));
        LOGGER.info("Environment: file.encoding  => {}", System.getProperty("file.encoding"));
    }

    @Override
    public void init() {
        registerBeanDefinitionParser("service", new RpcServiceParser());
        registerBeanDefinitionParser("registry", new RpcRegistryParser());
        registerBeanDefinitionParser("reference", new RpcReferenceParser());
    }
}
