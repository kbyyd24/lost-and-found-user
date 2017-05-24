package cn.gaoyuexiang.lostAndFound.controller;

import cn.gaoyuexiang.lostAndFound.annotation.UserEmailController;
import cn.gaoyuexiang.lostAndFound.enums.EmailVerifyMsg;
import cn.gaoyuexiang.lostAndFound.model.dto.EmailToken;
import cn.gaoyuexiang.lostAndFound.model.dto.Message;
import cn.gaoyuexiang.lostAndFound.service.EmailVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@CrossOrigin(origins = "*")
@UserEmailController
public class EmailController {

  private EmailVerificationService emailVerificationService;

  private Map<EmailVerifyMsg, HttpStatus> responseMap;

  @Autowired
  public EmailController(EmailVerificationService emailVerificationService) {
    this.emailVerificationService = emailVerificationService;
    this.responseMap = new HashMap<>();
    this.responseMap.put(EmailVerifyMsg.SUCCESS, OK);
    this.responseMap.put(EmailVerifyMsg.UNAUTHORIZED, UNAUTHORIZED);
    this.responseMap.put(EmailVerifyMsg.OFFLINE, UNAUTHORIZED);
    this.responseMap.put(EmailVerifyMsg.EMAIL_NOT_FOUND, NOT_FOUND);
    this.responseMap.put(EmailVerifyMsg.EMAIL_ENABLED, CONFLICT);
    this.responseMap.put(EmailVerifyMsg.TOKEN_TIMEOUT, GONE);
  }

  @PutMapping(path = "verification/application", headers = "user-token")
  public ResponseEntity<Message> apply(@PathVariable("email") String email,
                                       @RequestHeader(name = "user-token") String token) throws MessagingException {
    EmailVerifyMsg result = emailVerificationService.apply(email, token);
    HttpStatus responseStatus = this.responseMap.compute(result,
        (emailVerifyMsg, httpStatus) -> httpStatus == null ? INTERNAL_SERVER_ERROR : httpStatus);
    return  new ResponseEntity<>(new Message(result.name()), responseStatus);
  }

  @PutMapping(path = "verification", headers = {"username", "user-token"})
  public ResponseEntity<Message> verify(@RequestBody EmailToken emailToken,
                                        @PathVariable("email") String email,
                                        @RequestHeader("user-token") String userToken,
                                        @RequestHeader("username") String username) {
    EmailVerifyMsg verifyResult =
        emailVerificationService.verify(username, userToken, email, emailToken.getToken());
    HttpStatus responseStatus = this.responseMap.compute(verifyResult,
        (emailVerifyMsg, httpStatus) -> httpStatus == null ? INTERNAL_SERVER_ERROR : httpStatus);
    return new ResponseEntity<>(new Message(verifyResult.name()), responseStatus);
  }

  @ExceptionHandler(MessagingException.class)
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  public Message handleMessagingException() {
    return new Message("send email failed");
  }
}
