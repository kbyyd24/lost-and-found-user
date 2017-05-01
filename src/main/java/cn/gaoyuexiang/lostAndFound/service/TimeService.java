package cn.gaoyuexiang.lostAndFound.service;

public interface TimeService {

  long now();

  String nowAsFormatString();

  boolean isExpire(long startTime);

  long getExpireTime(long  startTime);

  String getExpireTimeAsFormatString(long startTime);

}
