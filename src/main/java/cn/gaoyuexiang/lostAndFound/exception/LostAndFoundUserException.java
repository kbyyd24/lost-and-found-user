package cn.gaoyuexiang.lostAndFound.exception;

public class LostAndFoundUserException extends RuntimeException{
  public LostAndFoundUserException() {
    super();
  }

  public LostAndFoundUserException(String message) {
    super(message);
  }
}
