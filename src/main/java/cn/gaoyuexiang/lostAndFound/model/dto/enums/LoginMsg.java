package cn.gaoyuexiang.lostAndFound.model.dto.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LoginMsg {
  MSG_NOT_COMPLETE("miss username or password"),
  USERNAME_PASSWORD_NOT_MATCH("username and password not match");

  private String msg;

  LoginMsg(String msg) {
    this.msg = msg;
  }

  @JsonValue
  public String getMsg() {
    return msg;
  }
}
