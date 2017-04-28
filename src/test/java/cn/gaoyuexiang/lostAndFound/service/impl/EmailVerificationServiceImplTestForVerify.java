package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.LostAndFoundUserApplication;
import cn.gaoyuexiang.lostAndFound.dao.EmailVerifierRepo;
import cn.gaoyuexiang.lostAndFound.dao.UserRepo;
import cn.gaoyuexiang.lostAndFound.enums.UserState;
import cn.gaoyuexiang.lostAndFound.model.persistence.EmailVerifier;
import cn.gaoyuexiang.lostAndFound.model.persistence.User;
import cn.gaoyuexiang.lostAndFound.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static cn.gaoyuexiang.lostAndFound.enums.UserState.OFFLINE;
import static cn.gaoyuexiang.lostAndFound.enums.UserState.ONLINE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LostAndFoundUserApplication.class)
public class EmailVerificationServiceImplTestForVerify {

  @Autowired
  private EmailVerificationService emailVerificationService;

  @MockBean
  private UserRepo userRepo;

  @MockBean
  private EmailVerifierRepo emailVerifierRepo;

  @MockBean
  private IdCreatorService idCreatorService;

  @MockBean
  private TokenService tokenService;

  @MockBean
  private EmailFormatService emailFormatService;

  @MockBean
  private EmailService emailService;

  @MockBean
  private LoginService loginService;

  @MockBean
  private PasswordService passwordService;

  private String username;
  private String userToken;
  private String email;
  private String verifyToken;

  @Before
  public void setUp() throws Exception {
    emailVerificationService =
        new EmailVerificationServiceImpl(userRepo,
            emailVerifierRepo,
            idCreatorService,
            tokenService,
            emailFormatService,
            emailService,
            loginService,
            passwordService);
    username = "username";
    userToken = "userToken";
    email = "email";
    verifyToken = "verifyToken";
  }

  @Test
  public void should_return_success_when_verify_success() throws Exception {
    String encodedVerifyToken = "encoded verify token";
    long createTime = System.currentTimeMillis();
    when(loginService.checkState(username, userToken)).thenReturn(ONLINE);
    EmailVerifier emailVerifier = mock(EmailVerifier.class);
    when(emailVerifier.getToken()).thenReturn(encodedVerifyToken);
    when(emailVerifier.getCreateTime()).thenReturn(createTime);
    when(emailVerifierRepo.findByEmail(email)).thenReturn(emailVerifier);
    when(passwordService.match(verifyToken, encodedVerifyToken)).thenReturn(true);
    User user = mock(User.class);
    when(user.getEmail()).thenReturn(email);
    when(userRepo.findByUsername(username)).thenReturn(user);
    doNothing().when(userRepo).enableEmailByUsername(username);
    String verifyResult = emailVerificationService.verify(username, userToken, email, verifyToken);
    assertThat(verifyResult, is("success"));
  }

  @Test
  public void should_return_unauthorized_when_user_is_offline() throws Exception {
    when(loginService.checkState(username, userToken)).thenReturn(OFFLINE);
    String verifyResult = emailVerificationService.verify(username, userToken, email, verifyToken);
    assertThat(verifyResult, is("unauthorized"));
  }

  @Test
  public void should_return_timeout_when_verify_time_too_late() throws Exception {
    long createTime = 1L;
    EmailVerifier mockVerifier = mock(EmailVerifier.class);
    when(loginService.checkState(username, userToken)).thenReturn(ONLINE);
    when(emailVerifierRepo.findByEmail(email)).thenReturn(mockVerifier);
    when(mockVerifier.getCreateTime()).thenReturn(createTime);
    String verifyResult = emailVerificationService.verify(username, userToken, email, verifyToken);
    assertThat(verifyResult, is("timeout"));
  }

  @Test
  public void should_return_unauthorized_when_user_and_email_not_match() throws Exception {
    String anotherEmail = "anotherEmail";
    EmailVerifier emailVerifier = mock(EmailVerifier.class);
    when(emailVerifier.getToken()).thenReturn(verifyToken);
    when(emailVerifier.getCreateTime()).thenReturn(System.currentTimeMillis());
    User user = mock(User.class);
    when(user.getEmail()).thenReturn(anotherEmail);

    when(loginService.checkState(username, userToken)).thenReturn(ONLINE);
    when(emailVerifierRepo.findByEmail(email)).thenReturn(emailVerifier);
    when(passwordService.match(anyString(), anyString())).thenReturn(true);
    when(userRepo.findByUsername(username)).thenReturn(user);

    String verifyResult = emailVerificationService.verify(username, userToken, email, verifyToken);

    assertThat(verifyResult, is("unauthorized"));
  }
}