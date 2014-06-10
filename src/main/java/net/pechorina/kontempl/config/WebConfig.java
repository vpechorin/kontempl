package net.pechorina.kontempl.config;

import java.util.List;
import java.util.Properties;

import javax.xml.transform.Source;

import net.pechorina.kontempl.filters.HeaderInterceptor;
import net.pechorina.kontempl.filters.LoggingInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="net.pechorina.kontempl")
public class WebConfig extends WebMvcConfigurerAdapter {
	
	@Autowired
	private Environment env;
	
	@Autowired
	@Qualifier("commonDataInterceptor")
	private WebRequestInterceptor commonDataInterceptor;
	
	@Autowired
	@Qualifier("loggingInterceptor")
	private  LoggingInterceptor loggingInterceptor;
	
	@Autowired
	@Qualifier("headerInterceptor")
	private  HeaderInterceptor headerInterceptor;
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loggingInterceptor).addPathPatterns("/**");
		registry.addInterceptor(headerInterceptor).addPathPatterns("/view/**");
		registry.addWebRequestInterceptor(commonDataInterceptor).addPathPatterns("/view/**");
	}
	
	@Bean(name="freemarkerCommonConfiguration")
	public FreeMarkerConfigurationFactoryBean freemarkerConfiguration() {
		FreeMarkerConfigurationFactoryBean factory = new FreeMarkerConfigurationFactoryBean();
		factory.setTemplateLoaderPaths(env.getProperty("spring.freemarker.templateLoaderPath"));
		Properties settings = new Properties();
		settings.setProperty("output_encoding", env.getProperty("spring.freemarker.templateEncoding"));
		factory.setFreemarkerSettings(settings);
		return factory;
	}
	
	@Bean(name="freemarkerConfig")
	public FreeMarkerConfigurer freeMarkerConfigurer() {
	    FreeMarkerConfigurer fmc = new FreeMarkerConfigurer();
	    fmc.setTemplateLoaderPath("classpath:/templates/");
	    return fmc;
	}
	
	@Override
	public void addArgumentResolvers(
			List<HandlerMethodArgumentResolver> argumentResolvers) {

		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		resolver.setFallbackPageable(new PageRequest(1, 10));

		argumentResolvers.add(resolver);
	}
	
	@Bean
	public Module jodaModule() {
	  return new JodaModule();
	}
	
	@Bean
    public ObjectMapper objectMapper() {
        Jackson2ObjectMapperFactoryBean bean = new Jackson2ObjectMapperFactoryBean();
        bean.afterPropertiesSet();
        ObjectMapper objectMapper = bean.getObject();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new JodaModule()); // <--- inject Joda de/ serializers
        return objectMapper;
    }
    
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }
    
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        stringConverter.setWriteAcceptCharset(false);
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(stringConverter);
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new SourceHttpMessageConverter<Source>());
        converters.add(new AllEncompassingFormHttpMessageConverter());
        converters.add(mappingJackson2HttpMessageConverter());
    }
}
