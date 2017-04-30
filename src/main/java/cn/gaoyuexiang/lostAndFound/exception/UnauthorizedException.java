package cn.gaoyuexiang.lostAndFound.exception;

public class UnauthorizedException extends LostAndFoundUserException {
  public UnauthorizedException() {
    super();
  }

  public UnauthorizedException(String message) {
    super(message);
  }
}
