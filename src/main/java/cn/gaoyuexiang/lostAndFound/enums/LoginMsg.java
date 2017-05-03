package cn.gaoyuexiang.lostAndFound.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LoginMsg {
  MSG_NOT_COMPLETE("miss username or password"),
  USERNAME_PASSWORD_NOT_MATCH("username and password not match"),
  USER_NOT_FOUND("user not found"),
  LOGOUT_SUCCESS("logout success"),
  OFFLINE("user offline"),
  UNAUTHORIZED("unauthorized");

  private String msg;

  LoginMsg(String msg) {
    this.msg = msg;
  }

  @JsonValue
  public String getMsg() {
    return msg;
  }
}
