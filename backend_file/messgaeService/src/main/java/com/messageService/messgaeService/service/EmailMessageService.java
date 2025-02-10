package com.messageService.messgaeService.service;

import com.messageService.messgaeService.config.EcommDTO;
import com.messageService.messgaeService.domain.EmailBuilder;
import com.messageService.messgaeService.domain.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@Slf4j
@Service
public class EmailMessageService implements IEmailMessageService {

    private JavaMailSender javaMailSender;
    private final String fromEmail = "cloudcartecommerce@gmail.com";

    @Autowired
    public EmailMessageService(JavaMailSender javaMailSender){
        this.javaMailSender=javaMailSender;
    }

    @Value("${otp.forgot.password.template}")
    private String otpForgotPassword;

    @Value("${welcome.mail.template}")
    private String welcomeMail;

    public void sendMail(EmailMessage message, boolean isHtml) throws MessagingException {

        MimeMessage emailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mailBuilder = new MimeMessageHelper(emailMessage, true);

        mailBuilder.setTo(message.getTo());
        mailBuilder.setFrom("cloudcartecommerce@gmail.com");
        mailBuilder.setText(message.getMailContent(), isHtml);
        mailBuilder.setSubject(message.getSubject());

        javaMailSender.send(emailMessage);

    }

    @RabbitListener(queues = "EmailQueue")
    @Override
    public void sendEmail(EcommDTO ecommDTO) {
        try {

            String to = ecommDTO.getJsonObject().get("to").toString();
            String subject = ecommDTO.getJsonObject().get("subject").toString();
            String name = ecommDTO.getJsonObject().get("name").toString();


            EmailMessage emailMessage = new EmailBuilder()
                    .from(fromEmail).to(to).template(welcomeMail)
                    .addContext("subject",subject)
                    .addContext("name",name)
                    .addContext("currentYear",String.valueOf(LocalDate.now().getYear()))
                    .addContext("email",to)
                    .createMail();
            sendMail(emailMessage,true);

        }catch (Exception ex){
            log.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public void userWelcome(String to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("cloudcartecommerce@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
        System.out.println("Success welcome user");
    }

//    @RabbitListener(queues = "EmailQueue")
    @Override
    public void reqApproved(EcommDTO ecommDTO) {
        EmailMessage emailMessage = new EmailMessage();

        String to = ecommDTO.getJsonObject().get("to").toString();
        String subject = ecommDTO.getJsonObject().get("subject").toString();
        String message = ecommDTO.getJsonObject().get("message").toString();

        emailMessage.setTo(to);
        emailMessage.setSubject(subject);
        emailMessage.setMessage(message);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("cloudcartecommerce@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
        System.out.println("Request Approved");
    }

    @Override
    public int sendOtp(String userEmail) {
        Random ranNum = new Random();
        int otpNum = ranNum.nextInt(1111,9999);
        String emailContent = "Please find below your One-Time Password (OTP) for account verification:\n" +
                              "\n" +
                              "OTP: "+otpNum+"\n" +
                              "\n" +
                              "Kindly enter this OTP within the designated timeframe to complete the verification process.\n" +
                              "\n" +
                              "Best regards,\n" +
                              "Cloud Cart Team";
        String subject ="Supplier Verification.";
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setText(emailContent);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setFrom("cloudcartecommerce@gmail.com");
        simpleMailMessage.setTo(userEmail);
        javaMailSender.send(simpleMailMessage);
        return otpNum;
    }

    @RabbitListener(queues = "ForgotPassQueue")
    @Override
    public void forgotPassSendOtp(EcommDTO ecommDTO) {

        try {
            String to = ecommDTO.getJsonObject().get("to").toString();
            String subject = ecommDTO.getJsonObject().get("subject").toString();
            String otp = ecommDTO.getJsonObject().get("otp").toString();
            String name = ecommDTO.getJsonObject().get("name").toString();

            EmailMessage emailMessage = new EmailBuilder()
                    .from(fromEmail).to(to).template(otpForgotPassword)
                    .addContext("email", to)
                    .addContext("otp", otp)
                    .addContext("name", name)
                    .addContext("currentYear", String.valueOf(LocalDate.now().getYear()))
                    .addContext("subject", subject)
                    .createMail();
            sendMail(emailMessage, true);
        }catch (Exception ex){
            throw new RuntimeException();
        }
    }
}
