package cn.gaoyuexiang.lostAndFound.service;

import org.springframework.stereotype.Service;

@Service
public interface TokenService {

  String buildToken();

}
