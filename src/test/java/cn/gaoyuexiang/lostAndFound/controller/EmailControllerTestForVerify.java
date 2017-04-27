package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.model.dto.EmailToken;
import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.service.EmailVerificationService;
import org.junit.Before;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.GONE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmailControllerTestForVerify {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private EmailVerificationService service;

  private static String PATH = "/user/email/email/verification";
  private String email;
  private String username;
  private String userToken;
  private String token;
  private EmailToken emailToken;

  @Before
  public void setUp() throws Exception {
    email = "email";
    username = "username";
    userToken = "userToken";
    token = "token";
    emailToken = new EmailToken();
    emailToken.setToken(token);
  }

  @Test
  public void should_return_200_when_verify_success() throws Exception {
    given(service.verify(eq(username), eq(userToken), eq(email), eq(token))).willReturn("success");
    ResponseEntity<Message> entity = getMessageResponseEntity();
    assertThat(entity.getStatusCode(), is(OK));
  }

  @Test
  public void should_return_401_when_verify_unauthorized() throws Exception {
    given(service.verify(eq(username), eq(userToken), eq(email), eq(token))).willReturn("unauthorized");
    ResponseEntity<Message> entity = getMessageResponseEntity();
    assertThat(entity.getStatusCode(), is(UNAUTHORIZED));
  }

  @Test
  public void should_return_410_when_email_token_expired_time() throws Exception {
    given(service.verify(eq(username), eq(userToken), eq(email), eq(token))).willReturn("timeout");
    ResponseEntity<Message> entity = getMessageResponseEntity();
    assertThat(entity.getStatusCode(), is(GONE));
  }

  private ResponseEntity<Message> getMessageResponseEntity() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("user-token", userToken);
    httpHeaders.add(username, username);
    HttpEntity<EmailToken> requestEntity = new HttpEntity<>(emailToken, httpHeaders);
    return restTemplate.exchange(PATH, PUT, requestEntity, Message.class);
  }
}