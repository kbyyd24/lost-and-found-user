package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.model.dto.SignInUser;
import cn.gaoyuexiang.lostAndFound.model.dto.enums.CreatorMsg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignInControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void should_return_200_when_given_a_valid_user() throws Exception {
    SignInUser signInUser = new SignInUser();
    signInUser.setUsername("username");
    signInUser.setEmail("email");
    signInUser.setPassword("password");
    ResponseEntity<CreatorMsg> entity = restTemplate.postForEntity("/user", signInUser, CreatorMsg.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(CreatorMsg.SUCCESS));
  }

  @Test
  public void should_return_400_when_given_a_user_miss_some_msg() throws Exception {
    SignInUser signInUser = new SignInUser();
    ResponseEntity<CreatorMsg> entity = restTemplate.postForEntity("/user", signInUser, CreatorMsg.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    assertThat(entity.getBody(), is(CreatorMsg.MSG_NOT_ENOUGH));
  }

  @Test
  public void should_return_409_when_given_a_user_has_existed_username() throws Exception {
    SignInUser signInUser = new SignInUser();
    signInUser.setUsername("username");
    signInUser.setEmail("email");
    signInUser.setPassword("password");
    ResponseEntity<CreatorMsg> entity = restTemplate.postForEntity("/user", signInUser, CreatorMsg.class);
    entity = restTemplate.postForEntity("/user", signInUser, CreatorMsg.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.CONFLICT));
    assertThat(entity.getBody(), is(CreatorMsg.USERNAME_EXIST));
  }
}