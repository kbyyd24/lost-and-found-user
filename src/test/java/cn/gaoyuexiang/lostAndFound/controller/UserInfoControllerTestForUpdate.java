package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.model.dto.UserInfoDTO;
import cn.gaoyuexiang.lostAndFound.service.LoginService;
import cn.gaoyuexiang.lostAndFound.service.UserInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserInfoControllerTestForUpdate {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private UserInfoService userInfoService;

  @MockBean
  private LoginService loginService;

  @Test
  public void should_return_200_when_create_success() throws Exception {
    String success = "success";
    String username = "username";
    String token = "token";
    String online = "online";
    given(loginService.checkState(eq(username), eq(token))).willReturn(online);
    given(userInfoService.createInfo(any(UserInfoDTO.class), anyString())).willReturn(success);
    check(username, token, success, OK);
  }

  @Test
  public void should_return_401_when_user_is_offline() throws Exception {
    String offline = "offline";
    String username = "username";
    String token = "token";
    String unauthorized = "unauthorized";
    given(loginService.checkState(eq(username), eq(token))).willReturn(offline);
    check(username, token, unauthorized, UNAUTHORIZED);
  }

  private void check(String username, String token, String message, HttpStatus status) {
    UserInfoDTO userInfoDTO = new UserInfoDTO();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(username, username);
    httpHeaders.add("user-token", token);
    HttpEntity<UserInfoDTO> postEntity = new HttpEntity<>(userInfoDTO, httpHeaders);
    ResponseEntity<Message> entity =
        restTemplate.exchange(String.format("/user/info/%s", username), HttpMethod.PUT, postEntity, Message.class);
    assertThat(entity.getStatusCode(), is(status));
    assertThat(entity.getBody().getMsg(), is(message));
  }

}