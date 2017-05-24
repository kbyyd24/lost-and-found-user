package cn.gaoyuexiang.lostAndFound.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CreatorMsg {
  SUCCESS("create user success"),
  MSG_NOT_ENOUGH("msg not enough"),
  USERNAME_EXIST("username exists"),
  EMAIL_EXIST("email exist");

  private String msg;

  CreatorMsg(String msg) {
    this.msg = msg;
  }

  @JsonValue
  public String getMsg() {
    return msg;
  }
}
