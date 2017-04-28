package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.LostAndFoundUserApplication;
import cn.gaoyuexiang.lostAndFound.dao.LoginRepo;
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
  private String userState;

  @Before
  public void setUp() throws Exception {
    username = "username";
    token = "token";
    userState = "offline";
  }

  @Test
  public void should_return_online_when_user_is_online() throws Exception {
    userState = "online";
    Login login = new Login();
    login.setToken(token);

    when(loginRepo.findByUsername(username)).thenReturn(login);
    when(passwordService.match(token, token)).thenReturn(true);

    String online = loginService.checkState(username, token);

    assertThat(online, is(userState));
  }

  @Test
  public void should_return_offline_when_user_is_offline() throws Exception {
    when(loginRepo.findByUsername(username)).thenReturn(null);

    String offline = loginService.checkState(username, token);

    assertThat(offline, is(userState));
  }

  @Test
  public void should_return_offline_when_given_user_with_error_token() throws Exception {
    Login login = new Login();
    login.setToken(token);

    when(loginRepo.findByUsername(username)).thenReturn(login);
    when(passwordService.match(token, token)).thenReturn(false);

    String checkStateMsg = loginService.checkState(username, token);

    assertThat(checkStateMsg, is(userState));
  }
}