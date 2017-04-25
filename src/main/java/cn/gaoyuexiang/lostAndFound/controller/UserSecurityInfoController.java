package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.annotation.UserController;
import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.model.dto.UserSecurityInfo;
import cn.gaoyuexiang.lostAndFound.service.LoginService;
import cn.gaoyuexiang.lostAndFound.service.UserSecurityInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

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
    String userState = loginService.checkState(username, token);
    if (userState.equals("offline")) {
      return new ResponseEntity<>(new Message("unauthorized"), HttpStatus.UNAUTHORIZED);
    }
    UserSecurityInfo info = userSecurityInfoService.getInfo(username);
    return new ResponseEntity<>(info, HttpStatus.OK);
  }
}
