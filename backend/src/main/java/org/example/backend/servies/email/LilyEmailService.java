package org.example.backend.servies.email;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LilyEmailService {

	private final JavaMailSender mailSender;

	@Async("emailTaskExecutor")
	public void sendEmail(String to, String subject, String email) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, "utf-8");
			messageHelper.setText(email, true);
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			messageHelper.setFrom("lily@backend.com");
			mailSender.send(message);

		} catch (Exception e) {
			// TODO: Add better Logger for errors
			e.printStackTrace();
		}
	}
}
