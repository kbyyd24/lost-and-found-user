package cn.gaoyuexiang.lostAndFound.exception;

public class OfflineException extends LostAndFoundUserException{
  public OfflineException() {
    super();
  }

  public OfflineException(String message) {
    super(message);
  }
}
