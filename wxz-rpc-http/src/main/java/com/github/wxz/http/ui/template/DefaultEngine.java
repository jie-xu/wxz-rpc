package com.github.wxz.http.ui.template;

import com.github.wxz.core.config.RpcSystemConfig;
import com.github.wxz.core.exception.TemplateException;
import com.github.wxz.core.utils.IoUtils;
import com.github.wxz.http.ui.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * default template implements
 *
 * @author xianzhi.wang
 * @date 2017/12/24 -17:02
 */
public class DefaultEngine implements TemplateEngine {


    @Override
    public void render(ModelAndView modelAndView, Writer writer) throws TemplateException {
        String view = modelAndView.getView();
        String viewPath = RpcSystemConfig.CLASS_PATH +
                File.separator +
                RpcSystemConfig.TEMPLATE_PATH +
                File.separator + view;
        viewPath = viewPath.replace("//", "/");
        try {
            String body = IoUtils.readToString(viewPath);
            Map<String, Object> attributes = new HashMap<>();
            attributes.putAll(modelAndView.getModel());

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IoUtils.closeQuietly(writer);
        }

    }

}
