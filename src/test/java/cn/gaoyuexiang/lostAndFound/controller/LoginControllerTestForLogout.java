package cn.gaoyuexiang.lostAndFound.controller;

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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.springframework.http.HttpMethod.DELETE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTestForLogout {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private LoginService loginService;

  @Test
  public void should_response_200_when_logout_success() throws Exception {
    String logoutSuccess = "logout success";
    check(logoutSuccess, HttpStatus.OK);
  }

  @Test
  public void should_response_401_when_token_not_match() throws Exception {
    String unauthorized = "unauthorized";
    check(unauthorized, HttpStatus.UNAUTHORIZED);
  }

  @Test
  public void should_response_404_when_user_is_not_online() throws Exception {
    String userNotFound = "user not found";
    check(userNotFound, HttpStatus.NOT_FOUND);
  }

  private void check(String msg, HttpStatus status) {
    String username = "username";
    String token = "token";
    given(loginService.logout(eq(username), eq(token))).willReturn(msg);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("user-token", token);
    ResponseEntity<Message> entity =
        restTemplate.exchange(String.format("/user/login/%s", username),
            DELETE,
            new HttpEntity<>(httpHeaders),
            Message.class);
    assertThat(entity.getStatusCode(), is(status));
    assertThat(entity.getBody().getMsg(), is(msg));
  }

}