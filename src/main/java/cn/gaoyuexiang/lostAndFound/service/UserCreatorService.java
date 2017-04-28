package cn.gaoyuexiang.lostAndFound.service;

import cn.gaoyuexiang.lostAndFound.model.dto.SignInUser;
import cn.gaoyuexiang.lostAndFound.enums.CreatorMsg;
import org.springframework.stereotype.Service;

@Service
public interface UserCreatorService {

  CreatorMsg create(SignInUser signInUser);

}
