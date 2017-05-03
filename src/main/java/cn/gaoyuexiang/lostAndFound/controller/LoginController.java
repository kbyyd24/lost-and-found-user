package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.annotation.UserController;
import cn.gaoyuexiang.lostAndFound.enums.UserState;
import cn.gaoyuexiang.lostAndFound.exception.MissParamException;
import cn.gaoyuexiang.lostAndFound.exception.PasswordNotMatchException;
import cn.gaoyuexiang.lostAndFound.exception.UserNotExistException;
import cn.gaoyuexiang.lostAndFound.model.dto.LoginToken;
import cn.gaoyuexiang.lostAndFound.model.dto.LoginUser;
import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.enums.LoginMsg;
import cn.gaoyuexiang.lostAndFound.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static cn.gaoyuexiang.lostAndFound.enums.LoginMsg.MSG_NOT_COMPLETE;
import static cn.gaoyuexiang.lostAndFound.enums.LoginMsg.USERNAME_PASSWORD_NOT_MATCH;
import static cn.gaoyuexiang.lostAndFound.enums.UserState.OFFLINE;
import static cn.gaoyuexiang.lostAndFound.enums.UserState.ONLINE;
import static org.springframework.http.HttpStatus.*;

@UserController
public class LoginController {

  private LoginService loginService;
  private Map<UserState, HttpStatus> stateMap;
  private Map<LoginMsg, HttpStatus> logoutResMap;

  @Autowired
  public LoginController(LoginService loginService) {
    this.loginService = loginService;

    stateMap = new HashMap<>();
    stateMap.put(ONLINE, OK);
    stateMap.put(OFFLINE, NOT_FOUND);
    stateMap.put(UserState.UNAUTHORIZED, UNAUTHORIZED);

    logoutResMap = new HashMap<>();
    logoutResMap.put(LoginMsg.LOGOUT_SUCCESS, OK);
    logoutResMap.put(LoginMsg.UNAUTHORIZED, UNAUTHORIZED);
    logoutResMap.put(LoginMsg.OFFLINE, NOT_FOUND);
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

  @GetMapping(value = "login/{username}", headers = "user-token")
  public ResponseEntity<Message> checkState(@PathVariable String username,
                                            @RequestHeader(name = "user-token", defaultValue = "") String token) {
    UserState state = loginService.checkState(username, token);
    return new ResponseEntity<>(new Message(state.name()), stateMap.get(state));
  }

  @DeleteMapping(value = "login/{username}", headers = "user-token")
  public ResponseEntity<Message> logout(@PathVariable String username,
                                        @RequestHeader(value = "user-token", defaultValue = "") String token) {
    LoginMsg logoutMsg = loginService.logout(username, token);
    return new ResponseEntity<>(new Message(logoutMsg.getMsg()), logoutResMap.get(logoutMsg));
  }
}
