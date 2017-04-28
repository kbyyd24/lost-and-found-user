package cn.gaoyuexiang.lostAndFound.service;

import cn.gaoyuexiang.lostAndFound.enums.UserState;
import cn.gaoyuexiang.lostAndFound.model.dto.LoginToken;
import cn.gaoyuexiang.lostAndFound.model.dto.LoginUser;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {

  LoginToken login(LoginUser loginUser);

  UserState checkState(String username, String token);

  String logout(String username, String token);

}
