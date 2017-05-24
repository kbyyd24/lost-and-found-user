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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserInfoDTO that = (UserInfoDTO) o;

    if (getNickName() != null ? !getNickName().equals(that.getNickName()) : that.getNickName() != null) return false;
    if (getAbout() != null ? !getAbout().equals(that.getAbout()) : that.getAbout() != null) return false;
    if (getWeiboName() != null ? !getWeiboName().equals(that.getWeiboName()) : that.getWeiboName() != null)
      return false;
    return getBlog() != null ? getBlog().equals(that.getBlog()) : that.getBlog() == null;
  }

  @Override
  public int hashCode() {
    int result = getNickName() != null ? getNickName().hashCode() : 0;
    result = 31 * result + (getAbout() != null ? getAbout().hashCode() : 0);
    result = 31 * result + (getWeiboName() != null ? getWeiboName().hashCode() : 0);
    result = 31 * result + (getBlog() != null ? getBlog().hashCode() : 0);
    return result;
  }
}
