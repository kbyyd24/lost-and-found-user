package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.annotation.UserController;
import cn.gaoyuexiang.lostAndFound.enums.UserState;
import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.model.dto.SecurityInfoUpdater;
import cn.gaoyuexiang.lostAndFound.model.dto.UserSecurityInfo;
import cn.gaoyuexiang.lostAndFound.service.LoginService;
import cn.gaoyuexiang.lostAndFound.service.UserSecurityInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static cn.gaoyuexiang.lostAndFound.enums.UserState.OFFLINE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@UserController
public class UserSecurityInfoController {

  private final UserSecurityInfoService userSecurityInfoService;
  private final LoginService loginService;

  @Autowired
  public UserSecurityInfoController(UserSecurityInfoService userSecurityInfoService,
                                    LoginService loginService) {
    this.userSecurityInfoService = userSecurityInfoService;
    this.loginService = loginService;
  }

  @GetMapping(path = "info/{username}/security", headers = "user-token")
  public ResponseEntity<?> getInfo(@PathVariable("username") String username,
                                   @RequestHeader("user-token") String token) {
    UserState userState = loginService.checkState(username, token);
    if (userState.equals(OFFLINE)) {
      return new ResponseEntity<>(new Message("unauthorized"), UNAUTHORIZED);
    }
    UserSecurityInfo info = userSecurityInfoService.getInfo(username);
    return new ResponseEntity<>(info, HttpStatus.OK);
  }

  @PutMapping(path = "info/{username}/security", headers = "user-token")
  public ResponseEntity<Message> updateInfo(@RequestBody SecurityInfoUpdater updater,
                                      @PathVariable("username") String username,
                                      @RequestHeader("user-token") String token) {
    UserState userState = loginService.checkState(username, token);
    if (userState.equals(OFFLINE)) {
      return new ResponseEntity<>(new Message("unauthorized"), UNAUTHORIZED);
    }
    String result = userSecurityInfoService.updateInfo(updater, username);
    return new ResponseEntity<>(new Message(result), OK);
  }

}
