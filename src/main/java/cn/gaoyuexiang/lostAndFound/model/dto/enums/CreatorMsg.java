package cn.gaoyuexiang.lostAndFound.model.dto.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CreatorMsg {
  SUCCESS("create user success"),
  MSG_NOT_ENOUGH("msg not enough"),
  USERNAME_EXIST("username exists"),
  EMAIL_EXIST("email existe");

  private String msg;

  CreatorMsg(String msg) {
    this.msg = msg;
  }

  public String getMsg() {
    return msg;
  }
}
