package cn.gaoyuexiang.lostAndFound.service.impl;

import cn.gaoyuexiang.lostAndFound.LostAndFoundUserApplication;
import cn.gaoyuexiang.lostAndFound.service.TimeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LostAndFoundUserApplication.class)
public class SystemTimeServiceTest {

  @Autowired
  private TimeService timeService;

  @Test
  public void just_show() throws Exception {
    long startTime = System.currentTimeMillis();
    System.out.println(timeService.now());
    System.out.println(timeService.nowAsFormatString());
    System.out.println(timeService.getExpireTime(startTime));
    System.out.println(timeService.getExpireTimeAsFormatString(startTime));
    System.out.println(timeService.isExpire(startTime));
  }
}