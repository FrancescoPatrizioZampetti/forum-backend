package com.blackphoenixproductions.forumbackend.infrastructure.email;


import com.blackphoenixproductions.forumbackend.domain.entity.Post;
import com.blackphoenixproductions.forumbackend.domain.entity.User;
import com.blackphoenixproductions.forumbackend.domain.IEmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
public class EmailSender implements IEmailSender {

    private final JavaMailSender javaMailSender;
    private final String domain;
    private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);


    @Autowired
    public EmailSender(JavaMailSender javaMailSender, @Value("${domain}") String domain) {
        this.javaMailSender = javaMailSender;
        this.domain = domain;
    }

    private void send(String to, String subject, String body)  {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setText("<img src='cid:logo'>" + body, true);
            ClassPathResource res = new ClassPathResource("/static/blackphoenix@2x.png");
            helper.addInline("logo", res);
            javaMailSender.send(message);
        } catch (Exception e){
            logger.error("Errore durante l'invio dell'email.");
        }
    }

    public void sendTopicReplyEmail(User user, User userReply, Post post) {
        String url = domain + "/forum";
        StringBuilder message = new StringBuilder();
        message.append("<h2>L'utente " + userReply.getUsername() + " ha risposto al tuo topic: " + post.getTopic().getTitle() + "</h2><br>");
        message.append("<h2>Messaggio: " + post.getMessage() + "</h2><br>");
        message.append("<h2>Accedi al <a href=\"" + url + "\">forum</a> per rispondere.</h2><br>");
        setEmailFooter(message);
        send(user.getEmail(), "Qualcuno ha risposto al tuo topic!", message.toString());
    }

    private void setEmailFooter(StringBuilder body){
        body.append("<br><br><br>&#169; All Rights Reserved 2021 by Black Phoenix Productions.");
    }




}
