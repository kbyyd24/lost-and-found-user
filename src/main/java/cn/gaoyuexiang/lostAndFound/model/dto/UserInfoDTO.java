package cn.gaoyuexiang.lostAndFound.model.dto;

public class UserInfoDTO {

  private String nickName;
  private String about;
  private String weiboName;
  private String blog;

  public UserInfoDTO() {}

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public String getAbout() {
    return about;
  }

  public void setAbout(String about) {
    this.about = about;
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
