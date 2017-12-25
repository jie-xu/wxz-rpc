package com.github.wxz.http.ui.template;

import com.github.wxz.core.exception.TemplateException;
import com.github.wxz.http.ui.ModelAndView;
import jetbrick.template.JetContext;
import jetbrick.template.JetEngine;
import jetbrick.template.JetGlobalContext;
import jetbrick.template.JetTemplate;
import jetbrick.template.resolver.GlobalResolver;

import java.io.Writer;
import java.util.Map;
import java.util.Properties;

/**
 * @author xianzhi.wang
 * @date 2017/12/24 -17:37
 */
public class JetBrickTemplateEngine implements TemplateEngine {
    private JetEngine jetEngine;
    private Properties config = new Properties();
    private String suffix = ".html";

    public JetBrickTemplateEngine() {
        this.config.put("jetx.template.suffix", this.suffix);
        String classpathLoader = "jetbrick.template.loader.ClasspathResourceLoader";
        this.config.put("jetx.template.loaders", "$classpathLoader");
        this.config.put("$classpathLoader", classpathLoader);
        this.config.put("$classpathLoader.root", "/tpl/");
        this.config.put("$classpathLoader.reloadable", "true");
    }

    public JetBrickTemplateEngine(Properties config) {
        this.config = config;
        this.jetEngine = JetEngine.create(config);
    }

    public JetBrickTemplateEngine(String conf) {
        this.jetEngine = JetEngine.create(conf);
    }

    public JetBrickTemplateEngine(JetEngine jetEngine) {
        if (null == jetEngine) {
            throw new IllegalArgumentException("jetEngine must not be null");
        } else {
            this.jetEngine = jetEngine;
        }
    }

    @Override
    public void render(ModelAndView modelAndView, Writer writer) throws TemplateException {
        if (null == this.jetEngine) {
            this.jetEngine = JetEngine.create(this.config);
        }
        Map<String, Object> modelMap = modelAndView.getModel();
        JetContext context = new JetContext(modelMap.size());
        context.putAll(modelMap);
        String templateName = modelAndView.getView().endsWith(this.suffix) ? modelAndView.getView() : modelAndView.getView() + this.suffix;

        try {
            JetTemplate template = this.jetEngine.getTemplate(templateName);
            template.render(context, writer);
        } catch (Exception var9) {
            throw new TemplateException(var9);
        }
    }

    public JetGlobalContext getGlobalContext() {
        if (null == this.jetEngine) {
            this.jetEngine = JetEngine.create(this.config);
        }

        return this.jetEngine.getGlobalContext();
    }

    public GlobalResolver getGlobalResolver() {
        if (null == this.jetEngine) {
            this.jetEngine = JetEngine.create(this.config);
        }

        return this.jetEngine.getGlobalResolver();
    }

    public TemplateEngine addConfig(String key, String value) {
        this.config.put(key, value);
        return this;
    }

    public JetEngine getJetEngine() {
        return this.jetEngine;
    }

    public void setJetEngine(JetEngine jetEngine) {
        this.jetEngine = jetEngine;
    }

    public Properties getConfig() {
        return this.config;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
