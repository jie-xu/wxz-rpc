package com.github.wxz.http.ui.template;

import com.github.wxz.http.ui.ModelAndView;
import com.github.wxz.exception.TemplateException;

import java.io.Writer;

/**
 * TemplateEngine Interface, For view layer to display data
 *
 * @author xianzhi.wang
 * @date 2017/12/24 -16:57
 */
public interface TemplateEngine {
    /**
     * Render a template file to the client
     *
     * @param modelAndView
     * @param writer
     * @throws TemplateException
     */
    void render(ModelAndView modelAndView, Writer writer) throws TemplateException;
}
