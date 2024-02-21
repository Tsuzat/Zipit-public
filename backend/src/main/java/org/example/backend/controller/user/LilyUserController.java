package org.example.backend.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.backend.bean.UserBean;
import org.example.backend.dao.LilyUrlDAO;
import org.example.backend.dao.LilyUserDAO;
import org.example.backend.utilies.LilyBasicUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LilyUserController {

	private final LilyUserDAO lilyUserDAO;
	private final LilyUrlDAO lilyUrlDAO;

	@GetMapping("/hello")
	public ResponseEntity<Map<String, String>> hello() {
		Map<String, String> mp = new HashMap<>();
		mp.put("message", String.format("Welcome, %s", LilyBasicUtils.getCurrentUserName()));
		return ResponseEntity.ok(mp);
	}

	@GetMapping("/get")
	public ResponseEntity<UserBean> getUser() {
		// Get the userName
		String email = LilyBasicUtils.getCurrentUserName();
		Optional<UserBean> userBeanOptional = lilyUserDAO.findUserBeanByEmail(email);
		return userBeanOptional
				.map(ResponseEntity::ok)
				.orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteUser() {
		String email = LilyBasicUtils.getCurrentUserName();
		try {
			lilyUrlDAO.deleteAllByUserId(email);
			lilyUserDAO.deleteUserBeanByEmail(email);
			return new ResponseEntity<>("%s deleted successfully.".formatted(email), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
