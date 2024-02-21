package org.example.backend.controller.url;

import lombok.RequiredArgsConstructor;
import org.example.backend.bean.URLBean;
import org.example.backend.bean.UserBean;
import org.example.backend.bean.url.UrlCreationRequestBean;
import org.example.backend.bean.url.UrlDeletionRequestBean;
import org.example.backend.bean.url.UrlDeletionResponseBean;
import org.example.backend.dao.LilyUserDAO;
import org.example.backend.servies.url.LilyUrlService;
import org.example.backend.utilies.LilyBasicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/url")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class LilyUrlController {

	@Autowired
	private LilyUrlService lilyUrlService;

	@Autowired
	private LilyUserDAO lilyUserDAO;

	@PostMapping("/add")
	public ResponseEntity<?> addNewUrl(
			@RequestBody UrlCreationRequestBean request
	) {
		// Get the UserName
		String userName = LilyBasicUtils.getCurrentUserName();
		// Check if the user have exhausted limit
		Optional<UserBean> optionalUserBean = lilyUserDAO.findUserBeanByEmail(userName);
		if (optionalUserBean.isEmpty()) return new ResponseEntity<>("User Not Found", HttpStatus.BAD_REQUEST);

		UserBean userBean = optionalUserBean.get();
		Integer maxUrlLimit = userBean.getMaxUrls();

		Integer urlCount = lilyUrlService.countUrlsByUserId(userName);
		if (urlCount >= maxUrlLimit) {
			return new ResponseEntity<>("Url creation limit exceeded.", HttpStatus.BAD_REQUEST);
		}

		try {
			URLBean response = lilyUrlService.createShortURL(request.getOriginal(), userName, request.getAlias());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Can't create short URL", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/delete")
	public ResponseEntity<UrlDeletionResponseBean> deleteUrl(
			@RequestBody UrlDeletionRequestBean request
	) {
		String userName = LilyBasicUtils.getCurrentUserName();
		var response = lilyUrlService.deleteShortURL(request.getShorten(), userName);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/allUrls")
	public ResponseEntity<Collection<URLBean>> getAllUrls() {
		String userName = LilyBasicUtils.getCurrentUserName();
		return ResponseEntity.ok(lilyUrlService.getAllURLsByUserName(userName));
	}

	@PostMapping("/check-alias")
	public ResponseEntity<Boolean> checkAlias(
			@RequestBody Map<String, String> data
	) {
		String alias = data.get("alias");
		boolean isAliasPresent = lilyUrlService.isShortUrlPresent(alias);
		if (isAliasPresent) return new ResponseEntity<>(true, HttpStatus.OK);
		else return new ResponseEntity<>(false, HttpStatus.ACCEPTED);
	}
}
