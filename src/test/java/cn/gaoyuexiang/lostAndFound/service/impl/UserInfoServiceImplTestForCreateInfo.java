package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.dao.UserInfoRepo;
import cn.gaoyuexiang.lostAndFound.model.dto.UserInfoDTO;
import cn.gaoyuexiang.lostAndFound.model.persistence.UserInfo;
import cn.gaoyuexiang.lostAndFound.service.IdCreatorService;
import cn.gaoyuexiang.lostAndFound.service.UserInfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserInfoServiceImplTestForCreateInfo {

  private UserInfoService userInfoService;

  @Mock
  private UserInfoRepo userInfoRepo;

  @Before
  public void setUp() throws Exception {
    userInfoService = new UserInfoServiceImpl(userInfoRepo);
  }

  @Test
  public void should_return_success_when_create_success() throws Exception {
    UserInfoDTO userInfoDTO = new UserInfoDTO();
    when(userInfoRepo.save(any(UserInfo.class))).thenReturn(new UserInfo());
    String success = userInfoService.createInfo(userInfoDTO, "username");
    assertThat(success, is("success"));
  }
}