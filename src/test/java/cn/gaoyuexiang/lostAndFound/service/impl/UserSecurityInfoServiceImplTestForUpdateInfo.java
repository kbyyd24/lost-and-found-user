package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.LostAndFoundUserApplication;
import cn.gaoyuexiang.lostAndFound.dao.UserRepo;
import cn.gaoyuexiang.lostAndFound.model.dto.SecurityInfoUpdater;
import cn.gaoyuexiang.lostAndFound.model.persistence.User;
import cn.gaoyuexiang.lostAndFound.service.UserSecurityInfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LostAndFoundUserApplication.class)
public class UserSecurityInfoServiceImplTestForUpdateInfo {

  @Autowired
  private UserSecurityInfoService userSecurityInfoService;
  
  @MockBean
  private UserRepo userRepo;

  @Before
  public void setUp() throws Exception {
    userSecurityInfoService = new UserSecurityInfoServiceImpl(userRepo);
  }

  @Test
  public void should_return_success_when_update_success() throws Exception {
    String username = "username";
    String email = "email";
    User user = mock(User.class);
    when(userRepo.findByUsername(username)).thenReturn(user);
    when(userRepo.save(user)).thenReturn(user);
    SecurityInfoUpdater updater = new SecurityInfoUpdater();
    updater.setEmail(email);

    String success = userSecurityInfoService.updateInfo(updater, username);
    assertThat(success, is("success"));
  }
}