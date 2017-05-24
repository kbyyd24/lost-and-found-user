package cn.gaoyuexiang.lostAndFound.dao;

import cn.gaoyuexiang.lostAndFound.model.persistence.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepo extends CrudRepository<User, String> {

  User findByUsernameOrEmail(String username, String email);

  User findByUsername(String username);

  User findByEmail(String email);

  @Transactional
  @Modifying
  @Query("update User user set user.emailEnable = true where user.username = :username")
  void enableEmailByUsername(@Param("username") String username);

}
