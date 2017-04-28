package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.enums.UserState;
import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.model.dto.SecurityInfoUpdater;
import cn.gaoyuexiang.lostAndFound.service.LoginService;
import cn.gaoyuexiang.lostAndFound.service.UserSecurityInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static cn.gaoyuexiang.lostAndFound.enums.UserState.ONLINE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserSecurityInfoControllerTestForUpdateInfo {

  public static final String URI_FORMAT = "/user/info/%s/security";
  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private UserSecurityInfoService userSecurityInfoService;

  @MockBean
  private LoginService loginService;

  @Test
  public void should_response_200_when_update_success() throws Exception {
    String username = "username";
    String path = String.format(URI_FORMAT, username);
    String token = "token";
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("user-token", token);
    String email = "email";
    SecurityInfoUpdater updater = new SecurityInfoUpdater();
    updater.setEmail(email);
    HttpEntity<SecurityInfoUpdater> requestEntity = new HttpEntity<>(updater, httpHeaders);

    String updateState = "success";
    given(loginService.checkState(eq(username), eq(token))).willReturn(ONLINE);
    given(userSecurityInfoService.updateInfo(any(SecurityInfoUpdater.class), eq(username)))
        .willReturn(updateState);

    ResponseEntity<Message> entity =
        restTemplate.exchange(path, HttpMethod.PUT, requestEntity, Message.class);

    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody().getMsg(), is(updateState));
  }

  @Test
  public void should_response_401_when_user_is_offline() throws Exception {
    String username = "username";
    String path = String.format(URI_FORMAT, username);
    String token = "token";
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("user-token", token);
    String email = "email";
    SecurityInfoUpdater updater = new SecurityInfoUpdater();
    updater.setEmail(email);
    HttpEntity<SecurityInfoUpdater> requestEntity = new HttpEntity<>(updater, httpHeaders);

    given(loginService.checkState(eq(username), eq(token))).willReturn(UserState.OFFLINE);

    ResponseEntity<Message> entity =
        restTemplate.exchange(path, HttpMethod.PUT, requestEntity, Message.class);

    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    assertThat(entity.getBody().getMsg(), is("unauthorized"));
  }
}