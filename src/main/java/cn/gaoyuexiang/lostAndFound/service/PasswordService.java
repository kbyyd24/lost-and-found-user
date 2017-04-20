package cn.gaoyuexiang.lostAndFound.service;

import org.springframework.stereotype.Service;

@Service
public interface PasswordService {

  String encode(String password);

  boolean match(String password, String encodedPassword);

}
