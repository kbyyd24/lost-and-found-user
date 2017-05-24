package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.service.EmailFormatService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SimpleHtmlEmailFormatter implements EmailFormatService {

  private String host = "localhost";
  private int port = 8080;

  @Override
  public String format(String username, String token, long expireTime) {
    String link = String.format("http://%s:%d/user/email/verification/%s", host, port, token);
    return String.format("<h2>%s,欢迎来到失物招领平台</h2><br>请在%s之前访问下面的链接:<br><a href='%s' target='blank'>%s</a>", username, new Date(expireTime).toString(), link, link);
  }
}
