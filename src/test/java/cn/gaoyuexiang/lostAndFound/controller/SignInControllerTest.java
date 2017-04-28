package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.model.dto.SignInUser;
import cn.gaoyuexiang.lostAndFound.enums.CreatorMsg;
import cn.gaoyuexiang.lostAndFound.service.UserCreatorService;
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
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignInControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private UserCreatorService userCreatorService;

  @Test
  public void should_return_200_when_given_a_valid_user() throws Exception {
    given(userCreatorService.create(any(SignInUser.class))).willReturn(CreatorMsg.SUCCESS);
    SignInUser signInUser = new SignInUser();
    ResponseEntity<Message> entity = restTemplate.postForEntity("/user", signInUser, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody().getMsg(), is(CreatorMsg.SUCCESS.getMsg()));
  }

  @Test
  public void should_return_400_when_given_a_user_miss_some_msg() throws Exception {
    given(userCreatorService.create(any(SignInUser.class))).willReturn(CreatorMsg.MSG_NOT_ENOUGH);
    SignInUser signInUser = new SignInUser();
    ResponseEntity<Message> entity = restTemplate.postForEntity("/user", signInUser, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    assertThat(entity.getBody().getMsg(), is(CreatorMsg.MSG_NOT_ENOUGH.getMsg()));
  }

  @Test
  public void should_return_409_when_given_a_user_has_existed_username() throws Exception {
    given(userCreatorService.create(any(SignInUser.class))).willReturn(CreatorMsg.USERNAME_EXIST);
    SignInUser signInUser = new SignInUser();
    ResponseEntity<Message> entity = restTemplate.postForEntity("/user", signInUser, Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.CONFLICT));
    assertThat(entity.getBody().getMsg(), is(CreatorMsg.USERNAME_EXIST.getMsg()));
  }
}