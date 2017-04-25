package cn.gaoyuexiang.lostAndFound.service;

import cn.gaoyuexiang.lostAndFound.model.dto.SecurityInfoUpdater;
import cn.gaoyuexiang.lostAndFound.model.dto.UserSecurityInfo;

public interface UserSecurityInfoService {

  UserSecurityInfo getInfo(String username);

  String updateInfo(SecurityInfoUpdater updater, String username);
}
