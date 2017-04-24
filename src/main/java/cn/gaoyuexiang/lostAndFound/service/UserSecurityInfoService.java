package cn.gaoyuexiang.lostAndFound.service;

import cn.gaoyuexiang.lostAndFound.model.dto.UserSecurityInfo;

public interface UserSecurityInfoService {

  UserSecurityInfo getInfo(String username);

  String updateInfo(UserSecurityInfo updateUser);

}
