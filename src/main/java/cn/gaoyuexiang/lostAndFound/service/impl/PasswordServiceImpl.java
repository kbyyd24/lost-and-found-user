package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.service.PasswordService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {

  private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(31);

  @Override
  public String encode(String password) {
    return passwordEncoder.encode(password);
  }

  @Override
  public boolean match(String password, String encodedPassword) {
    return passwordEncoder.matches(password, encodedPassword);
  }
}
