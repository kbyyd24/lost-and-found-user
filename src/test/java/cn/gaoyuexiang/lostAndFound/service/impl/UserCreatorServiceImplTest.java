package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.LostAndFoundUserApplication;
import cn.gaoyuexiang.lostAndFound.dao.UserRepo;
import cn.gaoyuexiang.lostAndFound.model.dto.SignInUser;
import cn.gaoyuexiang.lostAndFound.model.dto.enums.CreatorMsg;
import cn.gaoyuexiang.lostAndFound.model.persistence.User;
import cn.gaoyuexiang.lostAndFound.service.IdCreatorService;
import cn.gaoyuexiang.lostAndFound.service.PasswordService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LostAndFoundUserApplication.class)
public class UserCreatorServiceImplTest {

  @Autowired
  private UserCreatorServiceImpl userCreatorService;

  @MockBean
  private UserRepo userRepo;

  @MockBean
  private PasswordService passwordService;

  @MockBean
  private IdCreatorService idCreatorService;

  @Before
  @Ignore
  public void setUp() throws Exception {
    userCreatorService = new UserCreatorServiceImpl(userRepo, passwordService, idCreatorService);
  }

  @Test
  public void should_return_msg_not_enough_when_given_a_not_complete_user() throws Exception {
    SignInUser signInUser = Mockito.mock(SignInUser.class);
    when(signInUser.getEmail()).thenReturn(null);
    CreatorMsg creatorMsg = userCreatorService.create(signInUser);
    assertThat(creatorMsg, is(CreatorMsg.MSG_NOT_ENOUGH));
  }

  @Test
  public void should_return_username_exist_when_given_a_user_has_existed_username() throws Exception {
    SignInUser signInUser = new SignInUser();
    String username = "username";
    String email = "email";
    signInUser.setUsername(username);
    signInUser.setEmail(email);
    signInUser.setPassword("password");

    User user = Mockito.mock(User.class);
    when(user.getUsername()).thenReturn(username);
    when(user.getEmail()).thenReturn("email2");
    when(userRepo.findByUsernameOrEmail(username, email)).thenReturn(user);

    CreatorMsg creatorMsg = userCreatorService.create(signInUser);

    assertThat(creatorMsg, is(CreatorMsg.USERNAME_EXIST));
  }

  @Test
  public void should_return_email_exist_when_given_a_user_has_existed_email() throws Exception {
    SignInUser signInUser = new SignInUser();
    String username = "username";
    signInUser.setUsername(username);
    String email = "email";
    signInUser.setEmail(email);
    signInUser.setPassword("password");

    User user = new User();
    user.setUsername("username2");
    user.setEmail(email);
    when(userRepo.findByUsernameOrEmail(username, email)).thenReturn(user);

    CreatorMsg creatorMsg = userCreatorService.create(signInUser);

    assertThat(creatorMsg, is(CreatorMsg.EMAIL_EXIST));
  }

  @Test
  public void should_return_success_when_given_a_valid_user() throws Exception {
    SignInUser signInUser = new SignInUser();
    String username = "username";
    String email = "email";
    String password = "password";
    signInUser.setUsername(username);
    signInUser.setEmail(email);
    signInUser.setPassword(password);

    when(userRepo.findByUsernameOrEmail(username, email)).thenReturn(null);
    when(userRepo.save(any(User.class))).thenReturn(new User());

    CreatorMsg creatorMsg = userCreatorService.create(signInUser);

    assertThat(creatorMsg, is(CreatorMsg.SUCCESS));
  }

  @Test
  public void should_invoke_password_service_to_encode_password() throws Exception {
    SignInUser signInUser = new SignInUser();
    signInUser.setUsername("username");
    signInUser.setEmail("email");
    String password = "password";
    signInUser.setPassword(password);

    userCreatorService.create(signInUser);

    verify(passwordService).encode(password);
  }
}