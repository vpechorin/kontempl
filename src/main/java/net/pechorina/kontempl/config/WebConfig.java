package net.pechorina.kontempl.config;

import net.pechorina.kontempl.filters.HeaderInterceptor;
import net.pechorina.kontempl.filters.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    @Qualifier("commonDataInterceptor")
    private WebRequestInterceptor commonDataInterceptor;

    @Autowired
    @Qualifier("loggingInterceptor")
    private LoggingInterceptor loggingInterceptor;

    @Autowired
    @Qualifier("headerInterceptor")
    private HeaderInterceptor headerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor).addPathPatterns("/**");
        registry.addInterceptor(headerInterceptor).addPathPatterns("/view/**");
        registry.addWebRequestInterceptor(commonDataInterceptor).addPathPatterns("/view/**");
    }

    @Override
    public void addArgumentResolvers(
            List<HandlerMethodArgumentResolver> argumentResolvers) {

        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(new PageRequest(1, 20));
        resolver.setMaxPageSize(Integer.MAX_VALUE);

        argumentResolvers.add(resolver);
    }

}
