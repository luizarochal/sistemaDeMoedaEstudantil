package sistema.estudantil.system.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @SuppressWarnings("null")
    public void enviarEmailHtml(String para, String assunto, String html) throws MessagingException {
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(html, true);

            mailSender.send(message);
            System.out.println("Email enviado com sucesso para " + para);
            
        } catch (MessagingException e) {
            System.err.println("Erro ao enviar email: " + e.getMessage());
            throw e;
        }
    }
}
