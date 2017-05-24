package cn.gaoyuexiang.lostAndFound.model.dto;

public class LoginToken {

  private String token;

  public LoginToken() {}

  public LoginToken(String token) {
    this.token = token;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LoginToken that = (LoginToken) o;

    return getToken() != null ? getToken().equals(that.getToken()) : that.getToken() == null;
  }

  @Override
  public int hashCode() {
    return getToken() != null ? getToken().hashCode() : 0;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
