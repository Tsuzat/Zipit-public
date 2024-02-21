package org.example.backend.servies.url;

import lombok.RequiredArgsConstructor;
import org.example.backend.bean.URLBean;
import org.example.backend.bean.url.UrlDeletionResponseBean;
import org.example.backend.dao.LilyUrlDAO;
import org.example.backend.utilies.LilyBasicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LilyUserServiceImpl implements LilyUrlService {

	@Autowired
	private LilyUrlDAO lilyUrlDAO;


	@Override
	public URLBean createShortURL(
			String originalURL,
			String userName,
			String alias
	) throws Exception {
		String shorten;
		if (alias != null && !alias.isEmpty() && !alias.isBlank()) {
			shorten = alias;
		} else {
			shorten = LilyBasicUtils.generateRandomHash(10);
		}
		int tmpCount = 0;
		int daysToExpire = 10;
		/// We'll give it 10 retries
		while (lilyUrlDAO.existsURLBeanByShorten(shorten)) {
			shorten = LilyBasicUtils.generateRandomHash(10);
			tmpCount++;
			if (tmpCount >= 10) {
				throw new Exception("URL Can not be created");
			}
		}

		URLBean urlBean = URLBean
				.builder()
				.userId(userName)
				.createdOn(new Date(System.currentTimeMillis()))
				.expiresOn(new Date(System.currentTimeMillis() + daysToExpire * 86400 * 1000))
				.original(originalURL)
				.shorten(shorten)
				.build();
		urlBean = lilyUrlDAO.save(urlBean);
		return urlBean;
	}

	@Override
	public UrlDeletionResponseBean deleteShortURL(String shorten, String userName) {
		Optional<URLBean> urlBean = lilyUrlDAO.findURLBeanByShortenAndUserId(shorten, userName);
		if (urlBean.isEmpty()) {
			return UrlDeletionResponseBean
					.builder()
					.message(String.format("Shorten url %s may not belong to user %s", shorten, userName))
					.build();
		}
		lilyUrlDAO.delete(urlBean.get());
		return UrlDeletionResponseBean
				.builder()
				.message(String.format("Shorten url %s is deleted successfully", shorten))
				.build();
	}

	@Override
	public Collection<URLBean> getAllURLsByUserName(String userName) {
		return lilyUrlDAO.findURLBeansByUserId(userName);
	}

	@Override
	public Integer countUrlsByUserId(String userId) {
		return lilyUrlDAO.countURLBeansByUserId(userId);
	}

	@Override
	public Boolean isShortUrlPresent(String shortUrl) {
		return lilyUrlDAO.existsURLBeanByShorten(shortUrl);
	}
}
