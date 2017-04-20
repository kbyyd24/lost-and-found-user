package cn.gaoyuexiang.lostAndFound.exception;

public class UserNotExistException extends LostAndFoundUserException{
  public UserNotExistException() {
    super();
  }

  public UserNotExistException(String message) {
    super(message);
  }
}
