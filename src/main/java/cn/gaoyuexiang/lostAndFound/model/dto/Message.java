package cn.gaoyuexiang.lostAndFound.model.dto;

public class Message {
  private String reason;

  public Message() {
  }

  public Message(String reason) {
    this.reason = reason;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}
