package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.enums.UserState;
import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.model.dto.UserSecurityInfo;
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

import static cn.gaoyuexiang.lostAndFound.enums.UserState.OFFLINE;
import static cn.gaoyuexiang.lostAndFound.enums.UserState.ONLINE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserSecurityInfoControllerTestForGetInfo {

  private static final String URL_FORMAT = "/user/info/%s/security";
  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private UserSecurityInfoService securityInfoService;
  
  @MockBean
  private LoginService loginService;

  @Test
  public void should_return_200_when_get_info_success() throws Exception {
    String username = "username";
    String token = "token";
    String path = String.format(URL_FORMAT, username);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("user-token", token);
    HttpEntity<Object> requestEntity = new HttpEntity<>(httpHeaders);
    UserSecurityInfo expectInfo = new UserSecurityInfo();
    
    given(loginService.checkState(eq(username), eq(token))).willReturn(ONLINE);
    given(securityInfoService.getInfo(eq(username))).willReturn(expectInfo);
    
    ResponseEntity<UserSecurityInfo> entity = 
        restTemplate.exchange(path, HttpMethod.GET, requestEntity, UserSecurityInfo.class);
    
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(expectInfo));
  }

  @Test
  public void should_return_401_when_user_is_offline() throws Exception {
    String username = "username";
    String token = "token";
    String path = String.format(URL_FORMAT, username);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("user-token", token);
    HttpEntity<Object> requestEntity = new HttpEntity<>(httpHeaders);

    given(loginService.checkState(eq(username), eq(token))).willReturn(OFFLINE);

    ResponseEntity<Message> entity =
        restTemplate.exchange(path, HttpMethod.GET, requestEntity, Message.class);

    assertThat(entity.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    assertThat(entity.getBody().getMsg(), is("unauthorized"));
  }
}