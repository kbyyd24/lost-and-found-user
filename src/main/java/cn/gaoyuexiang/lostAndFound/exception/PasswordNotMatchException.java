package cn.gaoyuexiang.lostAndFound.exception;

public class PasswordNotMatchException extends LostAndFoundUserException {
  public PasswordNotMatchException() {
    super();
  }

  public PasswordNotMatchException(String message) {
    super(message);
  }
}
