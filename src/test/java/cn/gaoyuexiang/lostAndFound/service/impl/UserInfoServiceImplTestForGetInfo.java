package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.LostAndFoundUserApplication;
import cn.gaoyuexiang.lostAndFound.dao.UserInfoRepo;
import cn.gaoyuexiang.lostAndFound.exception.UserNotExistException;
import cn.gaoyuexiang.lostAndFound.model.dto.UserInfoDTO;
import cn.gaoyuexiang.lostAndFound.model.persistence.UserInfo;
import cn.gaoyuexiang.lostAndFound.service.UserInfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LostAndFoundUserApplication.class)
public class UserInfoServiceImplTestForGetInfo {

  @Autowired
  private UserInfoService userInfoService;

  @MockBean
  private UserInfoRepo userInfoRepo;

  @Before
  public void setUp() throws Exception {
    userInfoService = new UserInfoServiceImpl(userInfoRepo);
  }

  @Test
  public void should_return_info_when_given_a_existed_username() throws Exception {
    String username = "username";
    String nickName = "nick name";
    String about = "about";
    String blog = "blog";
    String weiboName = "weibo name";
    UserInfoDTO expectInfo = new UserInfoDTO();
    expectInfo.setNickName(nickName);
    expectInfo.setAbout(about);
    expectInfo.setBlog(blog);
    expectInfo.setWeiboName(weiboName);
    UserInfo userInfo = mock(UserInfo.class);
    when(userInfo.getNickName()).thenReturn(nickName);
    when(userInfo.getAbout()).thenReturn(about);
    when(userInfo.getWeiboName()).thenReturn(weiboName);
    when(userInfo.getBlog()).thenReturn(blog);
    when(userInfoRepo.findByUsername(username)).thenReturn(userInfo);
    UserInfoDTO info = userInfoService.getInfo(username);
    assertThat(info, is(expectInfo));
  }

  @Test(expected = UserNotExistException.class)
  public void should_throw_UserNotExistException_when_username_not_exist() throws Exception {
    String username = "username";
    when(userInfoRepo.findByUsername(username)).thenReturn(null);
    userInfoService.getInfo(username);
  }
}