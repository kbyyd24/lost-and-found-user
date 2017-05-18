package cn.gaoyuexiang.lostAndFound.model.dto;

public class LoginResponse {

  private String username;
  private String email;
  private String token;

  public LoginResponse() {}

  public LoginResponse(String username, String email, String token) {
    this.username = username;
    this.email = email;
    this.token = token;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LoginResponse that = (LoginResponse) o;

    if (getUsername() != null ? !getUsername().equals(that.getUsername()) : that.getUsername() != null) return false;
    if (getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null) return false;
    return getToken() != null ? getToken().equals(that.getToken()) : that.getToken() == null;
  }

  @Override
  public int hashCode() {
    int result = getUsername() != null ? getUsername().hashCode() : 0;
    result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
    result = 31 * result + (getToken() != null ? getToken().hashCode() : 0);
    return result;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
