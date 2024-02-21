package org.example.backend.dao;

import org.example.backend.bean.UserBean;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LilyUserDAO extends MongoRepository<UserBean, Integer> {

	Optional<UserBean> findUserBeanByEmail(String email);

	Optional<UserBean> findUserBeanByEmailAndPassword(String email, String password);

	Optional<UserBean> findUserBeanByEmailAndToken(String email, String token);

	void deleteUserBeanByEmail(String email);
}
