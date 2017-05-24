package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.enums.UserState;
import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.model.dto.UserInfoDTO;
import cn.gaoyuexiang.lostAndFound.service.LoginService;
import cn.gaoyuexiang.lostAndFound.service.UserInfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static cn.gaoyuexiang.lostAndFound.enums.UserState.OFFLINE;
import static cn.gaoyuexiang.lostAndFound.enums.UserState.ONLINE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
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

  private String username;
  private String token;

  @Before
  public void setUp() throws Exception {
    username = "username";
    token = "token";
  }

  @Test
  public void should_return_200_when_create_success() throws Exception {
    String success = "OK";
    given(loginService.checkState(eq(username), eq(token))).willReturn(ONLINE);
    doNothing().when(userInfoService).createInfo(any(UserInfoDTO.class), eq(username));
    check(success, OK);
  }

  @Test
  public void should_return_401_when_user_is_offline() throws Exception {
    String unauthorized = "UNAUTHORIZED";
    given(loginService.checkState(eq(username), eq(token))).willReturn(UserState.UNAUTHORIZED);
    check(unauthorized, UNAUTHORIZED);
  }

  private void check(String message, HttpStatus status) {
    UserInfoDTO userInfoDTO = new UserInfoDTO();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(username, username);
    httpHeaders.add("user-token", token);
    HttpEntity<UserInfoDTO> putEntity = new HttpEntity<>(userInfoDTO, httpHeaders);
    ResponseEntity<Message> entity =
        restTemplate.exchange("/user/info/" + username, HttpMethod.PUT, putEntity, Message.class);
    assertThat(entity.getStatusCode(), is(status));
    assertThat(entity.getBody().getMsg(), is(message));
  }

}