package com.codewithroy.resttester.expense.service;

import com.codewithroy.resttester.expense.web.exception.ExpenseException;
import com.codewithroy.resttester.expense.model.EmailNotification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;
    private final MailBuilder mailBuilder;

    @Async
    public void sendMail(EmailNotification emailNotification) {
        MimeMessagePreparator mimeMessagePreparator =  mimeMessage -> {
            MimeMessageHelper messageHelper =  new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("iaulqbfcsnxflonpfj@email.com");
            messageHelper.setTo(emailNotification.getRecipient());
            messageHelper.setSubject(emailNotification.getSubject());
            messageHelper.setText(mailBuilder.build(emailNotification.getBody()));
        };
        try {
            javaMailSender.send(mimeMessagePreparator);
            log.info("Mail Sent successfully");
        } catch (MailException e) {
            e.printStackTrace();
            throw new ExpenseException("Exception occured while sending auth mail to " + emailNotification.getRecipient() );

        }
    }
}
