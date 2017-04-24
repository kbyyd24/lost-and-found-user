package cn.gaoyuexiang.lostAndFound.model.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_info")
public class UserInfo {

  @Id @Column(unique = true)
  private String username;

  private String about;

  @Column(name = "nick_name")
  private String nickName;

  @Column(name = "weibo_name")
  private String weiboName;

  private String blog;

  public UserInfo() {
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getAbout() {
    return about;
  }

  public void setAbout(String about) {
    this.about = about;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public String getWeiboName() {
    return weiboName;
  }

  public void setWeiboName(String weiboName) {
    this.weiboName = weiboName;
  }

  public String getBlog() {
    return blog;
  }

  public void setBlog(String blog) {
    this.blog = blog;
  }
}
