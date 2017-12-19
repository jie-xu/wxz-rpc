package com.github.wxz.rpc.spring;

import com.google.common.io.CharStreams;
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
public class NettyRpcNamespaceHandler extends NamespaceHandlerSupport {
    static {
        Resource resource = new ClassPathResource("rpc-logo.txt");
        try {
            Reader reader = new InputStreamReader(resource.getInputStream(), "UTF-8");
            String text = CharStreams.toString(reader);
            System.out.println(text);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        registerBeanDefinitionParser("service", new RpcServiceParser());
        registerBeanDefinitionParser("registry", new RpcRegisteryParser());
        registerBeanDefinitionParser("reference", new RpcReferenceParser());
    }
}
