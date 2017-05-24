package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.dao.UserInfoRepo;
import cn.gaoyuexiang.lostAndFound.exception.UserNotExistException;
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
  public void createInfo(UserInfoDTO userInfoDTO, String username) {
    UserInfo userInfo = buildUserInfo(userInfoDTO, username);
    userInfoRepo.save(userInfo);
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
    UserInfo userInfo = userInfoRepo.findByUsername(username);
    if (userInfo == null) {
      throw new UserNotExistException();
    }
    return buildUserInfoDTO(userInfo);
  }

  private UserInfoDTO buildUserInfoDTO(UserInfo userInfo) {
    UserInfoDTO userInfoDTO = new UserInfoDTO();
    userInfoDTO.setNickName(userInfo.getNickName());
    userInfoDTO.setAbout(userInfo.getAbout());
    userInfoDTO.setBlog(userInfo.getBlog());
    userInfoDTO.setWeiboName(userInfo.getWeiboName());
    return userInfoDTO;
  }
}
