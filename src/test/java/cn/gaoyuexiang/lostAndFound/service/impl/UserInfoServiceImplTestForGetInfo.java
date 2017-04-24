package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.dao.UserInfoRepo;
import cn.gaoyuexiang.lostAndFound.model.dto.UserInfoDTO;
import cn.gaoyuexiang.lostAndFound.model.persistence.UserInfo;
import cn.gaoyuexiang.lostAndFound.service.UserInfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserInfoServiceImplTestForGetInfo {

  private UserInfoService userInfoService;

  @Mock
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
}