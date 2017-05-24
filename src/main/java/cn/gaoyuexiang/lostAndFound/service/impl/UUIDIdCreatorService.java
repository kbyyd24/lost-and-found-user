package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.service.IdCreatorService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UUIDIdCreatorService implements IdCreatorService {
  @Override
  public String create() {
    return UUID.randomUUID().toString();
  }
}
