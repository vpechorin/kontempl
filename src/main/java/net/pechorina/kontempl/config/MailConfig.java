package net.pechorina.kontempl.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
	private static Logger logger = LoggerFactory.getLogger(MailConfig.class);
	
	@Autowired
	private Environment env;
	
	@Bean(name = "mailSender")
	public JavaMailSender mailSender() {
		logger.debug("Creating instance of mail sender");
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(env.getProperty("smtpHost"));
		sender.setPort(env.getProperty("smtpPort", Integer.class));
		sender.setDefaultEncoding("UTF-8");
		if (env.getProperty("smtpAuth", Boolean.class)) {
			sender.setUsername(env.getProperty("smtpAuthUsername"));
			sender.setPassword(env.getProperty("smtpAuthPassword"));
		}
		Properties props = new Properties();
		props.setProperty("mail.smtp.starttls.enable",
				env.getProperty("smtpStartTlsEnable"));
		props.setProperty("mail.transport.protocol",
				env.getProperty("mailTransportProtocol"));
		props.setProperty("mail.smtp.auth", env.getProperty("smtpAuth"));
		props.setProperty("mail.debug", env.getProperty("mailDebug"));
		props.setProperty("mail.smtp.localhost",
				env.getProperty("smtpLocalhost"));
		sender.setJavaMailProperties(props);

		return sender;
	}

}
