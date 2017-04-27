package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.annotation.UserEmailController;
import cn.gaoyuexiang.lostAndFound.model.dto.EmailToken;
import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.service.EmailVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.mail.MessagingException;

import static org.springframework.http.HttpStatus.*;

@UserEmailController
public class EmailController {

  private EmailVerificationService emailVerificationService;

  @Autowired
  public EmailController(EmailVerificationService emailVerificationService) {
    this.emailVerificationService = emailVerificationService;
  }

  @PutMapping(path = "verification/application", headers = "user-token")
  public ResponseEntity<Message> apply(@PathVariable("email") String email,
                                       @RequestHeader(name = "user-token") String token) {
    try {
      String result = emailVerificationService.apply(email, token);
      switch (result) {
        case "success":
          return new ResponseEntity<>(new Message(result), OK);
        case "unauthorized":
          return new ResponseEntity<>(new Message(result), UNAUTHORIZED);
        case "user not found":
          return new ResponseEntity<>(new Message(result), NOT_FOUND);
      }
    } catch (MessagingException e) {
      return new ResponseEntity<>(new Message("something wrong"), INTERNAL_SERVER_ERROR);
    }
    return null;
  }

  @PutMapping(path = "verification", headers = {"username", "user-token"})
  public ResponseEntity<Message> verify(@RequestBody EmailToken emailToken,
                                        @PathVariable("email") String email,
                                        @RequestHeader("user-token") String userToken,
                                        @RequestHeader("username") String username) {
    String verifyResult =
        emailVerificationService.verify(username, userToken, email, emailToken.getToken());
    switch (verifyResult) {
      case "success":
        return new ResponseEntity<Message>(new Message(verifyResult), OK);
      case "timeout":
        return new ResponseEntity<Message>(new Message("token expired"), GONE);
      case "unauthorized":
        return new ResponseEntity<Message>(new Message(verifyResult), UNAUTHORIZED);
      default:
        return new ResponseEntity<Message>(new Message("unknown result"), NOT_FOUND);
    }
  }

}
