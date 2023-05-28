package org.nextlevel.email;

import org.thymeleaf.context.Context;

import java.util.Map;

public interface EmailService {

    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    //Formatted HTML email
    String sendHtmlMail(EmailDetails details, String templateName, Context context);

    String sendMessageUsingThymeleafTemplate(EmailDetails details, String templateName,
                                             Map<String, Object> templateModel);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}
