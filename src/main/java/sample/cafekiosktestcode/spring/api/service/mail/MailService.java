package sample.cafekiosktestcode.spring.api.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosktestcode.spring.clinet.mail.MailSendClient;
import sample.cafekiosktestcode.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosktestcode.spring.domain.history.mail.MailSendHistoryRepository;

@RequiredArgsConstructor
@Service
public class MailService {

	private final MailSendClient mailSendClient;

	private final MailSendHistoryRepository mailSendHistoryRepository;

	public boolean sendMail(String fromEmail, String toEmail, String subject, String content) {

		boolean result = mailSendClient.sendEmail(fromEmail, toEmail, subject, content);

		if(result) {
			mailSendHistoryRepository.save(MailSendHistory.builder()
				.fromEmail(fromEmail)
				.toEmail(toEmail)
				.subject(subject)
				.content(content)
				.build()
			);

			mailSendClient.a();
			mailSendClient.b();
			mailSendClient.c();

			return true;
		}

		return false;
	}
}
