package cn.gaoyuexiang.lostAndFound.service;

import javax.mail.MessagingException;

public interface EmailService {

  void send(String to, String content) throws MessagingException;

}
