package cn.gaoyuexiang.lostAndFound.dao;

import cn.gaoyuexiang.lostAndFound.model.persistence.EmailVerifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerifierRepo extends CrudRepository<EmailVerifier, String> {

  EmailVerifier findByEmail(String email);

}
