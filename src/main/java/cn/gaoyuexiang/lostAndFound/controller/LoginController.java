package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.annotation.UserController;
import cn.gaoyuexiang.lostAndFound.exception.MissParamException;
import cn.gaoyuexiang.lostAndFound.exception.PasswordNotMatchException;
import cn.gaoyuexiang.lostAndFound.exception.UserNotExistException;
import cn.gaoyuexiang.lostAndFound.model.dto.LoginToken;
import cn.gaoyuexiang.lostAndFound.model.dto.LoginUser;
import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.model.dto.enums.LoginMsg;
import cn.gaoyuexiang.lostAndFound.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static cn.gaoyuexiang.lostAndFound.model.dto.enums.LoginMsg.MSG_NOT_COMPLETE;
import static cn.gaoyuexiang.lostAndFound.model.dto.enums.LoginMsg.USERNAME_PASSWORD_NOT_MATCH;
import static org.springframework.http.HttpStatus.*;

@UserController
public class LoginController {

  private LoginService loginService;
  private Map<String, HttpStatus> stateMap;

  @Autowired
  public LoginController(LoginService loginService) {
    this.loginService = loginService;
    stateMap = new HashMap<>();
    stateMap.put("online", OK);
    stateMap.put("offline", NOT_FOUND);
    stateMap.put("unauthorized", UNAUTHORIZED);
  }

  @PostMapping("login")
  public LoginToken login(@RequestBody LoginUser loginUser) {
    return loginService.login(loginUser);
  }

  @ExceptionHandler(MissParamException.class)
  @ResponseStatus(BAD_REQUEST)
  public Message handleMissMsgException() {
    return new Message(MSG_NOT_COMPLETE.getMsg());
  }

  @ExceptionHandler({PasswordNotMatchException.class, UserNotExistException.class})
  @ResponseStatus(UNAUTHORIZED)
  public Message handleUnauthorizedException() {
    return new Message(USERNAME_PASSWORD_NOT_MATCH.getMsg());
  }

  @GetMapping("login/{username}")
  public ResponseEntity<Message> checkState(@PathVariable String username,
                                            @RequestHeader("user-token") String token) {
    String state = loginService.checkState(username, token);
    return new ResponseEntity<>(new Message(state), stateMap.get(state));
  }
}
