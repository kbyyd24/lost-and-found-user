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
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LostAndFoundUserApplication.class)
public class UserSecurityInfoServiceImplTestForUpdateInfo {

  @Autowired
  private UserSecurityInfoService userSecurityInfoService;
  
  @MockBean
  private UserRepo userRepo;

  private String username;
  private String email;

  @Before
  public void setUp() throws Exception {
    username = "username";
    email = "email";
  }

  @Test
  public void should_update_user_when_email_changed() throws Exception {
    User user = mock(User.class);
    when(user.getEmail()).thenReturn("email2");
    when(userRepo.findByUsername(username)).thenReturn(user);
    when(userRepo.save(user)).thenReturn(user);
    SecurityInfoUpdater updater = new SecurityInfoUpdater();
    updater.setEmail(email);

    userSecurityInfoService.updateInfo(updater, username);
    verify(userRepo).findByUsername(username);
    verify(user).getEmail();
    verify(userRepo).save(user);
  }

  @Test
  public void should_not_update_when_email_not_changed() throws Exception {
    User user = mock(User.class);
    when(user.getEmail()).thenReturn(email);
    when(userRepo.findByUsername(username)).thenReturn(user);
    SecurityInfoUpdater updater = new SecurityInfoUpdater();
    updater.setEmail(email);

    userSecurityInfoService.updateInfo(updater, username);

    verify(userRepo).findByUsername(username);
    verify(user).getEmail();
    verify(userRepo, never()).save(user);
  }
}