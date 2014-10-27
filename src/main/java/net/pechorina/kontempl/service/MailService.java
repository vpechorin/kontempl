package net.pechorina.kontempl.service;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	static final Logger logger = LoggerFactory.getLogger(MailService.class);
			
	@SuppressWarnings("unused")
	@Inject
	private Environment env;

	@Inject
	private JavaMailSender mailSender;
	
	@Async
	public void sendMimeEmail(final String from, final String to, final String subject, final String mailBody) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
					public void prepare(MimeMessage mimeMessage) throws Exception {
						MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
						message.setTo(to);
						message.setFrom(from);
						message.setSubject(subject);
						message.setText(mailBody, false);
					}
				};
		this.mailSender.send(preparator);
	}
	
	@Async
	public void sendSimpleEmail(String from, String replyTo, String to, String subject, String msgText) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setFrom(from);
		message.setReplyTo(replyTo);
		message.setSubject(subject);

		message.setText(msgText);
		this.mailSender.send(message);
	}
}
