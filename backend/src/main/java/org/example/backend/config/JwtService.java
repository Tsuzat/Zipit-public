package org.example.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

	/*  TODO:
		This secrete key needs to be generated either in compile time
		or needs to be pulled from environment variable
	* */
	private static final String SECRET_KEY = "sMW9kM2IzhGe0pUiG2DCmZ4AEmwNmYojKFO3D72rWQI=";

	/*  TODO:
		EXPIRY IN DAYS needs to be set as environment variable

	* */
	private static final Integer EXPIRY_IN_DAYS = 10;

	private static final Long EXPIRES_IN_MILISECONDS = (long) (EXPIRY_IN_DAYS * 86400 * 1000);


	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	public String generateToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails
	) {
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRES_IN_MILISECONDS))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public Boolean isTokenValid(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	public Claims extractAllClaims(String token) {
		try {
			return Jwts
					.parserBuilder()
					.setSigningKey(getSignInKey())
					.build()
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			// Log the exception for debugging purposes
			// TODO: Implement better logging method
			e.printStackTrace();
			throw e; // Rethrow the exception to maintain the existing behavior
		}
	}


	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
