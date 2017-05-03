package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.annotation.UserController;
import cn.gaoyuexiang.lostAndFound.enums.UserState;
import cn.gaoyuexiang.lostAndFound.exception.OfflineException;
import cn.gaoyuexiang.lostAndFound.exception.UnauthorizedException;
import cn.gaoyuexiang.lostAndFound.exception.UserNotExistException;
import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.model.dto.UserInfoDTO;
import cn.gaoyuexiang.lostAndFound.service.LoginService;
import cn.gaoyuexiang.lostAndFound.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@UserController
public class UserInfoController {

  private LoginService loginService;

  private UserInfoService userInfoService;

  @Autowired
  public UserInfoController(LoginService loginService, UserInfoService userInfoService) {
    this.loginService = loginService;
    this.userInfoService = userInfoService;
  }

  @PutMapping(path = "info/{username}", headers = "user-token")
  public ResponseEntity<Message> updateInfo(@RequestBody UserInfoDTO userInfoDTO,
                                            @PathVariable("username") String username,
                                            @RequestHeader("user-token") String token) {
    return this.addInfo(userInfoDTO, username, token);
  }
  @PostMapping(path = "info", headers = {"username", "user-token"})
  public ResponseEntity<Message> addInfo(@RequestBody UserInfoDTO userInfo,
                                         @RequestHeader("username") String username,
                                         @RequestHeader("user-token") String token) {
    UserState userState = loginService.checkState(username, token);
    if (userState.equals(UserState.UNAUTHORIZED)) {
      throw new UnauthorizedException();
    } else if (userState.equals(UserState.OFFLINE)) {
      throw new OfflineException();
    }
    userInfoService.createInfo(userInfo, username);
    return new ResponseEntity<>(new Message(HttpStatus.OK.name()), HttpStatus.OK);
  }

  @GetMapping(path = "info/{username}")
  @ResponseStatus(value = HttpStatus.OK)
  public UserInfoDTO getInfo(@PathVariable("username") String username) {
    return userInfoService.getInfo(username);
  }

  @ExceptionHandler(UnauthorizedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public Message handleUnauthorized() {
    return new Message(UserState.UNAUTHORIZED.name());
  }

  @ExceptionHandler(OfflineException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Message handleOffline() {
    return new Message(UserState.OFFLINE.name());
  }

  @ExceptionHandler(value = UserNotExistException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public Message handleUserNotExist() {
    return new Message(HttpStatus.NOT_FOUND.name());
  }
}
