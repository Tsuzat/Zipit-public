package org.example.backend.utilies;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Random;

public class LilyBasicUtils {

	public static String getCurrentUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			Object principal = auth.getPrincipal();
			if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
				return ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
			}
		}
		return "Anonymous";
	}

	public static String generateRandomHash(Integer length) {
		Random random = new Random();
		char[] bits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
		return NanoIdUtils.randomNanoId(random, bits, length);
	}

}
