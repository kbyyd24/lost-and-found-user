package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.LostAndFoundUserApplication;
import cn.gaoyuexiang.lostAndFound.dao.LoginRepo;
import cn.gaoyuexiang.lostAndFound.dao.UserRepo;
import cn.gaoyuexiang.lostAndFound.exception.MissParamException;
import cn.gaoyuexiang.lostAndFound.exception.PasswordNotMatchException;
import cn.gaoyuexiang.lostAndFound.exception.UserNotExistException;
import cn.gaoyuexiang.lostAndFound.model.dto.LoginResponse;
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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LostAndFoundUserApplication.class)
public class LoginServiceImplTestForLogin {

  @Autowired
  private LoginService loginService;

  @MockBean
  private LoginRepo loginRepo;

  @MockBean
  private UserRepo userRepo;

  @MockBean
  private PasswordService passwordService;

  @MockBean
  private TokenService tokenService;

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
    LoginUser loginUser = mock(LoginUser.class);
    String loginName = "loginName";
    String password = "password";
    when(loginUser.getLoginName()).thenReturn(loginName);
    when(loginUser.getPassword()).thenReturn(password);

    User user = mock(User.class);
    String anotherPassword = "another password";
    when(user.getPassword()).thenReturn(anotherPassword);

    when(userRepo.findByUsernameOrEmail(loginName, loginName)).thenReturn(user);
    when(passwordService.match(password, anotherPassword)).thenReturn(false);

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
    String email = "email";
    LoginResponse expectToken = new LoginResponse(username, email, tokenMsg);

    User user = mock(User.class);
    when(user.getPassword()).thenReturn(password);
    when(user.getUsername()).thenReturn(username);
    when(user.getEmail()).thenReturn(email);

    when(userRepo.findByUsernameOrEmail(username, username)).thenReturn(user);
    when(passwordService.match(password, password)).thenReturn(true);
    when(tokenService.buildToken()).thenReturn(tokenMsg);

    LoginResponse token = loginService.login(loginUser);
    assertThat(token, is(expectToken));
  }
}