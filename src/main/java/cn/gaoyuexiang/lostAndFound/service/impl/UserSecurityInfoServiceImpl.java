package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.dao.UserRepo;
import cn.gaoyuexiang.lostAndFound.exception.UserNotExistException;
import cn.gaoyuexiang.lostAndFound.model.dto.SecurityInfoUpdater;
import cn.gaoyuexiang.lostAndFound.model.dto.UserSecurityInfo;
import cn.gaoyuexiang.lostAndFound.model.persistence.User;
import cn.gaoyuexiang.lostAndFound.service.UserSecurityInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityInfoServiceImpl implements UserSecurityInfoService {

  private UserRepo userRepo;

  @Autowired
  public UserSecurityInfoServiceImpl(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public UserSecurityInfo getInfo(String username) {
    User info = userRepo.findByUsername(username);
    if (info == null) {
      throw new UserNotExistException();
    }
    return buildInfo(username, info);
  }

  private UserSecurityInfo buildInfo(String username, User info) {
    UserSecurityInfo resultInfo = new UserSecurityInfo();
    resultInfo.setUsername(username);
    resultInfo.setEmail(info.getEmail());
    resultInfo.setEmailEnable(info.getEmailEnable());
    return resultInfo;
  }

  @Override
  public void updateInfo(SecurityInfoUpdater updater, String username) {
    User user = userRepo.findByUsername(username);
    if (!needUpdate(updater, user)) return ;
    user.setEmail(updater.getEmail());
    user.setEmailEnable(false);
    userRepo.save(user);
  }

  private boolean needUpdate(SecurityInfoUpdater updater, User user) {
    return !updater.getEmail().equals(user.getEmail());
  }

}
