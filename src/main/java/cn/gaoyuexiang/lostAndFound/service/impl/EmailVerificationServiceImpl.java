package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.dao.EmailVerifierRepo;
import cn.gaoyuexiang.lostAndFound.dao.UserRepo;
import cn.gaoyuexiang.lostAndFound.enums.EmailVerifyMsg;
import cn.gaoyuexiang.lostAndFound.enums.UserState;
import cn.gaoyuexiang.lostAndFound.model.persistence.EmailVerifier;
import cn.gaoyuexiang.lostAndFound.model.persistence.User;
import cn.gaoyuexiang.lostAndFound.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalTime;

import static cn.gaoyuexiang.lostAndFound.enums.UserState.OFFLINE;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

  private final UserRepo userRepo;
  private final EmailVerifierRepo emailVerifierRepo;
  private final IdCreatorService idCreatorService;
  private final TokenService tokenService;
  private final EmailFormatService emailFormatService;
  private final EmailService emailService;
  private final LoginService loginService;
  private PasswordService passwordService;

  @Autowired
  public EmailVerificationServiceImpl(UserRepo userRepo,
                                      EmailVerifierRepo emailVerifierRepo,
                                      IdCreatorService idCreatorService,
                                      TokenService tokenService,
                                      EmailFormatService emailFormatService,
                                      EmailService emailService,
                                      LoginService loginService,
                                      PasswordService passwordService) {
    this.userRepo = userRepo;
    this.emailVerifierRepo = emailVerifierRepo;
    this.idCreatorService = idCreatorService;
    this.tokenService = tokenService;
    this.emailFormatService = emailFormatService;
    this.emailService = emailService;
    this.loginService = loginService;
    this.passwordService = passwordService;
  }

  @Override
  public EmailVerifyMsg apply(String email, String token) throws MessagingException {
    User user = userRepo.findByEmail(email);
    if (user == null) {
      return EmailVerifyMsg.EMAIL_NOT_FOUND;
    }
    if (user.getEmailEnable()) {
      return EmailVerifyMsg.EMAIL_ENABLED;
    }
    UserState userState = loginService.checkState(user.getUsername(), token);
    if (userState.equals(OFFLINE)) {
      return EmailVerifyMsg.UNAUTHORIZED;
    }
    String emailToken = tokenService.buildToken();
    long createTime = System.currentTimeMillis();
    long expireTime = createTime + 30 * 60 * 1000;
    EmailVerifier emailVerifier = new EmailVerifier();
    emailVerifier.setId(idCreatorService.create());
    emailVerifier.setEmail(email);
    emailVerifier.setToken(passwordService.encode(emailToken));
    emailVerifier.setCreateTime(createTime);
    emailVerifierRepo.save(emailVerifier);
    String emailText = emailFormatService.format(user.getUsername(), emailToken, expireTime);
    emailService.send(email, emailText);
    return EmailVerifyMsg.SUCCESS;
  }

  @Override
  public String verify(String username, String userToken, String email, String verifyToken) {
    UserState userState = loginService.checkState(username, userToken);
    if (userState.equals(OFFLINE)) {
      return "unauthorized";
    }
    EmailVerifier emailVerifier = emailVerifierRepo.findByEmail(email);
    if (emailVerifier.getCreateTime() + 30 * 60 * 1000 - System.currentTimeMillis() < 0) {
      return "timeout";
    }
    boolean isMatch = passwordService.match(verifyToken, emailVerifier.getToken());
    if (!isMatch) {
      return "unauthorized";
    }
    if (!userRepo.findByUsername(username).getEmail().equals(email)) {
      return "unauthorized";
    }
    userRepo.enableEmailByUsername(username);
    emailVerifierRepo.delete(emailVerifier);
    return "success";
  }
}
