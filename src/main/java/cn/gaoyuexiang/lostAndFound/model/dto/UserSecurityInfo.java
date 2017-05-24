package cn.gaoyuexiang.lostAndFound.model.dto;

public class UserSecurityInfo {

  private String username;
  private String email;
  private boolean emailEnable;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserSecurityInfo that = (UserSecurityInfo) o;

    if (isEmailEnable() != that.isEmailEnable()) return false;
    if (getUsername() != null ? !getUsername().equals(that.getUsername()) : that.getUsername() != null) return false;
    return getEmail() != null ? getEmail().equals(that.getEmail()) : that.getEmail() == null;
  }

  @Override
  public int hashCode() {
    int result = getUsername() != null ? getUsername().hashCode() : 0;
    result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
    result = 31 * result + (isEmailEnable() ? 1 : 0);
    return result;
  }

  public UserSecurityInfo() {}

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

  public boolean isEmailEnable() {
    return emailEnable;
  }

  public void setEmailEnable(boolean emailEnable) {
    this.emailEnable = emailEnable;
  }
}
