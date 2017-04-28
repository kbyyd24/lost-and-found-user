package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.annotation.UserController;
import cn.gaoyuexiang.lostAndFound.enums.UserState;
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
      return new ResponseEntity<>(new Message(userState.name()), HttpStatus.UNAUTHORIZED);
    } else if (userState.equals(UserState.OFFLINE)) {
      return new ResponseEntity<>(new Message(userState.name()), HttpStatus.NOT_FOUND);
    }
    String info = userInfoService.createInfo(userInfo, username);
    return new ResponseEntity<>(new Message(info), HttpStatus.OK);
  }

  @GetMapping(path = "info/{username}")
  public ResponseEntity<?> getInfo(@PathVariable("username") String username) {
    UserInfoDTO info = userInfoService.getInfo(username);
    if (info == null) {
      return new ResponseEntity<>(new Message("not found"), HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(info, HttpStatus.OK);
  }
}
