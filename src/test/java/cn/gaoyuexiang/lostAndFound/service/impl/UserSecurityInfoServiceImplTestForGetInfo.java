package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.LostAndFoundUserApplication;
import cn.gaoyuexiang.lostAndFound.dao.UserRepo;
import cn.gaoyuexiang.lostAndFound.model.dto.UserSecurityInfo;
import cn.gaoyuexiang.lostAndFound.model.persistence.User;
import cn.gaoyuexiang.lostAndFound.service.UserSecurityInfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LostAndFoundUserApplication.class)
public class UserSecurityInfoServiceImplTestForGetInfo {

  @Autowired
  private UserSecurityInfoService userSecurityInfoService;

  @MockBean
  private UserRepo userRepo;

  private String username;

  @Before
  public void setUp() throws Exception {
    username = "username";
  }

  @Test
  public void should_return_security_info_when_username_exist() throws Exception {
    String email = "email";
    User user = Mockito.mock(User.class);
    when(user.getUsername()).thenReturn(username);
    when(user.getEmail()).thenReturn(email);
    when(user.getEmailEnable()).thenReturn(true);
    when(userRepo.findByUsername(username)).thenReturn(user);

    UserSecurityInfo expectInfo = new UserSecurityInfo();
    expectInfo.setUsername(username);
    expectInfo.setEmail(email);
    expectInfo.setEmailEnable(true);

    UserSecurityInfo info = userSecurityInfoService.getInfo(username);

    assertThat(info, is(expectInfo));
  }

  @Test
  public void should_return_null_when_given_username_not_exist() throws Exception {
    when(userRepo.findByUsername(username)).thenReturn(null);
    UserSecurityInfo info = userSecurityInfoService.getInfo(username);
    assertNull(info);
  }
}