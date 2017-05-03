package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.enums.UserState;
import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.model.dto.SecurityInfoUpdater;
import cn.gaoyuexiang.lostAndFound.service.LoginService;
import cn.gaoyuexiang.lostAndFound.service.UserSecurityInfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static cn.gaoyuexiang.lostAndFound.enums.UserState.ONLINE;
import static cn.gaoyuexiang.lostAndFound.enums.UserState.UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserSecurityInfoControllerTestForUpdateInfo {

  private static final String URI_FORMAT = "/user/info/%s/security";
  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private UserSecurityInfoService userSecurityInfoService;

  @MockBean
  private LoginService loginService;

  private String username;
  private String token;
  private String email;
  private String path;

  @Before
  public void setUp() throws Exception {
    username = "username";
    token = "token";
    email = "email";
    path = String.format(URI_FORMAT, username);
  }

  @Test
  public void should_response_200_when_update_success() throws Exception {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("user-token", token);
    SecurityInfoUpdater updater = new SecurityInfoUpdater();
    updater.setEmail(email);
    HttpEntity<SecurityInfoUpdater> requestEntity = new HttpEntity<>(updater, httpHeaders);

    given(loginService.checkState(eq(username), eq(token))).willReturn(ONLINE);
    doNothing().when(userSecurityInfoService).updateInfo(any(SecurityInfoUpdater.class), eq(username));

    ResponseEntity<Message> entity =
        restTemplate.exchange(path, HttpMethod.PUT, requestEntity, Message.class);

    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody().getMsg(), is(HttpStatus.OK.name()));
  }

  @Test
  public void should_response_401_when_user_is_offline() throws Exception {
    given(loginService.checkState(eq(username), eq(token))).willReturn(UserState.OFFLINE);
    checkUnauthorized();
  }

  @Test
  public void should_response_401_when_token_not_match() throws Exception {
    given(loginService.checkState(eq(username), eq(token))).willReturn(UNAUTHORIZED);
    checkUnauthorized();
  }

  private void checkUnauthorized() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("user-token", token);
    SecurityInfoUpdater updater = new SecurityInfoUpdater();
    updater.setEmail(email);
    HttpEntity<SecurityInfoUpdater> requestEntity = new HttpEntity<>(updater, httpHeaders);

    ResponseEntity<Message> entity =
        restTemplate.exchange(path, HttpMethod.PUT, requestEntity, Message.class);

    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    assertThat(entity.getBody().getMsg(), is(HttpStatus.UNAUTHORIZED.name()));
  }
}