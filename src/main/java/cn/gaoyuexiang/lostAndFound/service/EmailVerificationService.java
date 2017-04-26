package cn.gaoyuexiang.lostAndFound.service;

import javax.mail.MessagingException;

public interface EmailVerificationService {

  String apply(String email, String token) throws MessagingException;

}
