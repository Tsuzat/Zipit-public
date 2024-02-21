package org.example.backend.servies.url;

import org.example.backend.bean.URLBean;
import org.example.backend.bean.url.UrlDeletionResponseBean;

import java.util.Collection;

public interface LilyUrlService {
	URLBean createShortURL(
			String originalURL,
			String userName,
			String alias
	) throws Exception;


	UrlDeletionResponseBean deleteShortURL(
			String shorten,
			String userName
	);

	Collection<URLBean> getAllURLsByUserName(String userName);


	Integer countUrlsByUserId(String userId);

	Boolean isShortUrlPresent(String shortUrl);
}
