package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.dao.UserRepo;
import cn.gaoyuexiang.lostAndFound.model.dto.UserSecurityInfo;
import cn.gaoyuexiang.lostAndFound.model.persistence.User;
import cn.gaoyuexiang.lostAndFound.service.UserSecurityInfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserSecurityInfoServiceImplTestForGetInfo {

  private UserSecurityInfoService userSecurityInfoService;

  @Mock
  private UserRepo userRepo;

  @Before
  public void setUp() throws Exception {
    userSecurityInfoService = new UserSecurityInfoServiceImpl(userRepo);
  }

  @Test
  public void should_return_security_info_when_username_exist() throws Exception {
    String username = "username";
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
    String username = "username";
    when(userRepo.findByUsername(username)).thenReturn(null);
    UserSecurityInfo info = userSecurityInfoService.getInfo(username);
    assertNull(info);
  }
}