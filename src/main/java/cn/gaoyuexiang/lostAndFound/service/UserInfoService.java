package cn.gaoyuexiang.lostAndFound.service;

import cn.gaoyuexiang.lostAndFound.model.dto.UserInfoDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserInfoService {

  void createInfo(UserInfoDTO userInfoDTO, String username);

  UserInfoDTO getInfo(String username);

}
