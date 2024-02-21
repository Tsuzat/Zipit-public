package org.example.redirect.dao;

import org.example.redirect.bean.URLBean;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LilyRedirectDAO extends MongoRepository<URLBean, String>{
	Optional<URLBean> findURLBeanByShorten(String shorten);
}
