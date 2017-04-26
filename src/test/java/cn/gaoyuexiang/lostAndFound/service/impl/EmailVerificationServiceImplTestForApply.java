package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.dao.EmailVerifierRepo;
import cn.gaoyuexiang.lostAndFound.dao.LoginRepo;
import cn.gaoyuexiang.lostAndFound.dao.UserRepo;
import cn.gaoyuexiang.lostAndFound.model.persistence.EmailVerifier;
import cn.gaoyuexiang.lostAndFound.model.persistence.User;
import cn.gaoyuexiang.lostAndFound.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailVerificationServiceImplTestForApply {

  private EmailVerificationService emailVerificationService;

  @Mock
  private UserRepo userRepo;

  @Mock
  private EmailVerifierRepo emailVerifierRepo;

  @Mock
  private IdCreatorService idCreatorService;

  @Mock
  private TokenService tokenService;

  @Mock
  private EmailFormatService emailFormatService;

  @Mock
  private EmailService emailService;

  @Mock
  private LoginService loginService;

  @Mock
  private PasswordService passwordService;

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
  }

  @Test
  public void should_return_mail_was_sent_when_token_and_email_matched_and_email_not_enable() throws Exception {
    String username = "username";
    String email = "email";
    String token = "token";

    User user = mock(User.class);
    when(user.getUsername()).thenReturn(username);
    when(user.getEmailEnable()).thenReturn(false);

    when(userRepo.findByEmail(email)).thenReturn(user);

    String userState = "online";
    when(loginService.checkState(username, token)).thenReturn(userState);

    when(idCreatorService.create()).thenReturn("id");
    when(tokenService.buildToken()).thenReturn(token);

    when(emailVerifierRepo.save(any(EmailVerifier.class))).thenReturn(new EmailVerifier());

    String text = "text";
    when(emailFormatService.format(eq(username), eq(token), anyLong())).thenReturn(text);
    doNothing().when(emailService).send(anyString(), eq(text));

    String result = emailVerificationService.apply(email, token);

    assertThat(result, is("success"));
  }

  @Test
  public void should_return_user_not_found_when_email_not_exist() throws Exception {
    String email = "email";
    when(userRepo.findByEmail(email)).thenReturn(null);
    String result = emailVerificationService.apply(email, "token");
    assertThat(result, is("user not found"));
  }

  @Test
  public void should_return_offline_when_token_not_match() throws Exception {
    String email = "email";
    String token = "token";
    String username = "username";
    User user = mock(User.class);
    when(user.getUsername()).thenReturn(username);
    when(userRepo.findByEmail(email)).thenReturn(user);
    when(loginService.checkState(username, token)).thenReturn("offline");
    String result = emailVerificationService.apply(email, token);
    assertThat(result, is("unauthorized"));
  }
}