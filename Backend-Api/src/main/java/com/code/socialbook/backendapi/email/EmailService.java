package com.code.socialbook.backendapi.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendEmail(
            String to,
            String username,
            EmailTemplate emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject)  {

       try{
           //Validating Email Address
           validateEmailAddress(to);
           String templateName = (emailTemplate == null ) ? "confirm-email" : emailTemplate.getName();

           MimeMessage message = mailSender.createMimeMessage();
           MimeMessageHelper helper = new MimeMessageHelper(
                   message,
                   MULTIPART_MODE_MIXED,
                   UTF_8.name());

           Map<String, Object> properties = new HashMap<>();
           properties.put("username", username);
           properties.put("confirmationUrl", confirmationUrl);
           properties.put("activationCode", activationCode);

           Context thymeleafContext = new Context();
           thymeleafContext.setVariables(properties);

           helper.setFrom("yhakizimana@rca.ac.rw");
           helper.setTo(to);
           helper.setSubject(subject);

           String template = templateEngine.process(templateName, thymeleafContext);
           helper.setText(template, true);
           mailSender.send(message);
       } catch (AddressException e) {
           throw new IllegalArgumentException("Invalid email address: ", e);
       } catch (MessagingException e){
           throw new RuntimeException("Failed to send the email", e);
       }
    }

    private void validateEmailAddress(String email) throws AddressException {
        InternetAddress internetAddress = new InternetAddress(email);
        internetAddress.validate();
    }
}
