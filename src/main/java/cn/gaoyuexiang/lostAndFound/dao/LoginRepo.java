package cn.gaoyuexiang.lostAndFound.dao;

import cn.gaoyuexiang.lostAndFound.model.persistence.Login;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepo extends CrudRepository<Login, String> {

  Login findByUsernameAndToken(String username, String token);

  Login findByUsername(String username);
}
