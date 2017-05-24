package cn.gaoyuexiang.lostAndFound.service;

public interface EmailFormatService {

  String format(String username, String token, long expireTime);

}