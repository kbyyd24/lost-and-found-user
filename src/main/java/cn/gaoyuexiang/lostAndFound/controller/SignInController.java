package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.annotation.UserController;
import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.model.dto.SignInUser;
import cn.gaoyuexiang.lostAndFound.enums.CreatorMsg;
import cn.gaoyuexiang.lostAndFound.service.UserCreatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.OK;

@UserController
public class SignInController {

  private UserCreatorService userCreatorService;
  private HashMap<CreatorMsg, HttpStatus> creatorStatusMap;

  @Autowired
  public SignInController(UserCreatorService userCreatorService) {
    this.userCreatorService = userCreatorService;
    creatorStatusMap = new HashMap<>();
    creatorStatusMap.put(CreatorMsg.SUCCESS, OK);
    creatorStatusMap.put(CreatorMsg.EMAIL_EXIST, CONFLICT);
    creatorStatusMap.put(CreatorMsg.USERNAME_EXIST, CONFLICT);
    creatorStatusMap.put(CreatorMsg.MSG_NOT_ENOUGH, BAD_REQUEST);
  }

  @PostMapping
  public ResponseEntity<Message> signIn(@RequestBody SignInUser signInUser) {
    CreatorMsg creatorMsg = userCreatorService.create(signInUser);
    return new ResponseEntity<>(new Message(creatorMsg.getMsg()), creatorStatusMap.get(creatorMsg));
  }
}
