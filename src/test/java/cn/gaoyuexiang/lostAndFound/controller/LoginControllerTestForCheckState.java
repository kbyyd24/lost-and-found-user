package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.enums.UserState;
import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.service.LoginService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.springframework.http.HttpMethod.GET;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTestForCheckState {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private LoginService loginService;

  @Test
  public void should_response_200_when_user_is_online() throws Exception {
    String online = "ONLINE";
    String token = "token";
    String username = "username";
    given(loginService.checkState(eq(username), eq(token))).willReturn(UserState.ONLINE);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("user-token", token);
    HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
    ResponseEntity<Message> responseEntity =
        restTemplate.exchange(String.format("/user/login/%s", username),
            GET,
            httpEntity,
            Message.class);
    assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
    assertThat(responseEntity.getBody().getMsg(), is(online));
  }

  @Test
  public void should_response_401_when_token_is_not_match() throws Exception {
    String unauthorized = "UNAUTHORIZED";
    String token = "token";
    String username = "username";
    given(loginService.checkState(eq(username), eq(token))).willReturn(UserState.UNAUTHORIZED);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("user-token", token);
    HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
    ResponseEntity<Message> entity =
        restTemplate.exchange(String.format("/user/login/%s", username),
            GET,
            httpEntity,
            Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    assertThat(entity.getBody().getMsg(), is(unauthorized));
  }

  @Test
  public void should_response_404_when_user_is_offline() throws Exception {
    String offline = "OFFLINE";
    String token = "token";
    String username = "username";
    given(loginService.checkState(eq(username), eq(token))).willReturn(UserState.OFFLINE);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("user-token", token);
    HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
    ResponseEntity<Message> entity =
        restTemplate.exchange(String.format("/user/login/%s", username),
            GET,
            httpEntity,
            Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
    assertThat(entity.getBody().getMsg(), is(offline));
  }
}