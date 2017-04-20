package cn.gaoyuexiang.lostAndFound.model.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "login")
public class Login {

  @Id
  private String id;

  @Column(unique = true)
  private String username;
  private String token;
  @Column(name = "login_time")
  private long loginTime;

  public Login() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Login login = (Login) o;

    if (getLoginTime() != login.getLoginTime()) return false;
    if (getId() != null ? !getId().equals(login.getId()) : login.getId() != null) return false;
    if (getUsername() != null ? !getUsername().equals(login.getUsername()) : login.getUsername() != null) return false;
    return getToken() != null ? getToken().equals(login.getToken()) : login.getToken() == null;
  }

  @Override
  public int hashCode() {
    int result = getId() != null ? getId().hashCode() : 0;
    result = 31 * result + (getUsername() != null ? getUsername().hashCode() : 0);
    result = 31 * result + (getToken() != null ? getToken().hashCode() : 0);
    result = 31 * result + (int) (getLoginTime() ^ (getLoginTime() >>> 32));
    return result;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public long getLoginTime() {
    return loginTime;
  }

  public void setLoginTime(long loginTime) {
    this.loginTime = loginTime;
  }
}
