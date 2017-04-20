package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.dao.UserRepo;
import cn.gaoyuexiang.lostAndFound.model.dto.SignInUser;
import cn.gaoyuexiang.lostAndFound.model.dto.enums.CreatorMsg;
import cn.gaoyuexiang.lostAndFound.model.persistence.User;
import cn.gaoyuexiang.lostAndFound.service.PasswordService;
import cn.gaoyuexiang.lostAndFound.service.UserCreatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cn.gaoyuexiang.lostAndFound.model.dto.enums.CreatorMsg.*;

@Service
public class UserCreatorServiceImpl implements UserCreatorService {

  private UserRepo userRepo;
  private PasswordService passwordService;

  @Autowired
  public UserCreatorServiceImpl(UserRepo userRepo, PasswordService passwordService) {
    this.userRepo = userRepo;
    this.passwordService = passwordService;
  }

  @Override
  public CreatorMsg create(SignInUser signInUser) {
    if (!isComplete(signInUser)) {
      return MSG_NOT_ENOUGH;
    }
    CreatorMsg x = checkExistUser(signInUser);
    if (x != null) {
      return x;
    }
    User user = buildUser(signInUser);
    userRepo.save(user);
    return SUCCESS;
  }

  private User buildUser(SignInUser signInUser) {
    User user = new User();
    user.setUsername(signInUser.getUsername());
    user.setEmail(signInUser.getEmail());
    user.setEmailEnable(false);
    user.setPassword(passwordService.encode(signInUser.getPassword()));
    return user;
  }

  private CreatorMsg checkExistUser(SignInUser signInUser) {
    User existUser = userRepo.findByUsernameOrEmail(signInUser.getUsername(), signInUser.getEmail());
    if (existUser != null) {
      if (existUser.getUsername().equals(signInUser.getUsername())) {
        return USERNAME_EXIST;
      }
      return EMAIL_EXIST;
    }
    return null;
  }

  private boolean isComplete(SignInUser signInUser) {
    return signInUser != null &&
        signInUser.getUsername() != null &&
        signInUser.getEmail() != null &&
        signInUser.getPassword() != null;
  }
}