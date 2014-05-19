package uk.co.postoffice.spike.esi.helloworld;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.spring3.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@Configuration
@ComponentScan
public class HelloWorldConfiguration extends WebMvcConfigurationSupport {

    @Override
    protected void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        final TemplateResolver urlTemplateResolver = new org.thymeleaf.templateresolver.UrlTemplateResolver() {{
            setOrder(10);
            setPrefix("http://localhost:1234/master/sampleweb/");
            setSuffix(".html");
            setTemplateMode("HTML5");
            setCharacterEncoding("UTF-8");
            setCacheable(false);
        }};

        final TemplateResolver servletContextTemplateResolver = new org.thymeleaf.templateresolver.ServletContextTemplateResolver() {{
            setOrder(20);
            setPrefix("/WEB-INF/templates/");
            setSuffix(".html");
            setTemplateMode("HTML5");
            setCharacterEncoding("UTF-8");
            setCacheable(false);
        }};

        final SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine() {{
            addTemplateResolver(servletContextTemplateResolver);
            addTemplateResolver(urlTemplateResolver);
            addDialect(new SpringSecurityDialect());
        }};

        final ThymeleafMasterLayoutViewResolver viewResolver = new ThymeleafMasterLayoutViewResolver() {{
            setCharacterEncoding("UTF-8");
            setTemplateEngine(springTemplateEngine);
            setCache(false);
//            setFullPageLayout("layout/fullPageLayout");
        }};

        return viewResolver;
    }
}
