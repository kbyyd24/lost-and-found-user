package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.LostAndFoundUserApplication;
import cn.gaoyuexiang.lostAndFound.dao.LoginRepo;
import cn.gaoyuexiang.lostAndFound.enums.LoginMsg;
import cn.gaoyuexiang.lostAndFound.enums.UserState;
import cn.gaoyuexiang.lostAndFound.model.persistence.Login;
import cn.gaoyuexiang.lostAndFound.service.LoginService;
import cn.gaoyuexiang.lostAndFound.service.PasswordService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LostAndFoundUserApplication.class)
public class LoginServiceImplTestForLogout {

  @Autowired
  private LoginService loginService;

  @MockBean
  private LoginRepo loginRepo;

  @MockBean
  private PasswordService passwordService;

  private String username;
  private String token;

  @Before
  public void setUp() throws Exception {
    username = "username";
    token = "token";
  }

  @Test
  public void should_return_logout_success_when_user_is_online() throws Exception {
    Login login = new Login();
    login.setToken(token);
    when(loginRepo.findByUsername(username)).thenReturn(login);
    when(passwordService.match(token, token)).thenReturn(true);
    doNothing().when(loginRepo).delete(login);
    checkResult(LoginMsg.LOGOUT_SUCCESS);
  }

  @Test
  public void should_return_unauthorized_when_username_and_token_not_matched() throws Exception {
    Login login = Mockito.mock(Login.class);
    when(login.getToken()).thenReturn(token);
    when(loginRepo.findByUsername(username)).thenReturn(login);
    when(passwordService.match(token, token)).thenReturn(false);
    checkResult(LoginMsg.UNAUTHORIZED);
  }

  @Test
  public void should_return_offline_when_user_is_offline() throws Exception {
    when(loginRepo.findByUsername(username)).thenReturn(null);
    checkResult(LoginMsg.OFFLINE);
  }

  private void checkResult(LoginMsg expectLogout) {
    LoginMsg msg = loginService.logout(username, token);
    assertThat(msg, is(expectLogout));
  }
}