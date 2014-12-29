package com.mycompany.template.utils;

import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by azee on 29.12.14.
 */
@Service
public class EmailUtils {
    private final static Logger log = Logger.getLogger(EmailUtils.class);

    @Value("${smtp.host}")
    String SMTP_HOST;

    @Value("${smtp.port}")
    int SMTP_PORT;

    @Value("${smtp.from}")
    String SMTP_FROM;

    final String ENCODING = "utf-8";

    public void sendEmail(List<String> subscribers, String subject, String body) {
        if (subscribers == null || subscribers.size() == 0){
            return;
        }

        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(SMTP_HOST);
            email.setSmtpPort(SMTP_PORT);
            email.setFrom(SMTP_FROM);
            for (String to : subscribers) {
                if (to != null && !"".equals(to)){
                    email.addTo(to.trim());
                }
            }

            email.setSubject(subject);
            email.setCharset(ENCODING);
            email.setHtmlMsg(body);
            email.send();

            log.info("Email sent to [" + email.getToAddresses().toString() + "]");
        } catch (Exception e) {
            log.warn("An exception '" + e.getMessage() +
                    "' occur trying to send email to [" + subscribers.toString() + "]");
        }
    }
}