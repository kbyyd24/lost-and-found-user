package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.dao.UserInfoRepo;
import cn.gaoyuexiang.lostAndFound.model.dto.UserInfoDTO;
import cn.gaoyuexiang.lostAndFound.model.persistence.UserInfo;
import cn.gaoyuexiang.lostAndFound.service.IdCreatorService;
import cn.gaoyuexiang.lostAndFound.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

  private UserInfoRepo userInfoRepo;

  @Autowired
  public UserInfoServiceImpl(UserInfoRepo userInfoRepo) {
    this.userInfoRepo = userInfoRepo;
  }

  @Override
  public String createInfo(UserInfoDTO userInfoDTO, String username) {
    UserInfo userInfo = buildUserInfo(userInfoDTO, username);
    userInfoRepo.save(userInfo);
    return "success";
  }

  private UserInfo buildUserInfo(UserInfoDTO userInfoDTO, String username) {
    UserInfo userInfo = new UserInfo();
    userInfo.setUsername(username);
    userInfo.setNickName(userInfoDTO.getNickName());
    userInfo.setAbout(userInfoDTO.getAbout());
    userInfo.setBlog(userInfoDTO.getBlog());
    userInfo.setWeiboName(userInfoDTO.getWeiboName());
    return userInfo;
  }

  @Override
  public UserInfoDTO getInfo(String username) {
    return null;
  }
}
