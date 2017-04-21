package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.dao.LoginRepo;
import cn.gaoyuexiang.lostAndFound.dao.UserRepo;
import cn.gaoyuexiang.lostAndFound.exception.MissParamException;
import cn.gaoyuexiang.lostAndFound.exception.PasswordNotMatchException;
import cn.gaoyuexiang.lostAndFound.exception.UserNotExistException;
import cn.gaoyuexiang.lostAndFound.model.dto.LoginToken;
import cn.gaoyuexiang.lostAndFound.model.dto.LoginUser;
import cn.gaoyuexiang.lostAndFound.model.persistence.User;
import cn.gaoyuexiang.lostAndFound.service.IdCreatorService;
import cn.gaoyuexiang.lostAndFound.service.LoginService;
import cn.gaoyuexiang.lostAndFound.service.PasswordService;
import cn.gaoyuexiang.lostAndFound.service.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceImplTestForLogin {

  private LoginService loginService;

  @Mock
  private LoginRepo loginRepo;

  @Mock
  private UserRepo userRepo;

  @Mock
  private PasswordService passwordService;

  @Mock
  private TokenService tokenService;

  @Mock
  private IdCreatorService idCreatorService;

  @Before
  public void setUp() throws Exception {
    loginService = new LoginServiceImpl(loginRepo, userRepo, passwordService, tokenService, idCreatorService);
  }

  @Test(expected = MissParamException.class)
  public void should_throw_MissParamException_when_given_a_loginUser_miss_password() throws Exception {
    LoginUser loginUser = new LoginUser();
    String loginName = "loginName";
    loginUser.setLoginName(loginName);
    loginService.login(loginUser);
  }

  @Test(expected = UserNotExistException.class)
  public void should_throw_UserNotExistException_when_given_a_user_with_username_not_exist() throws Exception {
    LoginUser loginUser = new LoginUser();
    String username = "username";
    loginUser.setLoginName(username);
    loginUser.setPassword("password");

    when(userRepo.findByUsernameOrEmail(username, username)).thenReturn(null);

    loginService.login(loginUser);
  }

  @Test(expected = PasswordNotMatchException.class)
  public void should_throw_PasswordNotMatchException_when_given_a_loginUser_with_error_password() throws Exception {
    LoginUser loginUser = new LoginUser();
    String loginName = "loginName";
    loginUser.setLoginName(loginName);
    String password = "password";
    loginUser.setPassword(password);

    User user = Mockito.mock(User.class);
    String apass = "another password";
    when(user.getPassword()).thenReturn(apass);
    when(userRepo.findByUsernameOrEmail(loginName, loginName)).thenReturn(user);
    when(passwordService.match(password, apass)).thenReturn(false);

    loginService.login(loginUser);
  }

  @Test
  public void should_return_token_when_given_a_valid_loginUser() throws Exception {
    LoginUser loginUser = new LoginUser();
    String username = "username";
    String password = "password";
    loginUser.setLoginName(username);
    loginUser.setPassword(password);
    String tokenMsg = "token";
    LoginToken expectToken = new LoginToken(tokenMsg);

    User user = Mockito.mock(User.class);
    when(user.getPassword()).thenReturn(password);
    when(user.getUsername()).thenReturn(username);
    when(userRepo.findByUsernameOrEmail(username, username)).thenReturn(user);
    when(passwordService.match(password, password)).thenReturn(true);
    when(tokenService.buildToken()).thenReturn(tokenMsg);

    LoginToken token = loginService.login(loginUser);
    assertThat(token, is(expectToken));
  }
}