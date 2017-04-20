package cn.gaoyuexiang.lostAndFound.dao;

import cn.gaoyuexiang.lostAndFound.model.persistence.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {

  User findByUsernameOrEmail(String username, String email);

}
