package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class HtmlEmailService implements EmailService {

  private JavaMailSender javaMailSender;

  private String fromMail;

  @Autowired
  public HtmlEmailService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
    this.fromMail = "cins@gaoyuexiang.cn";
  }

  @Override
  public void send(String to, String content) throws MessagingException {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false, "utf-8");
    messageHelper.setText(content, true);
    messageHelper.setTo(to);
    messageHelper.setSubject("请验证邮箱");
    messageHelper.setFrom(fromMail);
    javaMailSender.send(mimeMessage);
  }
}
