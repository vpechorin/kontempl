package net.pechorina.kontempl.config;

import java.util.Properties;

import net.pechorina.kontempl.view.CommonDataInterceptor;
import net.pechorina.kontempl.view.LoggingInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="net.pechorina.kontempl")
public class WebConfig extends WebMvcConfigurerAdapter {
	
	@Autowired
	private Environment env;
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoggingInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(new CommonDataInterceptor()).addPathPatterns("/**");
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
}
