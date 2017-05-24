package cn.gaoyuexiang.lostAndFound.model.dto;

public class LoginUser {

  private String loginName;
  private String password;

  public LoginUser() {}

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
