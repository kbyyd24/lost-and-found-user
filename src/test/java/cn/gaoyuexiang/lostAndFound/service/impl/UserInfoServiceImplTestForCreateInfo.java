package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.LostAndFoundUserApplication;
import cn.gaoyuexiang.lostAndFound.dao.UserInfoRepo;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LostAndFoundUserApplication.class)
public class UserInfoServiceImplTestForCreateInfo {

  @Autowired
  private UserInfoService userInfoService;

  @MockBean
  private UserInfoRepo userInfoRepo;

  @Before
  public void setUp() throws Exception {
    userInfoService = new UserInfoServiceImpl(userInfoRepo);
  }

  @Test
  public void should_return_success_when_create_success() throws Exception {
    UserInfoDTO userInfoDTO = new UserInfoDTO();
    when(userInfoRepo.save(any(UserInfo.class))).thenReturn(new UserInfo());
    userInfoService.createInfo(userInfoDTO, "username");
    verify(userInfoRepo).save(any(UserInfo.class));
  }
}