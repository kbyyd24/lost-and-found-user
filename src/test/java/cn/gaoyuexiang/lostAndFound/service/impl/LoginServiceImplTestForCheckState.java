package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.LostAndFoundUserApplication;
import cn.gaoyuexiang.lostAndFound.dao.LoginRepo;
import cn.gaoyuexiang.lostAndFound.enums.UserState;
import cn.gaoyuexiang.lostAndFound.model.persistence.Login;
import cn.gaoyuexiang.lostAndFound.service.LoginService;
import cn.gaoyuexiang.lostAndFound.service.PasswordService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static cn.gaoyuexiang.lostAndFound.enums.UserState.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LostAndFoundUserApplication.class)
public class LoginServiceImplTestForCheckState {

  @Autowired
  private LoginService loginService;

  @MockBean
  private LoginRepo loginRepo;

  @MockBean
  private PasswordService passwordService;

  private String username;
  private String token;
  private UserState userState;

  @Before
  public void setUp() throws Exception {
    username = "username";
    token = "token";
  }

  @Test
  public void should_return_online_when_user_is_online() throws Exception {
    userState = ONLINE;
    Login login = new Login();
    login.setToken(token);
    when(loginRepo.findByUsername(username)).thenReturn(login);
    when(passwordService.match(token, token)).thenReturn(true);
    UserState userState = loginService.checkState(username, token);
    assertThat(userState, is(this.userState));
  }

  @Test
  public void should_return_offline_when_user_is_offline() throws Exception {
    this.userState = OFFLINE;
    when(loginRepo.findByUsername(username)).thenReturn(null);
    UserState userState = loginService.checkState(username, token);
    assertThat(userState, is(this.userState));
  }

  @Test
  public void should_return_unauthorized_when_given_user_with_error_token() throws Exception {
    this.userState = UNAUTHORIZED;
    Login login = new Login();
    login.setToken(token);
    when(loginRepo.findByUsername(username)).thenReturn(login);
    when(passwordService.match(token, token)).thenReturn(false);
    UserState userState = loginService.checkState(username, token);
    assertThat(userState, is(this.userState));
  }
}