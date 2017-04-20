package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.service.TokenService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UUIDTokenService implements TokenService {
  @Override
  public String buildToken() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
