package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.LostAndFoundUserApplication;
import cn.gaoyuexiang.lostAndFound.dao.LoginRepo;
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

  @Before
  public void setUp() throws Exception {
    loginService = new LoginServiceImpl(loginRepo, null, passwordService, null, null);
  }

  @Test
  public void should_return_logout_success_when_given_valid_user_and_token() throws Exception {
    String username = "username";
    String token = "token";
    String expectLogout = "logout success";
    Login login = new Login();
    login.setToken(token);
    when(loginRepo.findByUsername(username)).thenReturn(login);
    when(passwordService.match(token, token)).thenReturn(true);
    doNothing().when(loginRepo).delete(login);

    String logoutMsg = loginService.logout(username, token);

    assertThat(logoutMsg, is(expectLogout));
  }

  @Test
  public void should_return_offline_when_username_and_token_not_matched() throws Exception {
    String token = "token";
    String username = "username";
    Login login = Mockito.mock(Login.class);
    when(login.getToken()).thenReturn(token);
    when(loginRepo.findByUsername(username)).thenReturn(login);
    when(passwordService.match(token, token)).thenReturn(false);

    String logoutMsg = loginService.logout(username, token);

    String userNotFound = "user not found";
    assertThat(logoutMsg, is(userNotFound));
  }

  @Test
  public void should_return_user_not_found_when_user_is_offline() throws Exception {
    String username = "username";
    when(loginRepo.findByUsername(username)).thenReturn(null);
    String logoutMsg = loginService.logout(username, "token");
    String userNotFound = "user not found";
    assertThat(logoutMsg, is(userNotFound));
  }
}