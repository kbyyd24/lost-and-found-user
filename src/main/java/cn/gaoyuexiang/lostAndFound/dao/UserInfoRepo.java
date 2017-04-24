package cn.gaoyuexiang.lostAndFound.dao;

import cn.gaoyuexiang.lostAndFound.model.persistence.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepo extends CrudRepository<UserInfo, String> {

  UserInfo findByUsername(String username);

}
