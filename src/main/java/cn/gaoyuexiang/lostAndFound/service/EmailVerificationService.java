package cn.gaoyuexiang.lostAndFound.service;

import cn.gaoyuexiang.lostAndFound.enums.EmailVerifyMsg;

import javax.mail.MessagingException;

public interface EmailVerificationService {

  EmailVerifyMsg apply(String email, String token) throws MessagingException;

  EmailVerifyMsg verify(String username, String userToken, String email, String verifyToken);

}
