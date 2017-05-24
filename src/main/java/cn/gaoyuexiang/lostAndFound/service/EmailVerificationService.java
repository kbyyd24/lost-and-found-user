package cn.gaoyuexiang.lostAndFound.service;

import cn.gaoyuexiang.lostAndFound.enums.EmailVerifyMsg;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

public interface EmailVerificationService {

  @Transactional(rollbackFor = MessagingException.class)
  EmailVerifyMsg apply(String email, String token) throws MessagingException;

  EmailVerifyMsg verify(String username, String userToken, String email, String verifyToken);

}
