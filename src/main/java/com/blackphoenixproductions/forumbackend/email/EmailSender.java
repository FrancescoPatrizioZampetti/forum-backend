package com.blackphoenixproductions.forumbackend.email;


import com.blackphoenixproductions.forumbackend.entity.Post;
import com.blackphoenixproductions.forumbackend.entity.User;
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
public class EmailSender {

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


    public void sendSigninEmail(User user) {
        StringBuilder message = new StringBuilder();
        message.append("<h1>Benvenuto nel nostro forum "+user.getUsername()+"!</h1><br>");
        message.append("<h2>Ti ringraziamo per esserti registrato, ora puoi partecipare attivamente alla nostra community!</h2><br>");
        setEmailFooter(message);
        send(user.getEmail(), "Black Phoenix Productions ti da il benvenuto!", message.toString());
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

    public void sendResetCredentialsEmail(User user, String token) {
        StringBuilder message = new StringBuilder();
        message.append("<h2>Ciao " + user.getUsername() + ", hai avviato la procedura di reset della password.<br><br>Per completarla procedi cliccando nel link sotto entro 15 minuti dall'arrivo di questa email.</h2><br>");
        String url = domain + "/finishresetcredentials?token=" + token + "&username=" + user.getUsername();
        message.append("<h2><a href=\"" + url + "\">Clicca qui per continuare con la procedura di reset.</a></h2><br>");
        setEmailFooter(message);
        send(user.getEmail(), "Black Phoenix Productions - Reset Credenziali", message.toString());
    }

    private void setEmailFooter(StringBuilder body){
        body.append("<br><br><br>&#169; All Rights Reserved 2021 by Black Phoenix Productions.");
    }




}
