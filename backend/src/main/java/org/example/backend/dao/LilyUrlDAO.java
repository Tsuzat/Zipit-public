package org.example.backend.dao;

import org.example.backend.bean.URLBean;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.Optional;

public interface LilyUrlDAO extends MongoRepository<URLBean, String> {

	boolean existsURLBeanByShorten(String shorten);

	Optional<URLBean> findURLBeanByShortenAndUserId(String shorten, String userId);

	Collection<URLBean> findURLBeansByUserId(String userId);

	Integer countURLBeansByUserId(String userId);

	void deleteAllByUserId(String userId);
}
