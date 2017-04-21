package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.dao.LoginRepo;
import cn.gaoyuexiang.lostAndFound.dao.UserRepo;
import cn.gaoyuexiang.lostAndFound.exception.MissParamException;
import cn.gaoyuexiang.lostAndFound.exception.PasswordNotMatchException;
import cn.gaoyuexiang.lostAndFound.exception.UserNotExistException;
import cn.gaoyuexiang.lostAndFound.model.dto.LoginToken;
import cn.gaoyuexiang.lostAndFound.model.dto.LoginUser;
import cn.gaoyuexiang.lostAndFound.model.persistence.Login;
import cn.gaoyuexiang.lostAndFound.model.persistence.User;
import cn.gaoyuexiang.lostAndFound.service.IdCreatorService;
import cn.gaoyuexiang.lostAndFound.service.LoginService;
import cn.gaoyuexiang.lostAndFound.service.PasswordService;
import cn.gaoyuexiang.lostAndFound.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.function.Function;

@Service
public class LoginServiceImpl implements LoginService {

  private final LoginRepo loginRepo;
  private final UserRepo userRepo;
  private final PasswordService passwordService;
  private final TokenService tokenService;
  private IdCreatorService idCreatorService;

  @Autowired
  public LoginServiceImpl(LoginRepo loginRepo,
                          UserRepo userRepo,
                          PasswordService passwordService,
                          TokenService tokenService,
                          IdCreatorService idCreatorService) {
    this.loginRepo = loginRepo;
    this.userRepo = userRepo;
    this.passwordService = passwordService;
    this.tokenService = tokenService;
    this.idCreatorService = idCreatorService;
  }

  @Override
  public LoginToken login(LoginUser loginUser) {
    this.checkComplete(loginUser);
    User user = this.loadUserByName(loginUser.getLoginName());
    checkPassword(loginUser.getPassword(), user.getPassword());
    String username = user.getUsername();
    Login onlineUser = loginRepo.findByUsername(username);
    long loginTime = System.currentTimeMillis();
    String token;
    if (onlineUser == null) {
      token = saveOnlineUser(username, loginTime);
    } else {
      token = updateOnlineUser(onlineUser, loginTime);
    }
    return new LoginToken(token);
  }

  private void checkPassword(String loginPassword, String savedPassword) {
    boolean isMatched = passwordService.match(loginPassword, savedPassword);
    if (!isMatched) {
      throw new PasswordNotMatchException();
    }
  }

  private String updateOnlineUser(Login onlineUser, long loginTime) {
    String token;
    onlineUser.setLoginTime(loginTime);
    token = onlineUser.getToken();
    loginRepo.save(onlineUser);
    return token;
  }

  private String saveOnlineUser(String username, long loginTime) {
    String token = tokenService.buildToken();
    String id = idCreatorService.create();
    Login login = new Login();
    login.setId(id);
    login.setUsername(username);
    login.setToken(passwordService.encode(token));
    login.setLoginTime(loginTime);
    loginRepo.save(login);
    return token;
  }

  private void checkComplete(LoginUser loginUser) {
    if (loginUser == null || loginUser.getLoginName() == null || loginUser.getPassword() == null) {
      throw new MissParamException();
    }
  }

  private User loadUserByName(String loginName) {
    User user = userRepo.findByUsernameOrEmail(loginName, loginName);
    if (user == null) {
      throw new UserNotExistException();
    }
    return user;
  }

  @Override
  public String checkState(String username, String token) {
    Login login = loginRepo.findByUsername(username);
    if (login == null) {
      return "offline";
    }
    boolean isMatch = passwordService.match(token, login.getToken());
    if (!isMatch) {
      return "unauthorized";
    }
    return "online";
  }

  @Override
  public String logout(String username, String token) {
    Login login = loginRepo.findByUsername(username);
    if (login == null) {
      return "user not found";
    }
    boolean isMatch = passwordService.match(token, login.getToken());
    if (!isMatch) {
      return "unauthorized";
    }
    loginRepo.delete(login);
    return "logout success";
  }

}
