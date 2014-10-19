package net.pechorina.kontempl.service

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class MailServiceImpl implements MailService {
	@Autowired
	Environment env

	@Autowired
	JavaMailSender mailSender
	
	@Async
	void sendMimeEmail(final String from, final String to, final String subject, final String mailBody) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
					public void prepare(MimeMessage mimeMessage) throws Exception {
						MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
						message.setTo(to);
						message.setFrom(from);
						message.setSubject(subject);
						message.setText(mailBody, false);
					}
				};
		this.mailSender.send(preparator)
	}
	
	@Async
	void sendSimpleEmail(String from, String replyTo, String to, String subject, String msgText) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setFrom(from);
		message.setReplyTo(replyTo);
		message.setSubject(subject);

		message.setText(msgText);
		this.mailSender.send(message);
	}
}
