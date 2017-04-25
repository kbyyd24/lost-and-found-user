package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.model.dto.UserInfoDTO;
import cn.gaoyuexiang.lostAndFound.service.UserInfoService;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserInfoControllerTestForGetInfo {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private UserInfoService userInfoService;

  @Test
  public void should_return_info_when_username_is_exist() throws Exception {
    String username = "username";
    UserInfoDTO userInfo = new UserInfoDTO();
    given(userInfoService.getInfo(username)).willReturn(userInfo);
    ResponseEntity<UserInfoDTO> entity =
        restTemplate.getForEntity(String.format("/user/info/%s", username), UserInfoDTO.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.OK));
    assertThat(entity.getBody(), is(userInfo));
  }

  @Test
  public void should_return_404_when_username_is_not_found() throws Exception {
    String username = "username";
    given(userInfoService.getInfo(username)).willReturn(null);
    ResponseEntity<Message> entity =
        restTemplate.getForEntity(String.format("/user/info/%s", username), Message.class);
    assertThat(entity.getStatusCode(), is(HttpStatus.NOT_FOUND));
    assertThat(entity.getBody().getMsg(), is("not found"));
  }
}