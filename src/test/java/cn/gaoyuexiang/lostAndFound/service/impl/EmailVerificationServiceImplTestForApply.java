package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.LostAndFoundUserApplication;
import cn.gaoyuexiang.lostAndFound.dao.EmailVerifierRepo;
import cn.gaoyuexiang.lostAndFound.dao.UserRepo;
import cn.gaoyuexiang.lostAndFound.enums.EmailVerifyMsg;
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
import static cn.gaoyuexiang.lostAndFound.enums.UserState.UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LostAndFoundUserApplication.class)
public class EmailVerificationServiceImplTestForApply {

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

  private String email;
  private String token;

  @Before
  public void setUp() throws Exception {
    email = "email";
    token = "token";
  }

  @Test
  public void should_return_mail_was_sent_when_token_and_email_matched_and_email_not_enable() throws Exception {
    String username = "username";
    String text = "text";
    User user = mock(User.class);
    when(userRepo.findByEmail(email)).thenReturn(user);
    when(user.getEmailEnable()).thenReturn(false);
    when(user.getUsername()).thenReturn(username);
    when(loginService.checkState(username, token)).thenReturn(ONLINE);
    when(idCreatorService.create()).thenReturn("id");
    when(tokenService.buildToken()).thenReturn(token);
    when(passwordService.encode(token)).thenReturn(token);
    when(emailVerifierRepo.save(any(EmailVerifier.class))).thenReturn(new EmailVerifier());
    when(emailFormatService.format(eq(username), eq(token), anyLong())).thenReturn(text);
    doNothing().when(emailService).send(anyString(), eq(text));
    EmailVerifyMsg result = emailVerificationService.apply(email, token);
    assertThat(result, is(EmailVerifyMsg.SUCCESS));
  }

  @Test
  public void should_return_user_not_found_when_email_not_exist() throws Exception {
    when(userRepo.findByEmail(email)).thenReturn(null);
    EmailVerifyMsg result = emailVerificationService.apply(email, token);
    assertThat(result, is(EmailVerifyMsg.EMAIL_NOT_FOUND));
  }

  @Test
  public void should_return_offline_when_user_offline() throws Exception {
    String username = "username";
    User user = mock(User.class);
    when(user.getUsername()).thenReturn(username);
    when(userRepo.findByEmail(email)).thenReturn(user);
    when(loginService.checkState(username, token)).thenReturn(OFFLINE);
    EmailVerifyMsg result = emailVerificationService.apply(email, token);
    assertThat(result, is(EmailVerifyMsg.OFFLINE));
  }

  @Test
  public void should_return_unauthorized_when_token_not_match() throws Exception {
    String username = "username";
    User user = mock(User.class);
    when(user.getUsername()).thenReturn(username);
    when(userRepo.findByEmail(email)).thenReturn(user);
    when(loginService.checkState(username, token)).thenReturn(UNAUTHORIZED);
    EmailVerifyMsg result = emailVerificationService.apply(email, token);
    assertThat(result, is(EmailVerifyMsg.UNAUTHORIZED));
  }

  @Test
  public void should_return_email_enabled_when_enable_is_true() throws Exception {
    User user = mock(User.class);
    when(user.getEmailEnable()).thenReturn(true);
    when(userRepo.findByEmail(email)).thenReturn(user);
    EmailVerifyMsg result = emailVerificationService.apply(email, token);
    assertThat(result, is(EmailVerifyMsg.EMAIL_ENABLED));
  }
}