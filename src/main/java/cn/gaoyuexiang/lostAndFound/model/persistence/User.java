package cn.gaoyuexiang.lostAndFound.model.persistence;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

  @Id
  private String id;

  private String username;

  private String email;

  @Column(name = "email_enable")
  private Boolean emailEnable;

  private String password;

  public User() {}

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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Boolean getEmailEnable() {
    return emailEnable;
  }

  public void setEmailEnable(Boolean emailEnable) {
    this.emailEnable = emailEnable;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
