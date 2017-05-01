package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

@Service
public class SystemTimeService implements TimeService {

  private long expireTime;
  private String pattern;

  @Autowired
  public SystemTimeService(
      @Value("#{systemProperties['lost-and-found.time.expire-time'] ?: 1800000}") long expireTime,
      @Value("#{systemProperties['lost-and-found.time.pattern'] ?: 'uuuu年MM月dd日 kk:mm:ss'}") String pattern) {
    this.expireTime = expireTime;
    this.pattern = pattern;
  }

  @Override
  public long now() {
    return System.currentTimeMillis();
  }

  @Override
  public String nowAsFormatString() {
    long now = this.now();
    Instant instant = Instant.ofEpochMilli(now);
    LocalDateTime date = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Shanghai"));
    return date.format(DateTimeFormatter.ofPattern(pattern));
  }

  @Override
  public boolean isExpire(long startTime) {
    long now = this.now();
    return now > startTime + expireTime;
  }

  @Override
  public long getExpireTime(long startTime) {
    return startTime + expireTime;
  }

  @Override
  public String getExpireTimeAsFormatString(long startTime) {
    Instant instant = Instant.ofEpochMilli(startTime + expireTime);
    LocalDateTime date = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Shanghai"));
    return date.format(DateTimeFormatter.ofPattern(pattern));
  }
}
