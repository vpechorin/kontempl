package net.pechorina.kontempl.service

import java.util.Map;

interface MailService {
	
	void sendMimeEmail(String from, String to, String subject, String mailBody)
	void sendSimpleEmail(String from, String replyTo, String to, String subject, String msgText)
}
