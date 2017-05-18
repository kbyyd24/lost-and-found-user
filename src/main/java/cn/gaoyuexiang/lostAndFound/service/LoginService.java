package cn.gaoyuexiang.lostAndFound.service;

import cn.gaoyuexiang.lostAndFound.enums.LoginMsg;
import cn.gaoyuexiang.lostAndFound.enums.UserState;
import cn.gaoyuexiang.lostAndFound.model.dto.LoginResponse;
import cn.gaoyuexiang.lostAndFound.model.dto.LoginToken;
import cn.gaoyuexiang.lostAndFound.model.dto.LoginUser;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {

  LoginResponse login(LoginUser loginUser);

  UserState checkState(String username, String token);

  LoginMsg logout(String username, String token);

}
