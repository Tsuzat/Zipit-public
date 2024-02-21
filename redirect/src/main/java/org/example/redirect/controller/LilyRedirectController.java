package org.example.redirect.controller;

import lombok.RequiredArgsConstructor;
import org.example.redirect.bean.URLBean;
import org.example.redirect.dao.LilyRedirectDAO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class LilyRedirectController {

	private final LilyRedirectDAO lilyRedirectDAO;

	@GetMapping("/")
	public ResponseEntity<String> hello(){
		return new ResponseEntity<>("Welcome!!!", HttpStatus.OK);
	}

	@GetMapping("/{shorten}")
	public ResponseEntity<?> redirectMe(
			@PathVariable String shorten
	){
		HttpHeaders headers = new HttpHeaders();
		// Find the shorten URL
		Optional<URLBean> urlBean = lilyRedirectDAO.findURLBeanByShorten(shorten);
		if (urlBean.isEmpty()){
      // TODO: put the URL for your Error page 
      String errorUrl = "http://localhost:5173/error";
			headers.setLocation(URI.create(errorUrl));
			return new ResponseEntity<>(headers, HttpStatus.FOUND);
		}
		URLBean url = urlBean.get();
		if (new Date(System.currentTimeMillis()).after(url.getExpiresOn()))
			return new ResponseEntity<>("URL Expired", HttpStatus.BAD_REQUEST);
		headers.setLocation(URI.create(urlBean.get().getOriginal()));
		return new ResponseEntity<>(headers, HttpStatus.FOUND);
	}
}
