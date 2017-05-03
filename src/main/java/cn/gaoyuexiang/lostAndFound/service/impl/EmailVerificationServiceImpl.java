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
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalTime;

import static cn.gaoyuexiang.lostAndFound.enums.EmailVerifyMsg.*;
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
  private TimeService timeService;

  @Autowired
  public EmailVerificationServiceImpl(UserRepo userRepo,
                                      EmailVerifierRepo emailVerifierRepo,
                                      IdCreatorService idCreatorService,
                                      TokenService tokenService,
                                      EmailFormatService emailFormatService,
                                      EmailService emailService,
                                      LoginService loginService,
                                      PasswordService passwordService,
                                      TimeService timeService) {
    this.userRepo = userRepo;
    this.emailVerifierRepo = emailVerifierRepo;
    this.idCreatorService = idCreatorService;
    this.tokenService = tokenService;
    this.emailFormatService = emailFormatService;
    this.emailService = emailService;
    this.loginService = loginService;
    this.passwordService = passwordService;
    this.timeService = timeService;
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
    EmailVerifyMsg x = checkUserState(user.getUsername(), token);
    if (x != null) return x;
    String emailToken = tokenService.buildToken();
    long createTime = timeService.now();
    long expireTime = timeService.getExpireTime(createTime);
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
  public EmailVerifyMsg verify(String username, String userToken, String email, String verifyToken) {
    EmailVerifyMsg x = checkUserState(username, userToken);
    if (x != null) return x;
    if (!userRepo.findByUsername(username).getEmail().equals(email)) {
      return UNAUTHORIZED;
    }
    EmailVerifier emailVerifier = emailVerifierRepo.findByEmail(email);
    if (emailVerifier == null) {
      return EMAIL_NOT_FOUND;
    }
    if (timeService.isExpire(emailVerifier.getCreateTime())) {
      return TOKEN_TIMEOUT;
    }
    boolean isMatch = passwordService.match(verifyToken, emailVerifier.getToken());
    if (!isMatch) {
      return UNAUTHORIZED;
    }
    enableEmail(username, emailVerifier);
    return SUCCESS;
  }

  private EmailVerifyMsg checkUserState(String username, String userToken) {
    UserState userState = loginService.checkState(username, userToken);
    if (userState.equals(UserState.UNAUTHORIZED)) {
      return UNAUTHORIZED;
    } else if (userState.equals(UserState.OFFLINE)) {
      return EmailVerifyMsg.OFFLINE;
    }
    return null;
  }

  @Transactional
  private void enableEmail(String username, EmailVerifier emailVerifier) {
    userRepo.enableEmailByUsername(username);
    emailVerifierRepo.delete(emailVerifier.getId());
  }
}
