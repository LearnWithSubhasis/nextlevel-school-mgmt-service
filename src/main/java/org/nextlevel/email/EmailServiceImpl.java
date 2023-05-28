package org.nextlevel.email;

import java.io.File;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

// Annotation
@Service
// Class
// Implementing EmailService interface
public class EmailServiceImpl implements EmailService {

    @Autowired private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${spring.mail.username}") private String sender;

    @Value("classpath:org_admin_invitation.png")
    Resource resourceFileOrgAdminInvite;

    @Value("classpath:school_admin_invitation.png")
    Resource resourceFileSchoolAdminInvite;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    public static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Bean
    public ITemplateResolver thymeleafTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

//    @Bean
//    public SpringTemplateEngine thymeleafTemplateEngine(ITemplateResolver templateResolver) {
//        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.setTemplateResolver(templateResolver);
//        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
//        return templateEngine;
//    }


    // Method 1
    // To send a simple email
    @Async
    public String sendSimpleMail(EmailDetails details)
    {
        // Try block to check for exceptions
        try {

            // Creating a simple mail message
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();

            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

    @Async
    public String sendHtmlMail(EmailDetails details, String templateName, Context context) {
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        String body = templateEngine.process(templateName, context);
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());
            helper.setText(body, true);

            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            String errorMsg = "Error while Sending Mail (HTML): " + e.getMessage();
            LOG.error(errorMsg);
            return errorMsg;
        }
        return "Mail (HTML) Sent Successfully...";
    }

    @Async
    public String sendMessageUsingThymeleafTemplate(EmailDetails details, String templateName,
                                                  Map<String, Object> templateModel) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process(templateName, thymeleafContext);

        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mailMessage, MimeMessageHelper.MULTIPART_MODE_MIXED, "UTF-8");
            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());
            helper.setText(htmlBody, true);

            String inlineType = (String) templateModel.get("inlineImage");
            if(null != inlineType) {
                if(inlineType.equalsIgnoreCase("organisation")) {
                    helper.addInline("attachment.png", resourceFileOrgAdminInvite);

//                    // Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
//                    Resource resource = resourceLoader.getResource("classpath:org_admin_invitation.png");
//                    File file = resource.getFile();
//                    byte[] imageBytes = Files.readAllBytes(file.toPath());
//                    final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
//                    helper.addInline("attachment.png", imageSource, "image/png");
                }
                else if(inlineType.equalsIgnoreCase("school")) {
                    helper.addInline("attachment.png", resourceFileSchoolAdminInvite);
                }
            }

            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            String errorMsg = "Error while Sending Mail (HTML): " + e.getMessage();
            LOG.error(errorMsg);
            return errorMsg;
        }

        return "Mail (HTML) Sent Successfully...";
    }

    // Method 2
    // To send an email with attachment
    @Async
    public String sendMailWithAttachment(EmailDetails details)
    {
        // Creating a mime message
        MimeMessage mimeMessage
                = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {

            // Setting multipart as true for attachments to
            // be send
            mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMsgBody());
            mimeMessageHelper.setSubject(
                    details.getSubject());

            // Adding the attachment
            FileSystemResource file
                    = new FileSystemResource(
                    new File(details.getAttachment()));

            mimeMessageHelper.addAttachment(
                    file.getFilename(), file);

            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }

        // Catch block to handle MessagingException
        catch (MessagingException e) {

            // Display message when exception occurred
            return "Error while sending mail!!!";
        }
    }
}
