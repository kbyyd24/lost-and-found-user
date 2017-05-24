package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.enums.EmailVerifyMsg;
import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.service.EmailVerificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmailControllerTestForApply {

  private static final String URI_FORMAT = "/user/email/%s/verification/application";
  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private EmailVerificationService emailVerificationService;

  @Test
  public void should_return_200_when_apply_success() throws Exception {
    String token = "token";
    String email = "email";
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("user-token", token);
    String path = String.format(URI_FORMAT, email);
    given(emailVerificationService.apply(eq(email), eq(token))).willReturn(EmailVerifyMsg.SUCCESS);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, new HttpEntity<>(httpHeaders), Message.class);
    assertThat(entity.getStatusCode(), is(OK));
  }

  @Test
  public void should_return_404_when_token_missed() throws Exception {
    String email = "email";
    String path = String.format(URI_FORMAT, email);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, new HttpEntity<>(new HttpHeaders()), Message.class);
    assertThat(entity.getStatusCode(), is(NOT_FOUND));
  }

  @Test
  public void should_return_401_when_token_not_match() throws Exception {
    String email = "email";
    String token = "token";
    given(emailVerificationService.apply(eq(email), eq(token))).willReturn(EmailVerifyMsg.UNAUTHORIZED);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("user-token", token);
    String path = String.format(URI_FORMAT, email);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, new HttpEntity<>(httpHeaders), Message.class);
    assertThat(entity.getStatusCode(), is(UNAUTHORIZED));
  }

  @Test
  public void should_return_404_when_email_not_found() throws Exception {
    String email = "email";
    String token = "token";
    given(emailVerificationService.apply(eq(email), eq(token))).willReturn(EmailVerifyMsg.EMAIL_NOT_FOUND);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("user-token", token);
    String path = String.format(URI_FORMAT, email);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, new HttpEntity<>(httpHeaders), Message.class);
    assertThat(entity.getStatusCode(), is(NOT_FOUND));
  }

  @Test
  public void should_return_409_when_email_enabled() throws Exception {
    String email = "email";
    String token = "token";
    given(emailVerificationService.apply(eq(email), eq(token))).willReturn(EmailVerifyMsg.EMAIL_ENABLED);
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("user-token", token);
    String path = String.format(URI_FORMAT, email);
    ResponseEntity<Message> entity =
        restTemplate.exchange(path, PUT, new HttpEntity<>(httpHeaders), Message.class);
    assertThat(entity.getStatusCode(), is(CONFLICT));
  }
}