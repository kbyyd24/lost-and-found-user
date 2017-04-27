package cn.gaoyuexiang.lostAndFound.service;

import javax.mail.MessagingException;

public interface EmailVerificationService {

  String apply(String email, String token) throws MessagingException;

  String verify(String username, String userToken, String email, String verifyToken);

}
