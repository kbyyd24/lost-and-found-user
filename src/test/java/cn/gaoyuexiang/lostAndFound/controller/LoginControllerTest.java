package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.exception.MissParamException;
import cn.gaoyuexiang.lostAndFound.exception.PasswordNotMatchException;
import cn.gaoyuexiang.lostAndFound.exception.UserNotExistException;
import cn.gaoyuexiang.lostAndFound.model.dto.LoginToken;
import cn.gaoyuexiang.lostAndFound.model.dto.LoginUser;
import cn.gaoyuexiang.lostAndFound.model.dto.enums.LoginMsg;
import cn.gaoyuexiang.lostAndFound.service.LoginService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private LoginService loginService;

  @Test
  public void should_return_200_when_given_a_valid_user() throws Exception {
    given(loginService.login(any(LoginUser.class))).willReturn(new LoginToken("token"));
    LoginUser loginUser = new LoginUser();

    ResponseEntity<LoginToken> entity = restTemplate.postForEntity("/user/login", loginUser, LoginToken.class);

    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
  }

  @Test
  @Ignore
  public void should_return_400_when_given_a_user_without_password() throws Exception {
    when(loginService.login(any(LoginUser.class))).thenThrow(new MissParamException());
    LoginUser loginUser = new LoginUser();

    ResponseEntity<LoginMsg> entity = restTemplate.postForEntity("/user/login", loginUser, LoginMsg.class);

    assertThat(entity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    assertThat(entity.getBody(), is(LoginMsg.MSG_NOT_COMPLETE));
  }

  @Test
  @Ignore
  public void should_return_401_when_given_a_user_not_signed_in() throws Exception {
    given(loginService.login(any(LoginUser.class))).willThrow(new UserNotExistException());
    LoginUser loginUser = new LoginUser();

    ResponseEntity<LoginMsg> entity = restTemplate.postForEntity("/user/login", loginUser, LoginMsg.class);

    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    assertThat(entity.getBody(), is(LoginMsg.USERNAME_PASSWORD_NOT_MATCH));
  }

  @Test
  @Ignore
  public void should_return_401_when_given_a_user_with_error_password() throws Exception {
    given(loginService.login(any(LoginUser.class))).willThrow(new PasswordNotMatchException());
    LoginUser loginUser = new LoginUser();

    ResponseEntity<LoginMsg> entity = restTemplate.postForEntity("/user/login", loginUser, LoginMsg.class);

    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    assertThat(entity.getBody(), is(LoginMsg.USERNAME_PASSWORD_NOT_MATCH));
  }
}